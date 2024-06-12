package com.espaceadherent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Nom;
    private String email;
    @Lob
    private String message;
    @Column(name = "Date_Envoi", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date;

}
