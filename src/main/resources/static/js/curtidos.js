
document.addEventListener("DOMContentLoaded", () => {

    // Pega os tokens CSRF das meta tags no HTML para segurança nas requisições.
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    /**
     * Adiciona um "ouvinte" de clique a um botão de curtir.
     * @param {HTMLElement} button - O elemento do botão.
     */
    function attachLikeListener(button) {
        const livroId = button.dataset.livroId;
        if (!livroId) return; 

        button.addEventListener("click", async () => {
            const isLiked = button.classList.contains("active");
            
            const method = isLiked ? "DELETE" : "POST";

            try {
                
                const response = await fetch(`/api/liked-books/${livroId}`, {
                    method: method,
                    headers: {
                        [csrfHeader]: csrfToken
                    }
                });

                if (!response.ok) {
                    throw new Error("A resposta da API não foi bem-sucedida.");
                }

                
                button.classList.toggle("active");

            
                const isOnCurtidosPage = document.body.contains(document.getElementById('liked-grid'));
                
                if (method === "DELETE" && isOnCurtidosPage) {
                    const card = button.closest(".book-card");
                    if (card) {
                        // Adiciona uma animação de fade-out antes de remover
                        card.style.transition = 'opacity 0.3s ease-out';
                        card.style.opacity = '0';
                        setTimeout(() => card.remove(), 300);
                    }
                }

            } catch (error) {
                console.error("Erro ao curtir/descurtir livro:", error);
                // Opcional: mostrar uma mensagem de erro para o usuário.
            }
        });
    }

   
    function initExistingButtons() {
        document.querySelectorAll(".like-btn, .heart-btn").forEach(button => {
            attachLikeListener(button);
        });
    }

    
    initExistingButtons();
});