package br.ifba.edu.BibliotecaOnline.controller;

import br.ifba.edu.BibliotecaOnline.DTO.AvaliacaoDTO;
import br.ifba.edu.BibliotecaOnline.DTO.CriarAvaliacaoDTO;
import br.ifba.edu.BibliotecaOnline.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros/{livroId}/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criarAvaliacao(
            @PathVariable Long livroId,
            @Valid @RequestBody CriarAvaliacaoDTO dto,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        AvaliacaoDTO novaAvaliacao = avaliacaoService.salvar(dto, livroId, emailUsuario);
        return new ResponseEntity<>(novaAvaliacao, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listarAvaliacoes(@PathVariable Long livroId) {
        List<AvaliacaoDTO> avaliacoes = avaliacaoService.buscarPorLivroId(livroId);
        return ResponseEntity.ok(avaliacoes);
    }

    @PutMapping("/{avaliacaoId}")
    public ResponseEntity<AvaliacaoDTO> atualizarAvaliacao(
            @PathVariable Long livroId,
            @PathVariable Long avaliacaoId,
            @Valid @RequestBody CriarAvaliacaoDTO dto,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        AvaliacaoDTO avaliacaoAtualizada = avaliacaoService.atualizar(avaliacaoId, dto, emailUsuario);
        return ResponseEntity.ok(avaliacaoAtualizada);
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> deletarAvaliacao(
            @PathVariable Long livroId,
            @PathVariable Long avaliacaoId,
            Authentication authentication) {
        
        String emailUsuario = authentication.getName();
        avaliacaoService.deletar(avaliacaoId, emailUsuario);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}