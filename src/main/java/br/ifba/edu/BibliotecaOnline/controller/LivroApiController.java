package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroApiController {

    private final LivroService livroService;

    @GetMapping("/search")
    public List<LivroDTO> searchLivros(@RequestParam("q") String query) {
        return livroService.buscarPorNomeOuAutor(query);
    }
}