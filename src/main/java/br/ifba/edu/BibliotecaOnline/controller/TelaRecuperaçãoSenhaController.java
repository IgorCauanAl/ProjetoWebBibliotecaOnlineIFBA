package br.ifba.edu.BibliotecaOnline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recuperar")
public class TelaRecuperaçãoSenhaController {

    @GetMapping
    public String retornarTela(){
        return "recuperar";
    }

}
