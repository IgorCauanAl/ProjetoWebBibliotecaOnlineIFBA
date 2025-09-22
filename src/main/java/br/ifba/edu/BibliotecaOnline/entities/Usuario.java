package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "TB_USUARIO")
public class Usuario {

    @Id
    @Column(name = "usuario_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=100)
    @Column(name="nome_usuario", nullable=false)
    private String nome;

    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(name = "senha",nullable=false, length=60)
    private String senha;

    @Column(name = "foto_perfil")
    private String fotoPerfil;


    // Lista de favoritos do usuário
    @OneToMany(mappedBy = "usuarioFavoritos")
    private List<Favoritos> favoritos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "TB_LIVRO_CURTIDA",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name ="livro_id")
    )
    //Livros curtidos pelo usuário
    private List<LivroEntity> livrosCurtidos = new ArrayList<>();

    //Lista de Livros Publicados
    @OneToMany(mappedBy = "publicadoPor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LivroEntity> livrosPublicados = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name ="TB_USUARIO_ROLE",
        joinColumns = @JoinColumn(name = "usuario_id"),      
        inverseJoinColumns = @JoinColumn(name = "role_id")    
    )
    private Set<Role> roles = new HashSet<>();


    //Token do usuario para recuperação da senha
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private TokenSenha tokenSenha;

}