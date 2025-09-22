const carousel = document.getElementById('bookCarousel');
const initialCarouselHTML = carousel.innerHTML;
const resultsDiv = document.getElementById('results');
const searchInput = document.getElementById('search');

// Cria o HTML do card
function createCardHtml(livro) {
    const capaHtml = livro.capaUrl
        ? `<img src="${livro.capaUrl}" alt="Capa"/>`
        : `<div class="no-cover">Sem capa</div>`;
    return `
        <div class="book-card" data-id="${livro.id}">
            <div class="cover">${capaHtml}</div>
            <div class="book-title">${escapeHtml(livro.nome || '')}</div>
            <div class="book-author">${escapeHtml(livro.autor || '')}</div>
            <div class="card-footer">
                <span class="meta">${livro.anoPublicacao || ''}</span>
            </div>
            <div class="card-actions">
                <a href="/livros/editar/${livro.id}" class="edit-btn">Editar</a>
                <a href="/livros/deletar/${livro.id}" class="delete-btn">Excluir</a>
            </div>
        </div>
    `;
}

// Cria item do autocomplete
function createSuggestionItem(livro) {
    const img = livro.capaUrl
        ? `<img src="${livro.capaUrl}" alt="Capa" />`
        : `<div style="width:40px;height:56px;background:#eee;border-radius:4px;"></div>`;
    return `<div class="result-item" data-id="${livro.id}">${img}<div>${escapeHtml(livro.nome || '')}</div></div>`;
}

// Faz a busca
function doSearch(query) {
    fetch('/livros/search?nome=' + encodeURIComponent(query))
        .then(resp => resp.json())
        .then(data => {
            // Sugestões
            resultsDiv.innerHTML = '';
            if (data.length > 0) {
                resultsDiv.style.display = 'block';
                data.forEach(l => resultsDiv.insertAdjacentHTML('beforeend', createSuggestionItem(l)));
            } else {
                resultsDiv.style.display = 'none';
            }

            carousel.innerHTML = '';
            if (data.length === 0) {
                carousel.innerHTML = initialCarouselHTML;
            } else {
                data.forEach(l => carousel.insertAdjacentHTML('beforeend', createCardHtml(l)));
            }
        })
        .catch(err => {
            console.error(err);
            resultsDiv.style.display = 'none';
        });
}


let debounceTimer;
searchInput.addEventListener('keyup', function () {
    const q = this.value.trim();
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
        if (q.length > 0) doSearch(q);
        else {
            resultsDiv.style.display = 'none';
            carousel.innerHTML = initialCarouselHTML;
        }
    }, 220);
});

// Fecha sugestões ao clicar fora
document.addEventListener('click', function (ev) {
    if (!ev.target.closest('#results') && !ev.target.closest('#search')) {
        resultsDiv.style.display = 'none';
    }
});

// Seleciona sugestão
resultsDiv.addEventListener('click', function (ev) {
    const item = ev.target.closest('.result-item');
    if (!item) return;
    searchInput.value = item.textContent.trim();
    doSearch(searchInput.value);
    resultsDiv.style.display = 'none';
});

// Escape HTML
function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>"']/g, function (m) {
        return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m];
    });
}
