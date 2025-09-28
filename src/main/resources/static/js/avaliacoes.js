// Constantes de contexto
const LOGGED_USER_EMAIL = document.getElementById('logged-user-email') ? 
                          document.getElementById('logged-user-email').value : null;
const LIVRO_ID = document.getElementById('livro-id').value;
const CSRF_TOKEN = document.querySelector('input[name="_csrf"]').value;
const CSRF_HEADER_NAME = document.getElementById('_csrf_header').value;
    
// --- Funções de Interface (Menu, Estrelas, Renderização) ---

// CORREÇÃO ESSENCIAL: Anexando funções ao escopo global (window)
// Isso permite que o HTML dinâmico, que usa 'onclick', encontre as funções.
window.toggleDropdown = toggleDropdown;
window.handleEditClick = handleEditClick;
window.handleDeleteClick = handleDeleteClick;

// Função para renderizar as estrelas (simulada)
function renderStars(nota) {
    let starsHtml = '';
    for (let i = 1; i <= 5; i++) {
        starsHtml += `<i class="fa-star ${i <= nota ? 'fa-solid' : 'fa-regular'}" style="color: gold;"></i>`;
    }
    return starsHtml;
}

// Função para renderizar uma única avaliação (adaptada para incluir o menu)
function renderAvaliacao(avaliacao) {
    // CORREÇÃO: Usando 'emailUsuario' do DTO para a checagem de propriedade única
    const isOwner = LOGGED_USER_EMAIL && avaliacao.emailUsuario === LOGGED_USER_EMAIL; 
    
    const optionsMenu = isOwner ? `
        <div class="options-menu">
            <button id="options-btn-${avaliacao.id}" class="options-button" type="button" 
                    onclick="toggleDropdown('${avaliacao.id}')" title="Opções">
                <i class="fa-solid fa-ellipsis-vertical"></i>
            </button>
            <div id="dropdown-${avaliacao.id}" class="dropdown-content">
                <button type="button" 
                        onclick="handleEditClick(${avaliacao.id}, ${avaliacao.nota}, \`${avaliacao.comentario.replace(/`/g, '\\`')}\`)">
                    <i class="fa-solid fa-pen-to-square"></i> Editar
                </button>
                <button type="button" onclick="handleDeleteClick(${avaliacao.id})">
                    <i class="fa-solid fa-trash"></i> Excluir
                </button>
            </div>
        </div>
    ` : '';

    // Estrutura básica do item de avaliação (adapte às suas classes CSS)
    return `
        <div class="avaliacao-item" id="avaliacao-${avaliacao.id}">
            <div class="review-header">
                <div class="user-info" style="display: flex; align-items: center; gap: 10px;">
                    <img src="${avaliacao.fotoUsuario || '/img/user.png'}" alt="Foto do Usuário" style="width: 40px; height: 40px; border-radius: 50%;" />
                    <span class="nome-usuario">${avaliacao.nomeUsuario}</span>
                </div>
                ${optionsMenu}
            </div>
            <div class="avaliacao-estrelas-display" style="margin: 5px 0;">
                ${renderStars(avaliacao.nota)}
            </div>
            <p class="comentario-texto">${avaliacao.comentario}</p>
            <small>Avaliado em: ${new Date(avaliacao.dataAvaliacao).toLocaleDateString('pt-BR')}</small>
        </div>
        <hr/>
    `;
}

// Função para alternar o menu dropdown
function toggleDropdown(avaliacaoId) {
    const dropdown = document.getElementById(`dropdown-${avaliacaoId}`);
    // Fecha todos os outros menus abertos
    document.querySelectorAll('.dropdown-content').forEach(d => {
        if (d.id !== `dropdown-${avaliacaoId}`) {
            d.style.display = 'none';
        }
    });
    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
}


window.onclick = function(event) {
    if (!event.target.closest('.options-menu')) {
        document.querySelectorAll('.dropdown-content').forEach(d => {
            d.style.display = 'none';
        });
    }
}


