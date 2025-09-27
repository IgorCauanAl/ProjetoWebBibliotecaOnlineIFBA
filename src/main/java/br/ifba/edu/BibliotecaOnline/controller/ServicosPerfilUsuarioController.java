package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.EditarDetalhesPerfilDTO;
import br.ifba.edu.BibliotecaOnline.DTO.MudarSenhaDTO;
import br.ifba.edu.BibliotecaOnline.config.CustomUserDetails;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.service.ServicosUsuariosPerfilService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/usuariosMudar")

public class ServicosPerfilUsuarioController {

    private  ServicosUsuariosPerfilService servicosUsuariosPerfilService;

    public ServicosPerfilUsuarioController(ServicosUsuariosPerfilService servicosUsuariosPerfilService) {
        this.servicosUsuariosPerfilService = servicosUsuariosPerfilService;
    }

    //End Point para mudar a senha
    @PostMapping("/senha")
    public ResponseEntity<String> mudarSenhaUser(@RequestBody MudarSenhaDTO mudarSenhaDto){
        try{
            servicosUsuariosPerfilService.alterarSenha(mudarSenhaDto);
            return ResponseEntity.ok("Senha alterado com sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Senha não foi alterado com sucesso");
        }
    }

    //End Point para deletar a conta
    @PostMapping("/deletarConta")
    public ResponseEntity<String> deletarConta(){
        try{
            servicosUsuariosPerfilService.apagarConta();
            return ResponseEntity.ok("Conta apagada com sucesso!");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Conta não foi apagada com sucesso!");
        }
    }

    
    @PostMapping("/editarDetalhes")
    public ResponseEntity<String> editarDetalhes (@RequestBody EditarDetalhesPerfilDTO dto, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        try{
        servicosUsuariosPerfilService.alterarDetalhes(dto,userDetails, request);
        return ResponseEntity.ok("Email e nome alterados com sucesso!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Nome ou email inválidos!");
        }
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<String> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // 1. Chama o serviço, que agora contém toda a lógica
            String novoCaminhoFoto = servicosUsuariosPerfilService.atualizarFotoPerfil(id, file);

            // 2. Atualiza a sessão do usuário logado para refletir a mudança imediatamente
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                Usuario usuarioDaSessao = ((CustomUserDetails) principal).getUsuario();
                usuarioDaSessao.setFotoPerfil(novoCaminhoFoto);
            }

            // 3. Retorna a resposta de sucesso
            return ResponseEntity.ok(novoCaminhoFoto);

        } catch (RuntimeException e) {
            // 4. Retorna uma resposta de erro caso o serviço lance uma exceção
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
