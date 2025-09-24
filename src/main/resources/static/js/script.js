// Seletores de elementos que podem estar no escopo global
const menuIcon = document.querySelector("#menu-icon");
const navbar = document.querySelector(".navbar");
const sections = document.querySelectorAll("section");
const navLinks = document.querySelectorAll("header nav a");

// --- LÓGICA DO MENU HAMBÚRGUER (MOBILE) ---
if (menuIcon) {
    menuIcon.onclick = () => {
        menuIcon.classList.toggle("bx-x");
        navbar.classList.toggle("active");
    };
}

// --- LÓGICA DE SCROLL OTIMIZADA COM DEBOUNCE ---
let scrollTimer;

// A lógica que estava dentro do onscroll agora fica em uma função separada
const handleScroll = () => {
    if (sections.length > 0 && navLinks.length > 0) {
        sections.forEach((sec) => {
            const top = window.scrollY;
            const offset = sec.offsetTop - 150;
            const height = sec.offsetHeight;
            const id = sec.getAttribute("id");

            if (top >= offset && top < offset + height) {
                navLinks.forEach((link) => {
                    link.classList.remove("active");
                });
                const activeLink = document.querySelector("header nav a[href*=" + id + "]");
                if (activeLink) {
                    activeLink.classList.add("active");
                }
            }
        });
    }
};

window.onscroll = () => {
    // Usa o debounce: cancela o timer anterior e agenda um novo
    clearTimeout(scrollTimer);
    scrollTimer = setTimeout(handleScroll, 100); // Executa a função 100ms após o usuário parar de rolar
};


// --- LÓGICA EXECUTADA APÓS O CARREGAMENTO DA PÁGINA ---
document.addEventListener("DOMContentLoaded", () => {
 
  // --- LÓGICA DO POPUP DE ENVIAR LIVRO ---
  const openPopupBtn = document.getElementById("open-popup-btn");
  const closePopupBtn = document.getElementById("close-popup-btn");
  const popupOverlay = document.getElementById("popup-overlay");
  const livroForm = document.getElementById("livro-form");

  if (openPopupBtn && closePopupBtn && popupOverlay && livroForm) {
    const openPopup = () => popupOverlay.classList.add("active");
    const closePopup = () => popupOverlay.classList.remove("active");

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
  }

 
  // --- LÓGICA PARA INICIALIZAR OS CARROSSÉIS (DINÂMICOS) ---
  function initializeCarousel(slider) {
    const list = slider.querySelector(".list");
    const items = slider.querySelectorAll(".list .item");
    const nextBtn = slider.querySelector(".next-btn");
    const prevBtn = slider.querySelector(".prev-btn");

    if (!list || items.length === 0 || !nextBtn || !prevBtn) {
      return; 
    }

    let active = 0;
    let autoPlayInterval;

    function reloadSlider() {
      if (items.length === 0) return;
      const cardWidth = items[0].offsetWidth;
      const gap = parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
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
        const gap = parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
        const step = cardWidth + gap;
        const itemsPerScreen = Math.floor(slider.offsetWidth / step);
        const scrollLimit = Math.max(0, items.length - itemsPerScreen);

        active = (active < scrollLimit) ? active + 1 : 0;
        reloadSlider();
    };

    prevBtn.onclick = function () {
        const cardWidth = items[0].offsetWidth;
        const gap = parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
        const step = cardWidth + gap;
        const itemsPerScreen = Math.floor(slider.offsetWidth / step);
        const scrollLimit = Math.max(0, items.length - itemsPerScreen);

        active = (active > 0) ? active - 1 : scrollLimit;
        reloadSlider();
    };

    reloadSlider();
    window.addEventListener("resize", reloadSlider);
  }

  const allSliders = document.querySelectorAll(".slider");
  allSliders.forEach((slider) => {
    initializeCarousel(slider);
  });

  // --- LÓGICA DO BOTÃO DE CURTIR ---
  document.body.addEventListener('click', function(event) {
      const likeButton = event.target.closest('.like-btn');
      if (likeButton) {
          likeButton.classList.toggle("active");
          likeButton.classList.add("animating");
          setTimeout(() => {
              likeButton.classList.remove("animating");
          }, 300);
      }
  });

});