package br.ifba.edu.BibliotecaOnline.DTO;

import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroDTO {
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String nome;

    @NotNull(message = "Campo ano de Publicação obrigatorio!")
    private Integer anoPublicacao;

    private String capaUrl;
    
    private String pdfUrl;

    @Size(max = 2000)
    @NotBlank(message = "Campo Sinopse obrigatorio!")
    private String sinopse;

    private GeneroEnum genero;

    private String publicadoPorNome;
    
    private String autorNome;
    
    // Usado quando se seleciona um autor existente no formulário
    private Long autorId;

    // Usado quando se cadastra um novo autor junto com o livro
    private String novoAutorNome;

    private String novoAutorDescricao;

    private String novoAutorFotoUrl;

    private boolean curtidoPeloUsuario; 

    private String tipoAutor;
}