package br.ifba.edu.BibliotecaOnline.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvaliacaoDTO {
    private Long id;
    private String comentario;
    private int nota;
    private LocalDateTime dataAvaliacao;
    private String nomeUsuario;
    private String fotoUsuario; // Adicional para exibir a foto do usuário no comentário
    private String emailUsuario;
}