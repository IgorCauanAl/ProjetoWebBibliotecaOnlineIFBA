package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.AvaliacaoEntity;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Long> {
    
    // Método para buscar todas as avaliações de um livro específico
    List<AvaliacaoEntity> findByLivroIdOrderByDataAvaliacaoDesc(Long livroId);

    Optional<AvaliacaoEntity> findByUsuarioAndLivro(Usuario usuario, LivroEntity livro);
}