package com.espaceadherent.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    Id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String governorate;
    @Column
    private String codepostale;
    @Column
    private String cite;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
}
