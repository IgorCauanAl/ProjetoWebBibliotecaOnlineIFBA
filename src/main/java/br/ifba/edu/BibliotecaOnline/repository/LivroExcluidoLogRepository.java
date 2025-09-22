package br.ifba.edu.BibliotecaOnline.repository;

import br.ifba.edu.BibliotecaOnline.entities.LivroExcluidoLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroExcluidoLogRepository extends JpaRepository<LivroExcluidoLog, Long> {
}