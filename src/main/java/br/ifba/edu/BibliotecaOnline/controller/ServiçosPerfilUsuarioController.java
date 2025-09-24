package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.MudarSenhaDTO;
import br.ifba.edu.BibliotecaOnline.service.ServiçosUsuariosPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuariosMudar")
public class ServiçosPerfilUsuarioController {

    @Autowired
    private ServiçosUsuariosPerfilService serviçosUsuariosPerfilService;

    //End Point para mudar a senha
    @PostMapping("/senha")
    public ResponseEntity<String> mudarSenhaUser(@RequestBody MudarSenhaDTO mudarSenhaDto){
        try{
            serviçosUsuariosPerfilService.alterarSenha(mudarSenhaDto);
            return ResponseEntity.ok("Senha alterado com sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Senha não foi alterado com sucesso");
        }
    }

    //End Point para deletar a conta
    @PostMapping("/deletarConta")
    public ResponseEntity<String> deletarConta(){
        try{
            serviçosUsuariosPerfilService.apagarConta();
            return ResponseEntity.ok("Conta apagada com sucesso!");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Conta não foi apagada com sucesso!");
        }
    }

}
