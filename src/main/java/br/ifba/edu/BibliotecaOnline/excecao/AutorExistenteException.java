package br.ifba.edu.BibliotecaOnline.excecao;

public class AutorExistenteException extends RuntimeException {
    public AutorExistenteException(String message) {
        super(message);
    }
}