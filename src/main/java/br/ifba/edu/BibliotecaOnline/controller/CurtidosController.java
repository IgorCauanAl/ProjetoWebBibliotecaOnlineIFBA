// br/ifba/edu/BibliotecaOnline/controller/CurtidosApiController.java

package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.CurtidosDTO;
import br.ifba.edu.BibliotecaOnline.service.CurtidosService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController 
@AllArgsConstructor
@RequestMapping("/api/liked-books")
public class CurtidosController {

    private final CurtidosService curtidosService;

    @GetMapping
    public ResponseEntity<Page<CurtidosDTO>> listarCurtidosPaginado(Authentication authentication, Pageable pageable) {
        Page<CurtidosDTO> paginaDeCurtidos = curtidosService.listarCurtidos(authentication, pageable);
        return ResponseEntity.ok(paginaDeCurtidos);
    }

    @PostMapping("/{livroId}")
    public ResponseEntity<String> curtirLivro(@PathVariable Long livroId, Authentication authentication) {
        curtidosService.curtirLivro(livroId, authentication);
        return ResponseEntity.ok("Livro curtido!");
    }

    @DeleteMapping("/{livroId}")
    public ResponseEntity<String> descurtirLivro(@PathVariable Long livroId, Authentication authentication) {
        curtidosService.descurtirLivro(livroId, authentication);
        return ResponseEntity.ok("Livro removido dos curtidos!");
    }
}