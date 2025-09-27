package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.CurtidosDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.service.CurtidosService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/liked-books")
public class CurtidosController {


    private final CurtidosService favoritosService;



    @GetMapping
    public ResponseEntity<List<CurtidosDTO>> listarCurtidos(Authentication authentication) {
        return ResponseEntity.ok(favoritosService.listarCurtidos(authentication));
    }

    @PostMapping("/{livroId}")
    public ResponseEntity<String> curtirLivro(@PathVariable Long livroId, Authentication authentication) {
        favoritosService.curtirLivro(livroId, authentication);
        return ResponseEntity.ok("Livro curtido!");
    }

    @DeleteMapping("/{livroId}")
    public ResponseEntity<String> descurtirLivro(@PathVariable Long livroId, Authentication authentication) {
        favoritosService.descurtirLivro(livroId, authentication);
        return ResponseEntity.ok("Livro removido dos curtidos!");
    }
}


