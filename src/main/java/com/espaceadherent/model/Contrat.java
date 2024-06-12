package com.espaceadherent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRAT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BRANCHE_ID", nullable = false)
    private Branche branche;

    @ManyToOne
    @JoinColumn(name = "PRODUIT_ID", nullable = false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "GARANTIE_ID", nullable = false)
    private Garanties garantie;

    @ManyToOne
    @JoinColumn(name = "EXCLUSION_ID", nullable = false)
    private Exclusions exclusion;

    @Column
    private String Referencement;

    @Column
    private String numContrat;

     @Column
     private Long prix;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_effet;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date_fin_effet;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date_paiement;

    @Column(columnDefinition = "BIT", length = 1)
    private boolean paye=false;
    @Column(columnDefinition = "BIT", length = 1)
    private boolean renewed = false;

    // bi-directional many-to-many association to Role
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_contrats", joinColumns = {@JoinColumn(name = "CONTRAT_ID")}, inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    private Set<Contrat> contrats;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = true)
    private User user;

}
