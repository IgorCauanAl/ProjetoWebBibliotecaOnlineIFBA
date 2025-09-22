package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {


    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Usuario> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String palavraChave, String palavraChave2,
            Pageable pageable);
}
