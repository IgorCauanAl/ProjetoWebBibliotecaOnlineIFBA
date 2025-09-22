package br.ifba.edu.BibliotecaOnline.repository;


import br.ifba.edu.BibliotecaOnline.entities.TokenSenha;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenSenhaRepository extends JpaRepository<TokenSenha, Long> {

    Optional<TokenSenha> findByUsuarioAndCodigo (Usuario usuario, String codigo);

    Optional<TokenSenha> findByCodigo(String codigo);

    List<TokenSenha> codigo(String codigo);
}
