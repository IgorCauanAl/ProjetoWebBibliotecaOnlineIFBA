document.addEventListener("DOMContentLoaded", () => {
  const userList = document.querySelector(".user-list");

  if (userList) {
    userList.addEventListener("click", (event) => {
      const deleteButton = event.target.closest(
        ".delete-form button[type='submit']"
      );
      if (!deleteButton) return;

      const form = deleteButton.closest(".delete-form");
      event.preventDefault();

      Swal.fire({
        title: "Você tem certeza?",
        text: "O usuário será permanentemente excluído. Esta ação não poderá ser revertida!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sim, deletar!",
        cancelButtonText: "Cancelar",
        reverseButtons: true,
        buttonsStyling: false,
        customClass: {
          confirmButton: "swal-btn swal-btn--danger",
          cancelButton: "swal-btn swal-btn--primary",
        },
      }).then((result) => {
        if (result.isConfirmed) {
          form.submit();
        }
      });
    });
  }
});
