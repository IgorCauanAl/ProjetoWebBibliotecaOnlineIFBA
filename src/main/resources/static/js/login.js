// Seleciona os elementos do DOM
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

signupFormElement.addEventListener("submit", async (e) => {
    e.preventDefault(); 

    // Pega os valores dos campos de input
    const nome = document.querySelector("#nome-cadastro").value;
    const email = document.querySelector("#email-cadastro").value;
    const senha = document.querySelector("#password-cadastro").value;

    try {
        const response = await fetch("/api/cadastro", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, email, senha }),
        });

        if (response.status === 409) {
            alert("Este e-mail já está cadastrado!");
            return;
        }

        if (!response.ok) {
            throw new Error("Ocorreu um erro no cadastro. Tente novamente.");
        }
        
        // Se o cadastro foi bem-sucedido
        alert("Cadastro realizado com sucesso! Faça o login para continuar.");
        
        loginToggleBtn.click();
        
    } catch (err) {
        alert(err.message);
    }
});
