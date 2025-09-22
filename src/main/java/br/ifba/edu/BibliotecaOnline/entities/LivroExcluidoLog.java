package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_LIVROS_EXCLUIDOS")
@Getter
@Setter
@NoArgsConstructor
public class LivroExcluidoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "livro_id_original", nullable = false)
    private Long livroIdOriginal;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "autor")
    private String autor;
    
    @Column(name = "deletado_por_admin_email", nullable = false)
    private String deletadoPorAdminEmail;
    
    @Column(name = "deletado_em", nullable = false)
    private LocalDateTime deletadoEm;

    public LivroExcluidoLog(Long livroIdOriginal, String titulo, String autor, String deletadoPorAdminEmail) {
        this.livroIdOriginal = livroIdOriginal;
        this.titulo = titulo;
        this.autor = autor;
        this.deletadoPorAdminEmail = deletadoPorAdminEmail;
        this.deletadoEm = LocalDateTime.now();
    }
}