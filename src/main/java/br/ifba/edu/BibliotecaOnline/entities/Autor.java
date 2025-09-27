package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Audited
@Table(name = "TB_AUTOR")
public class Autor {

    public Autor(String nomeAutor,String descricaoDoAutor) {
        this.nomeAutor = nomeAutor;
        this.descricaoDoAutor = descricaoDoAutor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_id")
    private Long id;

    @NotBlank
    @Column(name = "nome_autor" , nullable = false, unique = true) // Adicionado unique = true
    private String nomeAutor;

    @Column(name = "foto_autor")
    private String fotoAutor;

    @NotBlank
    @Column(name = "descricao_autor", columnDefinition = "TEXT")
    private String descricaoDoAutor;

    //Para cada autor há vários livros
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<LivroEntity> livros = new ArrayList<>();


    

    
}