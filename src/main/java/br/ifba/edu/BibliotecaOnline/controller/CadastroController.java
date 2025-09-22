package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.UsuarioCadastroDTO;
import br.ifba.edu.BibliotecaOnline.DTO.UsuarioRespostaDTO;
import br.ifba.edu.BibliotecaOnline.excecao.EmailJaExisteException;
import br.ifba.edu.BibliotecaOnline.service.CadastroService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService){
        this.cadastroService = cadastroService;
    }

    @PostMapping
    public ResponseEntity<?> insert(@Valid @RequestBody UsuarioCadastroDTO dto){        
        try{
            UsuarioRespostaDTO usuarioResposta = cadastroService.insert(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResposta);
        }catch(EmailJaExisteException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
}

