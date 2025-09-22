document.addEventListener("DOMContentLoaded", () => {
  const COLOR_PRIMARY = "#2f3559"; // azul
  const COLOR_DANGER = "#d94a4a"; // vermelho

  // Função helper para pop-ups de OK
  const swalOK = (opts = {}) =>
    Swal.fire({
      buttonsStyling: true,
      confirmButtonText: "OK",
      confirmButtonColor: COLOR_PRIMARY,
      ...opts,
    });

  // Função helper para pop-ups de confirmação de exclusão
  const swalConfirmDelete = (opts = {}) =>
    Swal.fire({
      icon: "warning",
      showCancelButton: true,
      reverseButtons: true,
      confirmButtonText: "Sim, deletar!",
      cancelButtonText: "Cancelar",
      confirmButtonColor: COLOR_DANGER,
      cancelButtonColor: COLOR_PRIMARY,
      buttonsStyling: true,
      ...opts,
    });

  // Lógica para Mostrar/ocultar senha
  document.querySelectorAll(".toggle").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = btn.getAttribute("data-target");
      const input = document.getElementById(id);
      if (!input) return;

      const isPass = input.type === "password";
      input.type = isPass ? "text" : "password";
      btn.innerHTML = isPass
        ? '<i class="bx bx-show"></i>'
        : '<i class="bx bx-hide"></i>';
    });
  });

  // Lógica para o formulário de Alterar Senha
  const formSenha = document.querySelector('form[action*="mudar-senha"]');
  if (formSenha) {
    formSenha.addEventListener("submit", async (e) => {
      e.preventDefault();

      const senhaAtual = document.getElementById("senha-atual");
      const novaSenha = document.getElementById("nova-senha");
      const confirma = document.getElementById("confirma-senha");

      if (!senhaAtual.value || !novaSenha.value || !confirma.value) {
        await swalOK({
          icon: "warning",
          title: "Preencha todos os campos",
          text: "Para alterar a senha, complete os três campos.",
        });
        return;
      }

      if (novaSenha.value !== confirma.value) {
        await swalOK({
          icon: "error",
          title: "As senhas não coincidem",
          text: "Verifique a confirmação da nova senha.",
        });
        return;
      }
      
      // Se tudo estiver certo, o formulário pode ser enviado
      // Aqui você faria a chamada fetch para o backend ou simplesmente enviaria o form
       await swalOK({
         icon: "success",
         title: "Enviado!",
         text: "Sua solicitação de mudança de senha foi enviada.",
       });
       formSenha.reset();
       // formSenha.submit(); // Descomente esta linha para enviar o formulário
    });
  }

  // Lógica para Apagar a conta
  const chkApagar = document.getElementById("confirmar-exclusao");
  const apagarBtn = document.getElementById("apagar-btn");
  if (chkApagar && apagarBtn) {
    // Habilita/desabilita o botão com base no checkbox
    chkApagar.addEventListener("change", () => {
        apagarBtn.disabled = !chkApagar.checked;
    });
    
    apagarBtn.addEventListener("click", async (e) => {
      e.preventDefault();
      
      const { isConfirmed } = await swalConfirmDelete({
        title: "Tem certeza?",
        text: "Essa ação não poderá ser revertida!",
      });

      if (isConfirmed) {
        // Aqui você enviaria o formulário de exclusão para o backend
        await swalOK({
          icon: "success",
          title: "Apagada!",
          text: "Sua conta foi excluída com sucesso.",
        });
        // Ex: window.location.href = "/login";
      }
    });
  }

  // Lógica para abrir/fechar pop-ups (modals)
  document.querySelectorAll("[data-open]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const targetId = btn.getAttribute("data-open");
      const el = document.getElementById(targetId);
      if (el) el.classList.add("active");
    });
  });

  document.querySelectorAll("[data-close]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const targetId = btn.getAttribute("data-close");
      const el = document.getElementById(targetId);
      if (el) el.classList.remove("active");
    });
  });

  // Lógica para o formulário de Editar Detalhes
  const formEditar = document.getElementById("form-editar-detalhes");
  if (formEditar) {
    formEditar.addEventListener("submit", async (e) => {
      e.preventDefault();
      // Lógica para enviar os novos dados para o backend e atualizar a UI
      await swalOK({
        icon: "success",
        title: "Informações atualizadas!",
        text: "Seus dados foram salvos com sucesso.",
      });
      // Atualizaria os campos na tela com os novos valores
      const popup = document.getElementById("editar-detalhes");
      if (popup) popup.classList.remove("active");
    });
  }

  // Lógica para o upload da foto de perfil
  const fileInput = document.getElementById("upload-foto");
  const profileAvatarImg = document.querySelector(".avatar-img");

  if (fileInput && profileAvatarImg) {
    fileInput.addEventListener("change", () => {
      const file = fileInput.files && fileInput.files[0];
      if (!file) return;

      const reader = new FileReader();
      reader.onload = async (e) => {
        const src = e.target.result;
        profileAvatarImg.src = src;

        // Aqui você enviaria o 'file' para o backend para salvar
        await swalOK({
          icon: "success",
          title: "Foto atualizada!",
          text: "Sua imagem de perfil foi alterada com sucesso.",
        });
      };
      reader.readAsDataURL(file);
    });
  }
});
