package br.ifba.edu.BibliotecaOnline.excecao;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String message) {
        super(message);
    }

}
