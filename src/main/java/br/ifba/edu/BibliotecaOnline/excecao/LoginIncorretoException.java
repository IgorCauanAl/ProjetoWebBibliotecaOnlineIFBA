package br.ifba.edu.BibliotecaOnline.excecao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginIncorretoException extends RuntimeException {

    public LoginIncorretoException(){
        super("Login ou senha incorreto!");
    }

    public LoginIncorretoException(String mensagem){
        super(mensagem);
    }

}
