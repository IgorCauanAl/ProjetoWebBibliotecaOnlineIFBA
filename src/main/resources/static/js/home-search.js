document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("home-search-input");
    const resultsContainer = document.getElementById("search-results-container");

    if (!searchInput || !resultsContainer) {
        return;
    }

    let debounceTimer;

    const renderSuggestions = (books) => {
        resultsContainer.innerHTML = ''; 

        if (books.length === 0) {
            resultsContainer.innerHTML = '<div class="no-results-suggestion">Nenhum resultado encontrado.</div>';
            return;
        }

        const booksHtml = books.map(livro => `
            <a href="/livros/${livro.id}" class="result-item">
                <img src="${livro.capaUrl || '/img/placeholder.png'}" alt="Capa de ${livro.nome}" />
                <div class="result-item-info">
                    <span class="title">${livro.nome}</span>
                    <span class="author">${livro.autorNome}</span>
                </div>
            </a>
        `).join('');

        resultsContainer.innerHTML = booksHtml;
    };
    
    /**
     * Função que faz a chamada para a API no backend para buscar as sugestões.
     * @param {string} query - O texto que o usuário digitou.
     */
    const performSearch = async (query) => {
        if (query.length === 0) {
            resultsContainer.style.display = "none";
            return;
        }

        try {
            // AQUI FAZ A CHAMADA PARA A API (endpoint real-time)
            const response = await fetch(`/api/livros/search?q=${encodeURIComponent(query)}`);
            if (!response.ok) {
                throw new Error('Erro ao buscar os livros.');
            }
            
            const pageData = await response.json(); 
            const books = pageData.content;
            
            resultsContainer.style.display = "block"; 
            renderSuggestions(books);

        } catch (error) {
            console.error("Falha na busca:", error);
            resultsContainer.style.display = "block";
            resultsContainer.innerHTML = '<div class="no-results-suggestion">Erro ao realizar a busca.</div>';
        }
    };

    // Listener para o evento 'input' (digitar)
    searchInput.addEventListener("input", (event) => {
        const query = event.target.value.trim();
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            performSearch(query);
        }, 150); 
    });

    // Listener para esconder as sugestões se o usuário clicar fora
    document.addEventListener("click", (event) => {
        if (event.isTrusted && !event.target.closest('.search-container')) {
            resultsContainer.style.display = "none";
        }
    });

    // --- LÓGICA 2: REDIRECIONAR AO PRESSIONAR ENTER ---

    // Listener para o evento 'keypress' (pressionar Enter)
    searchInput.addEventListener("keypress", (event) => {
        if (event.key === "Enter") {
            const query = searchInput.value.trim();
            if (query.length > 0) {
                // AQUI FAZ O REDIRECIONAMENTO para a página principal com a busca
                window.location.href = `/home?q=${encodeURIComponent(query)}`;
            }
        }
    });
});