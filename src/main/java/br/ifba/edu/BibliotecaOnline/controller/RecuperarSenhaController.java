package br.ifba.edu.BibliotecaOnline.controller;


import br.ifba.edu.BibliotecaOnline.DTO.CodigoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.EmailRecuperacaoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.SenhaNovaDTO;
import br.ifba.edu.BibliotecaOnline.service.RecuperarSenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recuperar")
public class RecuperarSenhaController {


    @Autowired
    private RecuperarSenhaService recuperarSenhaService;

    //EndPoint para enviar o código
    @PostMapping("/enviarCodigo")
    private ResponseEntity<String> enviarCodigo(@RequestBody EmailRecuperacaoDTO emailRecuperacaoDTO){

        try{
            recuperarSenhaService.enviarCodigo(emailRecuperacaoDTO);
            return ResponseEntity.ok("Código enviado!");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body("Código não foi enviado com sucesso!");
        }

    }

    @PostMapping("/receberCodigo")
    private ResponseEntity<String> receberCodigo(@RequestBody CodigoDTO codigoDto){
        boolean valido = recuperarSenhaService.receberCodigo(codigoDto);

        if(valido){
            return ResponseEntity.ok("Código validado com sucesso!");
        }else{
            return ResponseEntity.badRequest().body("Codigo não corresponde ao que foi enviado!");
        }

    }



    @PostMapping("/trocarSenha")
    private ResponseEntity<String> trocarSenha(@RequestBody SenhaNovaDTO senhaNovaDto){

        try{
            recuperarSenhaService.trocarSenha(senhaNovaDto);
            return ResponseEntity.ok("Senha alterado com sucesso!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Senha não foi alterada com sucesso!");
        }



    }



}
