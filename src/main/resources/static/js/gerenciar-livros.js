document.addEventListener("DOMContentLoaded", () => {
  const bookList = document.getElementById("book-list");

  if (bookList) {
    bookList.addEventListener("click", (event) => {
      const deleteButton = event.target.closest(".delete-btn");
      if (!deleteButton) return;

      // Previne que o link seja seguido imediatamente
      event.preventDefault();
      const deleteUrl = deleteButton.href;

      // Mostra um pop-up de confirmação
      Swal.fire({
        title: "Você tem certeza?",
        text: "Esta ação não poderá ser revertida!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sim, deletar!",
        cancelButtonText: "Cancelar",
        reverseButtons: true,
        customClass: {
          confirmButton: "swal-btn swal-btn--danger",
          cancelButton: "swal-btn swal-btn--primary",
        },
      }).then((result) => {
        // Se o usuário clicou em "Sim, deletar!"
        if (result.isConfirmed) {
          window.location.href = deleteUrl;
        }
      });
    });
  }

  const searchInput = document.getElementById("table-search-input");

  if (searchInput) {
    searchInput.addEventListener("input", (event) => {
      const term = event.target.value.trim().toLowerCase();
      const rows = document.querySelectorAll("#book-list tr");

      rows.forEach((row) => {
        const cells = row.cells;
        // Pula a linha de "Nenhum livro encontrado"
        if (!cells || cells.length < 3) return; 

        const title = cells[1].textContent.toLowerCase();
        const author = cells[2].textContent.toLowerCase();

        // Mostra a linha se o termo de busca for encontrado no título ou autor
        const isMatch = title.includes(term) || author.includes(term);
        row.style.display = isMatch ? "" : "none";
      });
    });
  }
});