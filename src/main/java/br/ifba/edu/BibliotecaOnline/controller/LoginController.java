package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.LoginRequest;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.excecao.LoginIncorretoException;
import br.ifba.edu.BibliotecaOnline.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Teste para o PostMan
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    //Chava o service de login
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    //Método que verificar se o login é sucedido ou não chamando o login service
    @PostMapping
    public ResponseEntity<LoginRequest> login(@RequestBody LoginRequest request){
        try{
            Usuario usuario = loginService.authenticate(request);

            LoginRequest response = new LoginRequest();
            response.setEmail(usuario.getEmail());
            return ResponseEntity.ok(response);
        }catch(LoginIncorretoException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }

}