function handleEditClick(avaliacaoId, currentNota, currentComentario) {
    toggleDropdown(avaliacaoId); // Fecha o menu
    
    let newNota = currentNota;

    Swal.fire({
        title: 'Editar sua Avaliação',
        // HTML para o formulário de edição
        html: `
            <div id="swal-rating-container" style="display: flex; justify-content: center; margin-bottom: 15px;">
                </div>
            <textarea id="swal-review-text" class="swal2-textarea" placeholder="Edite seu comentário..." 
                      style="height: 150px; width: 100%; border: 1px solid #ccc; padding: 10px; border-radius: 4px;">${currentComentario}</textarea>
        `,
        showCancelButton: true,
        confirmButtonText: 'Salvar Alterações',
        cancelButtonText: 'Cancelar',
        focusConfirm: false,
        didOpen: () => {
            const ratingContainer = document.getElementById('swal-rating-container');
            
            for (let i = 1; i <= 5; i++) {
                const star = document.createElement('i');
             
                star.className = `fa-star ${i <= currentNota ? 'fa-solid' : 'fa-regular'} rating-star`;
                star.style.cssText = 'color: gold; font-size: 2rem; cursor: pointer; margin: 0 5px;';
                star.setAttribute('data-nota', i);

                star.addEventListener('click', function() {
                    newNota = i;
            
                    ratingContainer.querySelectorAll('.rating-star').forEach(s => {
                        const notaAttr = parseInt(s.getAttribute('data-nota'));
                        s.className = `fa-star ${notaAttr <= newNota ? 'fa-solid' : 'fa-regular'} rating-star`;
                    });
                });
                ratingContainer.appendChild(star);
            }
        },
        preConfirm: () => {
            const newComentario = document.getElementById('swal-review-text').value.trim();
            if (!newComentario || newNota === 0) {
                Swal.showValidationMessage('Por favor, preencha o comentário e atribua uma nota.');
                return false;
            }
           
            return { comentario: newComentario, nota: newNota };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            
            updateAvaliacao(avaliacaoId, result.value.comentario, result.value.nota);
        }
    });
}


function updateAvaliacao(avaliacaoId, comentario, nota) {
    const url = `/api/livros/${LIVRO_ID}/avaliacoes/${avaliacaoId}`;
    const data = {
        comentario: comentario,
        nota: nota
    };

    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [CSRF_HEADER_NAME]: CSRF_TOKEN 
        },
        body: JSON.stringify(data)
    })
    .then(async response => {
        if (!response.ok) {
            const errorBody = await response.json();
            throw new Error(errorBody.message || 'Erro ao atualizar a avaliação.');
        }
        return response.json();
    })
    .then(updatedAvaliacao => {
        Swal.fire('Atualizado!', 'Sua avaliação foi atualizada com sucesso.', 'success');
        const avaliacaoElement = document.getElementById(`avaliacao-${avaliacaoId}`);
        if (avaliacaoElement) {
            
            avaliacaoElement.outerHTML = renderAvaliacao(updatedAvaliacao); 
        } else {
           
            loadAvaliacoes(); 
        }
       
    })
    .catch(error => {
        console.error('Erro na atualização:', error);
        Swal.fire('Erro!', error.message, 'error');
    });
}


function handleDeleteClick(avaliacaoId) {
    toggleDropdown(avaliacaoId); // Fecha o menu

    Swal.fire({
        title: 'Tem certeza?',
        text: 'Você não poderá reverter esta ação!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sim, deletar!',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            deleteAvaliacao(avaliacaoId);
        }
    });
}


function deleteAvaliacao(avaliacaoId) {
    const url = `/api/livros/${LIVRO_ID}/avaliacoes/${avaliacaoId}`;

    fetch(url, {
        method: 'DELETE',
        headers: {
            [CSRF_HEADER_NAME]: CSRF_TOKEN 
        }
    })
    .then(async response => {
        if (response.status === 204) { 
            Swal.fire('Deletado!', 'Sua avaliação foi removida.', 'success');
           
            const avaliacaoElement = document.getElementById(`avaliacao-${avaliacaoId}`);
            if (avaliacaoElement) {
                
                const nextSibling = avaliacaoElement.nextElementSibling;
                if (nextSibling && nextSibling.tagName === 'HR') {
                    nextSibling.remove();
                }
                avaliacaoElement.remove();
            }
            loadAvaliacoes(); 
        } else {
            const errorBody = response.headers.get('content-type')?.includes('application/json') ? await response.json() : { message: 'Erro desconhecido.' };
            throw new Error(errorBody.message || 'Erro ao deletar a avaliação.');
        }
    })
    .catch(error => {
        console.error('Erro na exclusão:', error);
        Swal.fire('Erro!', error.message, 'error');
    });
}

function loadAvaliacoes() {
    const listaDiv = document.getElementById('lista-avaliacoes');
    if (!listaDiv) return;

    fetch(`/api/livros/${LIVRO_ID}/avaliacoes`)
        .then(response => response.json())
        .then(avaliacoes => {
            listaDiv.innerHTML = '';
            let userHasCommented = false;
            
            avaliacoes.forEach(avaliacao => {
                listaDiv.innerHTML += renderAvaliacao(avaliacao);
                
                if (avaliacao.emailUsuario === LOGGED_USER_EMAIL) {
                    userHasCommented = true;
                }
            });
            

            const criarAvaliacaoBox = document.getElementById('avaliar-box');
            if (criarAvaliacaoBox) {
                if (userHasCommented) {
   
                    criarAvaliacaoBox.style.display = 'none';
                } else {
                    
                    criarAvaliacaoBox.style.display = 'flex'; 
                }
            }
        })
        .catch(error => console.error('Erro ao carregar avaliações:', error));
}


document.addEventListener('DOMContentLoaded', loadAvaliacoes);