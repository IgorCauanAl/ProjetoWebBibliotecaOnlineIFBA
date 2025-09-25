package br.ifba.edu.BibliotecaOnline.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarAvaliacaoDTO {

    @NotBlank(message = "O comentário não pode estar em branco.")
    private String comentario;

    private Integer nota;
}