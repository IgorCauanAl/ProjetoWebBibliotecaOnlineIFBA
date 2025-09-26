package br.ifba.edu.BibliotecaOnline.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class TelaDetalhesController {

    @GetMapping
    public String retornarTela(){
        return "livros";
    }
}
