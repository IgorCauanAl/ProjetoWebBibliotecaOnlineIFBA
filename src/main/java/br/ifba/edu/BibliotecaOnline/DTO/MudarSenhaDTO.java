package br.ifba.edu.BibliotecaOnline.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MudarSenhaDTO {

    private String senhaAntiga;
    private String senhaNova;

}
