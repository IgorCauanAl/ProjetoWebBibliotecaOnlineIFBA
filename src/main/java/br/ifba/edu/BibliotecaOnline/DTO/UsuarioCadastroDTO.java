package br.ifba.edu.BibliotecaOnline.DTO;

import br.ifba.edu.BibliotecaOnline.entities.Role;
import br.ifba.edu.BibliotecaOnline.entities.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioCadastroDTO {


    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha obrigatória")
    @Size(min = 6 , message = "Senha deve ter pelo meno 6 caracteres")
    private String senha;

}
