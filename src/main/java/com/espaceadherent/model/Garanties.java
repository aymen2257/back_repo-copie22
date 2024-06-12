package com.espaceadherent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Garanties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GARANTIE_ID")
    private Long id;

    @Lob
    private String lesGaranties ;



}
