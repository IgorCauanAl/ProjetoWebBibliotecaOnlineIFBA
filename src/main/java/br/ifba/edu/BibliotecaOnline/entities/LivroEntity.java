package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.envers.Audited;

import java.util.List;
import java.util.ArrayList;

import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;

@Entity
@Table(name = "TB_LIVRO") // Nome da tabela alinhado ao data.sql
@Getter
@Setter
@Audited
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "ano_publicacao", nullable = false) // Nome da coluna padronizado
    private Integer anoPublicacao;

    @Column(name = "capa_url") // Nome da coluna padronizado
    private String capaUrl;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "sinopse", length = 2000)
    private String sinopse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicado_por_admin_id")
    private Usuario publicadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private GeneroEnum genero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = true) // Novo campo de relacionamento
    private Autor autor;

    @ManyToMany(mappedBy = "livrosCurtidos")
    private List<Usuario> usuariosQueCurtiram = new ArrayList<>();

}