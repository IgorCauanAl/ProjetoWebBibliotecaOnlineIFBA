package br.ifba.edu.BibliotecaOnline.repository;

import java.util.List;

import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

    List<LivroEntity> findByNomeContainingIgnoreCase(String nome);//para pesquisar o nome no html
    //List<LivroEntity> findByNomeContainingIgnoreCaseOrAutorContainingIgnoreCase(String nome, String autor);
    Page<LivroEntity> findByNomeContainingIgnoreCaseOrAutorContainingIgnoreCase(String nome, String autor, Pageable pageable);
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);
}
