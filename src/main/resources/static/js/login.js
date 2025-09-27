//Seleciona os elementos do DOM
const loginForm = document.querySelector(".login-wrap");
const signupForm = document.querySelector(".signup-wrap");
const title = document.querySelector("title");
const signupToggleBtn = document.querySelector("#toggle-signup");
const loginToggleBtn = document.querySelector("#toggle-login");
const signupFormElement = document.querySelector(".signup-wrap form");

// Função para alternar para a tela de Cadastro
signupToggleBtn.onclick = () => {
    loginForm.classList.remove("active");
    signupForm.classList.add("active");
    title.textContent = "Cadastrar";
};

// Função para alternar para a tela de Login
loginToggleBtn.onclick = () => {
    signupForm.classList.remove("active");
    loginForm.classList.add("active");
    title.textContent = "Entrar";
};

// Event listener para o formulário de cadastro com os alertas modificados
signupFormElement.addEventListener("submit", async (e) => {
    e.preventDefault();

    const nome = document.querySelector("#nome-cadastro").value;
    const email = document.querySelector("#email-cadastro").value;
    const senha = document.querySelector("#password-cadastro").value;

    try {
        const response = await fetch("/api/cadastro", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, email, senha }),
        });

        if (!response.ok) {
            let errorMessage = "Ocorreu um erro desconhecido. Tente novamente.";
            const contentType = response.headers.get("content-type");

            if (contentType && contentType.includes("application/json")) {
                const errorData = await response.json();
                errorMessage = errorData.message || JSON.stringify(errorData);
            } else {
                errorMessage = await response.text();
            }
            throw new Error(errorMessage);
        }

        const data = await response.json();

        // Alerta de sucesso COM a classe personalizada
        Swal.fire({
            title: "Cadastro realizado!",
            text: "Você já pode fazer o login com suas credenciais.",
            icon: "success",
            confirmButtonText: "login",
            customClass: {
                confirmButton: 'meu-botao-confirmar'
            },
            buttonsStyling: false
            // ------------------------
        }).then(() => {
            signupFormElement.reset();
            loginToggleBtn.click();
        });

    } catch (err) {
        Swal.fire({
            title: "Erro no Cadastro",
            text: err.message,
            icon: "error",
            confirmButtonText: "Entendi", 
            customClass: {
                confirmButton: 'meu-botao-confirmar'
            },
            buttonsStyling: false
        });
    }
});