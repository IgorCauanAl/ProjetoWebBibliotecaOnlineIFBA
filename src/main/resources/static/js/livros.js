// Aguarda o HTML ser completamente carregado antes de executar o script
document.addEventListener('DOMContentLoaded', () => {

    // --- Seletores de Elementos ---
    // Pega os elementos da página que vamos manipular
    const ratingStarsContainer = document.getElementById('rating-stars');
    const reviewTextArea = document.getElementById('review-text');
    const sendReviewButton = document.getElementById('send-review');
    const reviewListContainer = document.getElementById('lista-avaliacoes');
    
    // Pega o ID do livro a partir da URL (ex: /livros/15)
    const livroId = window.location.pathname.split('/').pop();

    let currentRating = 0; // Variável para guardar a nota (estrelas) selecionada

    // --- Funções ---

    /**
     * Cria o HTML para as estrelas de avaliação.
     */
    function renderStars(rating = 0) {
        ratingStarsContainer.innerHTML = ''; // Limpa as estrelas atuais
        for (let i = 1; i <= 5; i++) {
            const star = document.createElement('i');
            star.classList.add('fa-solid', 'fa-star');
            // Pinta a estrela se ela for menor ou igual à nota atual
            if (i <= rating) {
                star.style.color = '#ffd700';
            }
            star.dataset.value = i; // Guarda o valor da estrela (1, 2, 3, 4, 5)
            ratingStarsContainer.appendChild(star);
        }
    }

    /**
     * Cria o HTML para um card de avaliação individual.
     * @param {object} avaliacao - O objeto da avaliação vindo da API.
     * @returns {string} - O HTML do card.
     */
    function criarCardAvaliacao(avaliacao) {
        // Formata a data para um formato mais amigável
        const dataFormatada = new Date(avaliacao.dataAvaliacao).toLocaleDateString('pt-BR', {
            day: '2-digit', month: 'long', year: 'numeric'
        });

        let estrelasHtml = '';
        for (let i = 1; i <= 5; i++) {
            estrelasHtml += `<i class="fa-solid fa-star" style="color: ${i <= avaliacao.nota ? '#ffd700' : '#ccc'};"></i>`;
        }

        return `
            <div class="avaliacao-card">
                <img class="avaliacao-foto" src="${avaliacao.fotoUsuario || '/img/user.png'}" alt="Foto do usuário" />
                <div class="avaliacao-conteudo">
                    <div class="avaliacao-header">
                        <strong>${avaliacao.nomeUsuario}</strong>
                        <div class="avaliacao-estrelas">${estrelasHtml}</div>
                    </div>
                    <p>${avaliacao.comentario}</p>
                    <small style="color: #999; font-size: 1.2rem;">${dataFormatada}</small>
                </div>
            </div>
        `;
    }

    /**
     * Busca as avaliações na API e as exibe na tela.
     */
    async function carregarAvaliacoes() {
        try {
            const response = await fetch(`/api/livros/${livroId}/avaliacoes`);
            if (!response.ok) {
                throw new Error('Falha ao buscar avaliações.');
            }
            const avaliacoes = await response.json();

            if (avaliacoes.length > 0) {
                reviewListContainer.innerHTML = avaliacoes.map(criarCardAvaliacao).join('');
            } else {
                reviewListContainer.innerHTML = '<p style="text-align:center; color: #888;">Este livro ainda não tem avaliações. Seja o primeiro a comentar!</p>';
            }
        } catch (error) {
            console.error('Erro:', error);
            reviewListContainer.innerHTML = '<p style="text-align:center; color: red;">Não foi possível carregar as avaliações.</p>';
        }
    }

    /**
     * Envia uma nova avaliação para a API.
     */
    async function enviarAvaliacao() {
        const comentario = reviewTextArea.value.trim();
        const nota = currentRating;

        if (!comentario || nota === 0) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Por favor, escreva um comentário e selecione uma nota (estrelas)!',
            });
            return;
        }

        try {
            // Pega o token CSRF que o Spring Security adiciona na página
            const csrfToken = document.querySelector('input[name="_csrf"]').value;
            const csrfHeader = document.querySelector('input[name="_csrf_header"]').value;

            const response = await fetch(`/api/livros/${livroId}/avaliacoes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken // Adiciona o token CSRF no cabeçalho
                },
                body: JSON.stringify({ comentario, nota })
            });

            if (!response.ok) {
                 if (response.status === 401) { // Não autenticado
                    Swal.fire('Ops!', 'Você precisa estar logado para avaliar um livro.', 'warning');
                 } else {
                    throw new Error('Falha ao enviar avaliação.');
                 }
                 return;
            }
            
            const novaAvaliacao = await response.json();
            
            // Adiciona o novo comentário no topo da lista
            const primeiroComentario = reviewListContainer.querySelector('p');
            if (primeiroComentario) {
                reviewListContainer.innerHTML = criarCardAvaliacao(novaAvaliacao);
            } else {
                reviewListContainer.insertAdjacentHTML('afterbegin', criarCardAvaliacao(novaAvaliacao));
            }

            // Limpa os campos
            reviewTextArea.value = '';
            currentRating = 0;
            renderStars();

        } catch (error) {
            console.error('Erro ao enviar avaliação:', error);
            Swal.fire('Erro!', 'Não foi possível enviar sua avaliação.', 'error');
        }
    }


    // --- Event Listeners (Onde a mágica acontece) ---

    // Adiciona o evento de clique para as estrelas
    ratingStarsContainer.addEventListener('click', (e) => {
        if (e.target.classList.contains('fa-star')) {
            currentRating = parseInt(e.target.dataset.value, 10);
            renderStars(currentRating);
        }
    });

    // Adiciona o evento de clique para o botão de enviar
    sendReviewButton.addEventListener('click', enviarAvaliacao);


    // --- Inicialização ---
    // Quando a página carregar, renderiza as estrelas e busca os comentários
    renderStars();
    carregarAvaliacoes();
});