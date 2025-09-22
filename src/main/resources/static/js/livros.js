let menuIcon = document.querySelector("#menu-icon");
let navbar = document.querySelector(".navbar");
let sections = document.querySelectorAll("section");
let navLinks = document.querySelectorAll("header nav a");

window.onscroll = () => {
  sections.forEach((sec) => {
    let top = window.scrollY;
    let offset = sec.offsetTop - 150;
    let height = sec.offsetHeight;
    let id = sec.getAttribute("id");

    if (top >= offset && top < offset + height) {
      navLinks.forEach((links) => {
        links.classList.remove("active");
        document
          .querySelector("header nav a[href*=" + id + "]")
          .classList.add("active");
      });
    }
  });
};

menuIcon.onclick = () => {
  menuIcon.classList.toggle("bx-x");
  navbar.classList.toggle("active");
};

//CRIAR LIVROS
document.addEventListener("DOMContentLoaded", () => {

  const livro = {
    capa: "../img/capa (6).png",
    titulo: "Está chovendo estrelas",
    sinopse:
      "Está chovendo estrelas é um livro que retrata a história de Laura, ela é uma garota autista que sonha em ser uma astronauta, estuda astronomia desde seus 8 anos, porém tudo muda quando sua mãe morre em um acidente trágico de carro e ela precisa encarar o luto enquanto estuda para o vestibular. Nessa história você vai ver uma garota aprendendo a lidar com uma dor inimaginável e tirando forças para alcançar seus sonhos.",
    autor: {
      nome: "Ana Soares",
      foto:"../img/autor-anasoares.png",
      descricao:
        "Ana sempre foi uma garota criativa e sonhadora, escreve livros desde seus 15 anos mas sempre teve dificuldades por conta da sua familia em publicar oficialmente seus livros.",
    },
    avaliacoes: [
      {
        estrelas: 5,
        autor: "Ana Souza",
        foto: "../img/avaliacoes2.png",
        texto: "Que livro FODA! Já quero ler novamente.",
      },
      {
        estrelas: 1,
        autor: "Clara Costa",
        foto: "../img/avaliacoes1.png",
        texto:
          "Já li melhores, não achei a escrita boa, além disso, o plot é fraco.",
      },
    ],
  };

  // --- FUNÇÃO PARA PREENCHER OS DADOS NA PÁGINA ---
  function carregarDadosDoLivro() {
    //livros
    document.getElementById("capa-livro").src = livro.capa;
    document.getElementById("titulo-livro").textContent = livro.titulo;
    document.getElementById("sinopse-livro").textContent = livro.sinopse;

    //autor
    document.getElementById("foto-autor").src = livro.autor.foto;
    document.getElementById("nome-autor").textContent = livro.autor.nome;
    document.getElementById("descricao-autor").textContent =
      livro.autor.descricao;

    //lista de avaliações
    const listaAvaliacoes = document.getElementById("lista-avaliacoes");
    listaAvaliacoes.innerHTML = "";

    livro.avaliacoes.forEach((avaliacao) => {
      const avaliacaoHTML = `
                <div class="avaliacao-card">
                    
                    <img src="${avaliacao.foto}" alt="Foto de ${
        avaliacao.autor
      }" class="avaliacao-foto">
                    <div class="avaliacao-conteudo">
                        <strong>${avaliacao.autor}</strong>
                        <div class="avaliacao-estrelas">
                        ${"★".repeat(avaliacao.estrelas)}${"☆".repeat(
        5 - avaliacao.estrelas
      )}
                    </div>
                        <p>"${avaliacao.texto}"</p>
                    </div>
                </div>
            `;
      listaAvaliacoes.innerHTML += avaliacaoHTML;
    });
  }
  carregarDadosDoLivro();
});
