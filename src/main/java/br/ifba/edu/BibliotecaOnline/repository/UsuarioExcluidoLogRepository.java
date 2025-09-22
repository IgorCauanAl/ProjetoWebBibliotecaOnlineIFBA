package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.UsuarioExcluidoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioExcluidoLogRepository extends JpaRepository<UsuarioExcluidoLog, Long> {
}