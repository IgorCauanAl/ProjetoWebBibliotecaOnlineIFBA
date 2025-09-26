package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.EditarDetalhesPerfilDTO;
import br.ifba.edu.BibliotecaOnline.DTO.MudarSenhaDTO;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import br.ifba.edu.BibliotecaOnline.repository.UsuarioRepository;
import br.ifba.edu.BibliotecaOnline.service.ServiçosUsuariosPerfilService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/usuariosMudar")

public class ServiçosPerfilUsuarioController {

    @Autowired
    private  ServiçosUsuariosPerfilService serviçosUsuariosPerfilService;
    @Autowired
    private  UsuarioRepository usuarioRepository;


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

    //EndPoint para mudar nome e email
    @PostMapping("/editarDetalhes")
    public ResponseEntity<String> editarDetalhes (@RequestBody EditarDetalhesPerfilDTO dto, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        try{
           serviçosUsuariosPerfilService.alterarDetalhes(dto,userDetails, request);
           return ResponseEntity.ok("Email e nome alterados com sucesso!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Nome ou email inválidos!");
        }
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<String> uploadFoto(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file) {
        try {
            // Criar pasta se não existir
            Path path = Paths.get("uploads/fotos-perfil/");
            if (!Files.exists(path)) Files.createDirectories(path);

            // Salvar arquivo com nome único
            String nome = "usuario-" + id + "-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), path.resolve(nome), StandardCopyOption.REPLACE_EXISTING);

            // Atualizar caminho no banco
            Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            u.setFotoPerfil("/uploads/fotos-perfil/" + nome);
            usuarioRepository.save(u);

            return ResponseEntity.ok("Foto atualizada!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao salvar foto");
        }
    }



}
