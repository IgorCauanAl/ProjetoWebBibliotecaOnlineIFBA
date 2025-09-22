package br.ifba.edu.BibliotecaOnline.excecao;

public class LivroDuplicadoException extends RuntimeException {
    public LivroDuplicadoException(String message){
        super(message);
    }

}