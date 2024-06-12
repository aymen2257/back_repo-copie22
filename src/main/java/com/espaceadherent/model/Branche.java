package com.espaceadherent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Branche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRANCHE_ID")
    private Long id;

    @Column
    private String codeBranche;

    @Column
    private String libelleBranche;


}
