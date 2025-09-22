package br.ifba.edu.BibliotecaOnline.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRespostaDTO {
    
    private Long id; // <-- Adicione esta linha
    private String nome;
    private String email;

}