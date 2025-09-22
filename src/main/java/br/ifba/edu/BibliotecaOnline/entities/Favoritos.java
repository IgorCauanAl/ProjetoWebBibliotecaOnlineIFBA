package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_FAVORITOS")
public class Favoritos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favoritos_id")
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    //Muitos favoritos pertencem a único usuario
    private Usuario usuarioFavoritos;


    @ManyToMany
    @JoinTable(
            name = "TB_FAVORITO_LIVRO",
            joinColumns = @JoinColumn (name = "favorito_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    //Usuario pode ter livros associado a seu favorito
    private List<LivroEntity> livros = new ArrayList<>();

}
