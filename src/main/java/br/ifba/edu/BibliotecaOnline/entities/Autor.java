package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "TB_AUTOR")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_id")
    private Long id;

    @NotBlank
    @Column(name = "nome_autor" , nullable = false)
    private String nomeAutor;

    @Column(name = "foto_autor")
    private String fotoAutor;

    @NotBlank
    @Column(name = "descricao_autor", columnDefinition = "TEXT")
    private String descricaoDoAutor;

    //Para cada autor há vários livros
    @OneToMany(mappedBy = "autor")
    private List<LivroEntity> livros = new ArrayList<>();



}
