package br.ifba.edu.BibliotecaOnline.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import br.ifba.edu.BibliotecaOnline.config.audit.CustomRevisionListener;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TB_REVINFO") // Nome da tabela que guardará as revisões
@RevisionEntity(CustomRevisionListener.class)
@Data 
public class CustomRevisionEntity implements Serializable {

    @Id
    @GeneratedValue
    @RevisionNumber // Marca o campo como o número da revisão
    private long id;

    @RevisionTimestamp // Marca o campo como a data/hora da revisão
    private Date timestamp;

    @Column(name = "username") //para saber QUEM fez a alteração
    private String username;
}