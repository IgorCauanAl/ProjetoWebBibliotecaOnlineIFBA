package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_USUARIOS_EXCLUIDOS")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioExcluidoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String nomeUsuario;

    @Column(nullable = false)
    private String emailUsuario;

    @Column(nullable = false)
    private String adminEmail;

    @Column(nullable = false)
    private LocalDateTime dataExclusao;

    public UsuarioExcluidoLog(Long usuarioId, String nomeUsuario, String emailUsuario, String adminEmail) {
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.adminEmail = adminEmail;
        this.dataExclusao = LocalDateTime.now();
    }
}