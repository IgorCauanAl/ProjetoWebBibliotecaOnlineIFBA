document.addEventListener("DOMContentLoaded", () => {
    const tituloEtapa = document.getElementById("titulo-etapa");
    const etapaEmail = document.getElementById("recuperarForm");
    const etapaCodigo = document.getElementById("etapa-codigo");
    const etapaNovaSenha = document.getElementById("etapa-nova-senha");

    let codigoDigitado = "";

    // Enviar o email
    etapaEmail.addEventListener("submit", async (event) => {
        event.preventDefault();
        const email = document.getElementById('email').value;

        const resposta = await fetch('/api/recuperar/enviarCodigo', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email})
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            Swal.fire({
                title: "Erro",
                text: mensagem,
                icon: "error",
                customClass: {
                    popup: 'meu-popup-estilizado',
                    confirmButton: 'meu-botao-confirmar'
                },
                buttonsStyling: false,
            });
            return;
        }

        Swal.fire({
            title: "E-mail enviado!",
            text: mensagem,
            icon: "success",
            customClass: {
                popup: 'meu-popup-estilizado',
                confirmButton: 'meu-botao-confirmar'
            },
            buttonsStyling: false,
        });

        tituloEtapa.innerText = "Digite o Código";
        etapaEmail.style.display = "none";
        etapaCodigo.style.display = "block";
    });

    // Receber o codigo
    etapaCodigo.addEventListener("submit", async (event) => {
        event.preventDefault();
        const codigoInput = document.getElementById("codigo").value;

        const resposta = await fetch('/api/recuperar/receberCodigo', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({codigo: codigoInput})
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            Swal.fire({
                title: "Código Incorreto",
                text: mensagem,
                icon: "error",
                customClass: {
                    popup: 'meu-popup-estilizado',
                    confirmButton: 'meu-botao-confirmar'
                },
                buttonsStyling: false,
            });
            return;
        }

        codigoDigitado = codigoInput;
        tituloEtapa.innerText = "Crie uma Nova Senha";
        etapaCodigo.style.display = "none";
        etapaNovaSenha.style.display = "block";
    });

    // Trocar a senha
    etapaNovaSenha.addEventListener("submit", async (event) => {
        event.preventDefault();
        const novaSenha = document.getElementById("nova-senha").value;
        const confirmarSenha = document.getElementById("confirmar-senha").value;

        if (novaSenha !== confirmarSenha) {
            Swal.fire({
                title: "As senhas não coincidem!",
                text: "Por favor, digite a mesma senha nos dois campos.",
                icon: "warning",
                customClass: {
                    popup: 'meu-popup-estilizado',
                    confirmButton: 'meu-botao-confirmar'
                }, buttonsStyling: false,
            });
            return;
        }

        const resposta = await fetch('/api/recuperar/trocarSenha', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({codigo: codigoDigitado, novaSenha: novaSenha})
        });

        const mensagem = await resposta.text();

        if (!resposta.ok) {
            Swal.fire({
                title: "Erro",
                text: mensagem,
                icon: "error",
                customClass: {
                    popup: 'meu-popup-estilizado',
                    confirmButton: 'meu-botao-confirmar'
                }, buttonsStyling: false,
            });
            return;
        }

        Swal.fire({
            title: "Senha alterada com sucesso!",
            icon: "success",
            customClass: {
                popup: 'meu-popup-estilizado',
                confirmButton: 'meu-botao-confirmar'
            }, buttonsStyling: false,
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = "/telalogin";
            }
        });
    });
});
