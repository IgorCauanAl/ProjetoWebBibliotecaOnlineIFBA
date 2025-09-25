package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Long> {
    
    // Método para buscar todas as avaliações de um livro específico
    List<AvaliacaoEntity> findByLivroIdOrderByDataAvaliacaoDesc(Long livroId);
}