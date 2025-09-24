// Aguarda o carregamento completo do DOM para executar os scripts
document.addEventListener('DOMContentLoaded', function() {
    
    // --- SCRIPT PARA PREVIEW DA IMAGEM DE CAPA ---
    const capaInput = document.getElementById('capa-livro-input');
    if (capaInput) {
        capaInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const previewImage = document.getElementById('capa-preview');
                    const uploadBox = previewImage.closest('.cover-upload-box');
                    previewImage.src = e.target.result;
                    previewImage.style.display = 'block';
                    uploadBox.classList.add('has-image');
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // --- SCRIPT PARA LÓGICA DE SELEÇÃO DO AUTOR ---
    const tipoAutorRadios = document.querySelectorAll('input[name="tipoAutor"]');
    const autorExistenteSection = document.getElementById('autor-existente-section');
    const novoAutorSection = document.getElementById('novo-autor-section');

    const autorIdSelect = document.getElementById('autor-existente');
    const novoAutorNomeInput = document.getElementById('novo-nome-autor');
    const novoAutorDescricaoTextarea = document.getElementById('novo-descricao-autor');
    
    // Função para alternar a visibilidade e o estado dos campos do autor
    function toggleAutorFields(tipo) {
        if (tipo === 'existente') {
            autorExistenteSection.style.display = 'block';
            novoAutorSection.style.display = 'none';
            
            autorIdSelect.disabled = false;
            novoAutorNomeInput.disabled = true;
            novoAutorDescricaoTextarea.disabled = true;

            // Limpa os campos de novo autor para não enviar dados desnecessários
            novoAutorNomeInput.value = '';
            novoAutorDescricaoTextarea.value = '';
        } else { // tipo === 'novo'
            autorExistenteSection.style.display = 'none';
            novoAutorSection.style.display = 'block';
            
            autorIdSelect.disabled = true;
            novoAutorNomeInput.disabled = false;
            novoAutorDescricaoTextarea.disabled = false;

            // Limpa a seleção do dropdown para não enviar dados conflitantes
            autorIdSelect.value = '';
        }
    }

    // Adiciona o evento de 'change' para os radio buttons
    tipoAutorRadios.forEach(radio => {
        radio.addEventListener('change', (event) => {
            toggleAutorFields(event.target.value);
        });
    });

    // Define o estado inicial do formulário com base no modo (novo ou edição)
    const idInput = document.querySelector('input[name="id"]');
    if (idInput) {
        const isEdicao = idInput.value !== '';
        if (isEdicao) {
             // Em modo de edição, sempre começa com "Autor Existente" selecionado
            document.querySelector('input[name="tipoAutor"][value="existente"]').checked = true;
            toggleAutorFields('existente');
        } else {
            // Para um livro novo, também começa com a opção padrão "Autor Existente"
            toggleAutorFields('existente');
        }
    }
});