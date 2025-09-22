document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("home-search-input");
    const resultsContainer = document.getElementById("search-results-container");

    
    if (!searchInput || !resultsContainer) {
        return;
    }

    let debounceTimer; 

    /**
     * Função que recebe a lista de livros (em formato JSON) e a desenha na tela.
     * @param {Array} books - A lista de livros retornada pela API.
     */
    const renderSuggestions = (books) => {
        resultsContainer.innerHTML = ''; // Limpa os resultados anteriores

        // Se a lista de livros estiver vazia, mostra uma mensagem.
        if (books.length === 0) {
            resultsContainer.innerHTML = '<div class="no-results-suggestion">Nenhum resultado encontrado.</div>';
            return;
        }

        // Cria o HTML para cada livro na lista
        const booksHtml = books.map(livro => `
            <a href="#" class="result-item">
                <img src="${livro.capaUrl || '/img/placeholder.png'}" alt="Capa de ${livro.nome}" />
                <div class="result-item-info">
                    <span class="title">${livro.nome}</span>
                    <span class="author">${livro.autor}</span>
                </div>
            </a>
        `).join('');

        resultsContainer.innerHTML = booksHtml;
    };
    
    /**
     * Função que faz a chamada para a API no backend.
     * @param {string} query - O texto que o usuário digitou.
     */
    const performSearch = async (query) => {
        if (query.length === 0) {
            resultsContainer.style.display = "none";
            return;
        }

        try {
            const response = await fetch(`/api/livros/search?q=${encodeURIComponent(query)}`);
            if (!response.ok) {
                throw new Error('Erro ao buscar os livros.');
            }
            
            const books = await response.json();
            
            resultsContainer.style.display = "block"; 
            renderSuggestions(books);

        } catch (error) {
            console.error("Falha na busca:", error);
            resultsContainer.style.display = "block";
            resultsContainer.innerHTML = '<div class="no-results-suggestion">Erro ao realizar a busca.</div>';
        }
    };

    searchInput.addEventListener("input", (event) => {
        const query = event.target.value.trim();
        
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            performSearch(query);
        }, 150); 
    });

    document.addEventListener("click", (event) => {
        if (!event.target.closest('.search-container')) {
            resultsContainer.style.display = "none";
        }
    });
});