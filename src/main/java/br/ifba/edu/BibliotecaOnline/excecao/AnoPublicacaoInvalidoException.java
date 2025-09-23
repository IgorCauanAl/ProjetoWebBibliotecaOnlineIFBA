package br.ifba.edu.BibliotecaOnline.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnoPublicacaoInvalidoException extends RuntimeException {
    public AnoPublicacaoInvalidoException(String message){
        super(message);
    }
    
}
