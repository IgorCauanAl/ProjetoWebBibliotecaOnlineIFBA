package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroApiController {

    private final LivroService livroService;

    @GetMapping("/search")
    public Page<LivroDTO> searchLivros(@RequestParam("q") String query, Pageable pageable) {
        return livroService.buscarPorPalavraChave(query, pageable);
    }
}