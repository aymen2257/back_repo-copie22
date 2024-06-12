package com.espaceadherent.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Nom;
    private String description;
    @Lob
    private String contenu;
    private String titre;
    @ManyToOne
    @JoinColumn(name = "BRANCHE_ID", nullable = false)
    private Branche branche;
    @Lob
    @Column(name = "icon_file", columnDefinition="LONGBLOB")
    private byte[] icon;
    @Lob
    @Column(name = "image_file", columnDefinition="LONGBLOB")
    private byte[] image;  // Renamed to clarify its usage
}
