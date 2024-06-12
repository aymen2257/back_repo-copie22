package com.espaceadherent.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name="reclamation")


public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomprenom", nullable = false, length = 100)
    private String nomprenom;

    @Column(name = "telephone", nullable = false, length = 15)
    private String telephone;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "categorie", nullable = false, length = 50)
    private String categorie;

    @Column(name = "sujet", nullable = false, length = 200)
    private String sujet;

    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;
    @Column(name = "file_type")
    private String fileType;
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_user_id", referencedColumnName = "USER_ID") // Assuming 'id' is the column name in 'User' table.
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReclamationStatus status = ReclamationStatus.ENATTENTE; // default status

}
