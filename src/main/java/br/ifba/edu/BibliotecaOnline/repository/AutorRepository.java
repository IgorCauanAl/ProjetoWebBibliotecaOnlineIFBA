package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeAutorIgnoreCase(String nome);

}
