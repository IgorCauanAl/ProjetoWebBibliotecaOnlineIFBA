
const deleteButton = document.getElementById('btn-confirmar-delecao');

if (deleteButton) {
    const deleteForm = document.getElementById('form-deletar-autor');

    deleteButton.addEventListener('click', function(event) {
        event.preventDefault(); 

        Swal.fire({
            title: 'Você tem certeza?',
            text: "Isso deletará o autor e TODOS os seus livros permanentemente. Esta ação não pode ser desfeita!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sim, deletar!',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteForm.submit();
            }
        });
    });
}