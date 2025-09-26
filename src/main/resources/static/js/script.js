// let menuIcon = document.querySelector("#menu-icon");
// let navbar = document.querySelector(".navbar");
// let sections = document.querySelectorAll("section");
// let navLinks = document.querySelectorAll("header nav a");

// window.onscroll = () => {
//   sections.forEach((sec) => {
//     let top = window.scrollY;
//     let offset = sec.offsetTop - 150;
//     let height = sec.offsetHeight;
//     let id = sec.getAttribute("id");

//     if (top >= offset && top < offset + height) {
//       navLinks.forEach((links) => {
//         links.classList.remove("active");
//         document
//           .querySelector("header nav a[href*=" + id + "]")
//           .classList.add("active");
//       });
//     }
//   });
// };

// menuIcon.onclick = () => {
//   menuIcon.classList.toggle("bx-x");
//   navbar.classList.toggle("active");
// };

document.addEventListener("DOMContentLoaded", () => {
  const openPopupBtn = document.getElementById("open-popup-btn");
  const closePopupBtn = document.getElementById("close-popup-btn");
  const popupOverlay = document.getElementById("popup-overlay");
  const livroForm = document.getElementById("form-enviar-livro");

  const openPopup = () => {
    popupOverlay.classList.add("active");
  };

  const closePopup = () => {
    popupOverlay.classList.remove("active");
  };

  openPopupBtn.addEventListener("click", (event) => {
    event.preventDefault();
    openPopup();
  });

  closePopupBtn.addEventListener("click", closePopup);

  popupOverlay.addEventListener("click", (event) => {
    if (event.target === popupOverlay) {
      closePopup();
    }
  });
  livroForm.addEventListener("submit", (event) => {
    event.preventDefault();

    closePopup();

    livroForm.reset();

    Swal.fire({
      title: "Enviado com sucesso!",
      text: "Agradecemos sua contribuição. Nossa equipe irá avaliar.",
      icon: "success",
    });
  });
});

document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM carregado. Iniciando script dinâmico do carrossel.");

  // DADOS DOS LIVROS (ADICIONAR/REMOVER LIVROS)
  // ESTA SEÇÃO FOI REMOVIDA PARA QUE OS DADOS SEJAM PUXADOS DE OUTRA FONTE (EX: BANCO DE DADOS)

  //FUNÇÃO QUE CRIA O HTML DE CADA CARD
  function gerarCardHTML(livro) {
    return `
        <div class="item">
          <div class="card">
            <div class="card-image-container">
              <img src="${livro.imagem}" alt="${livro.alt}" />
              <button class="like-btn" aria-label="Curtir">
                <svg width="24" height="24" viewBox="0 0 24 24">
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                </svg>
              </button>
            </div>
            <div class="card-content">
              <p class="card-title">${livro.titulo}</p>
              <p class="card-author">${livro.autor}</p>
              <button class="details-btn" onclick="window.location.href='livros.html'">Detalhes</button>
            </div>
          </div>
        </div>
      `;
  }

  //FUNÇÃO QUE PREENCHE O CARROSSEL E INICIALIZA TUDO
  function popularCarrossel(seletorCarrossel, listaDeLivros) {
    const carrosselList = document.querySelector(`${seletorCarrossel} .list`);
    if (carrosselList) {
      // Gera o HTML para todos os livros e junta tudo em uma única string
      carrosselList.innerHTML = listaDeLivros.map(gerarCardHTML).join("");
    } else {
      console.error(
        `Elemento .list não encontrado para o seletor ${seletorCarrossel}`
      );
    }
  }

  // Preenche cada carrossel com seus respectivos livros
  // As chamadas para popularCarrossel foram removidas, pois os dados estáticos não existem mais.

  function initializeCarousel(slider) {
    const list = slider.querySelector(".list");
    const items = slider.querySelectorAll(".list .item");
    const nextBtn = slider.querySelector(".next-btn");
    const prevBtn = slider.querySelector(".prev-btn");

    if (!list || items.length === 0 || !nextBtn || !prevBtn) {
      console.error(
        "ERRO: Elementos essenciais do carrossel não foram encontrados dentro de:",
        slider
      );
      return;
    }

    let active = 0;
    let autoPlayInterval;

    function reloadSlider() {
      if (items.length === 0) return;
      const cardWidth = items[0].offsetWidth;
      const gap =
        parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
      const step = cardWidth + gap;
      const itemsPerScreen = Math.floor(slider.offsetWidth / step);
      const scrollLimit = Math.max(0, items.length - itemsPerScreen);

      if (active > scrollLimit) active = scrollLimit;
      if (active < 0) active = 0;

      let scrollDistance = active * step;
      list.style.transform = `translateX(-${scrollDistance}px)`;

      clearInterval(autoPlayInterval);
      autoPlayInterval = setInterval(() => nextBtn.click(), 5000);
    }

    nextBtn.onclick = function () {
      const cardWidth = items[0].offsetWidth;
      const gap =
        parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
      const step = cardWidth + gap;
      const itemsPerScreen = Math.floor(slider.offsetWidth / step);
      const scrollLimit = Math.max(0, items.length - itemsPerScreen);

      if (active < scrollLimit) {
        active++;
      } else {
        active = 0;
      }
      reloadSlider();
    };

    prevBtn.onclick = function () {
      const cardWidth = items[0].offsetWidth;
      const gap =
        parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
      const step = cardWidth + gap;
      const itemsPerScreen = Math.floor(slider.offsetWidth / step);
      const scrollLimit = Math.max(0, items.length - itemsPerScreen);

      if (active > 0) {
        active--;
      } else {
        active = scrollLimit;
      }
      reloadSlider();
    };

    reloadSlider();
    window.addEventListener("resize", reloadSlider);
  }

  const allSliders = document.querySelectorAll(".slider");
  allSliders.forEach((slider) => {
    initializeCarousel(slider);
  });

  const likeButtons = document.querySelectorAll(".like-btn");
  likeButtons.forEach((button) => {
    button.addEventListener("click", function () {
      this.classList.toggle("active");
      this.classList.add("animating");
      setTimeout(() => {
        this.classList.remove("animating");
      }, 300);
    });
  });
});