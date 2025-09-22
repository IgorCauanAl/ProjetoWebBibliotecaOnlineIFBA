package br.ifba.edu.BibliotecaOnline.model;

public enum GeneroEnum {
    // Adicionamos um nome "bonito" para cada constante
    FICCAO("Ficção"),
    TERROR("Terror"),
    COMEDIA("Comédia"),
    SUSPENSE("Suspense"),
    SUPERACAO("Superação"),
    AVENTURA("Aventura"),
    ANIME("Anime"),
    FANTASIA("Fantasia"),
    ACAO("Ação"),
    ROMANCE("Romance"),
    TECNICO("Técnico"),
    BIOGRAFIA("Biografia"),
    HISTORIA("História");

    // 1. Campo para armazenar o nome formatado
    private final String nomeFormatado;

    // 2. Construtor para associar a constante ao nome
    GeneroEnum(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    // 3. Método que o Thymeleaf vai usar para pegar o nome
    public String getNomeFormatado() {
        return nomeFormatado;
    }
}