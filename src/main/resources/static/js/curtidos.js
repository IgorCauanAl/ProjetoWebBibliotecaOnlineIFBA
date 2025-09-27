const csrfToken = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

/**
 * Função para adicionar o listener de curtir/descurtir a um botão
 */
function attachLikeListener(button, livroId) {
    button.addEventListener("click", async () => {
        const isLiked = button.classList.contains("active");
        const method = isLiked ? "DELETE" : "POST";

        try {
            const response = await fetch(`/api/liked-books/${livroId}`, {
                method: method,
                headers: { [csrfHeader]: csrfToken }
            });
            if (!response.ok) throw new Error("Erro ao atualizar curtidos");

            // Atualiza o visual do botão
            button.classList.toggle("active");

            // Se for página de curtidos e estiver descurtindo, remove o card
            if (method === "DELETE") {
                const card = button.closest(".book-card");
                if (card) card.remove();
            }

        } catch (error) {
            console.error("Erro ao curtir/descurtir livro:", error);
        }
    });
}


/**
 * Inicializa os botões já existentes na página (home, categorias, etc.)
 */
function initExistingButtons() {
    document.querySelectorAll(".like-btn, .heart-btn").forEach(btn => {
        const livroId = btn.dataset.livroId;
        if (livroId) attachLikeListener(btn, livroId);
    });
}

/**
 * Carrega os livros curtidos no grid da página de curtidos
 */
async function carregarCurtidos() {
    try {
        const response = await fetch("/api/liked-books");
        if (!response.ok) throw new Error("Erro ao buscar livros curtidos");
        const livros = await response.json();

        const container = document.getElementById("liked-grid");
        if (!container) return;

        container.innerHTML = ""; // limpa antes de preencher

        livros.forEach(livro => {
            const card = document.createElement("div");
            card.classList.add("book-card");
            card.dataset.book = livro.id;
            card.innerHTML = `
                <div class="book-cover">
                    <img src="${livro.capaUrl || '/img/capas/placeholder.jpg'}" alt="Capa do livro ${livro.nome}" />
                    <button class="heart-btn active" data-livro-id="${livro.id}" title="Curtido">
                        <i class="bx bxs-heart"></i>
                    </button>
                </div>
                <h3 class="book-title">${livro.nome}</h3>
                <p class="book-author">${livro.autorNome}</p>
            `;
            container.appendChild(card);

            // adiciona listener ao botão criado dinamicamente
            const heartBtn = card.querySelector(".heart-btn");
            attachLikeListener(heartBtn, livro.id);
        });

    } catch (error) {
        console.error("Erro ao carregar livros curtidos:", error);
    }
}

// Inicialização quando o DOM estiver pronto
window.addEventListener("DOMContentLoaded", () => {
    initExistingButtons();
    carregarCurtidos();
});
