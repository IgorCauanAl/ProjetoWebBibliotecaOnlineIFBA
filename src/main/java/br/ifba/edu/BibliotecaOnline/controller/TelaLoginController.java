package br.ifba.edu.BibliotecaOnline.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class TelaLoginController {

    @GetMapping
    public String retornarTela(){
        return "login";
    }
}
