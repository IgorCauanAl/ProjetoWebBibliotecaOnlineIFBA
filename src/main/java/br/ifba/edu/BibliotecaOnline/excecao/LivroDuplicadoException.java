package br.ifba.edu.BibliotecaOnline.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LivroDuplicadoException extends RuntimeException {
    public LivroDuplicadoException(String message){
        super(message);
    }
}