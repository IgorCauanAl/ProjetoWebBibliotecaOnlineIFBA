package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import br.ifba.edu.BibliotecaOnline.model.GeneroEnum;

@Entity
@Table(name = "TB_LIVRO") // Nome da tabela alinhado ao data.sql
@Getter
@Setter
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "ano_publicacao", nullable = false) // Nome da coluna padronizado
    private Integer anoPublicacao;

    @Column(name = "capa_url") // Nome da coluna padronizado
    private String capaUrl;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "sinopse", length = 2000)
    private String sinopse;

    @Column(length = 1000)
    private String descricaoAutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicado_por_admin_id")
    private Usuario publicadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private GeneroEnum genero;

}