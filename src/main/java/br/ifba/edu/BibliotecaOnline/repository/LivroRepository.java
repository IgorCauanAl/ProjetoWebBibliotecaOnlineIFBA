package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

    // Método de busca ajustado para pesquisar pelo nome do livro OU pelo nome do autor na entidade relacionada
    Page<LivroEntity> findByNomeContainingIgnoreCaseOrAutorNomeAutorContainingIgnoreCase(String nome, String autorNome, Pageable pageable);

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}