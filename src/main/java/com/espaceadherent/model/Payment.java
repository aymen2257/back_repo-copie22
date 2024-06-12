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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long id;

    @Column(name = "SESSION_ID")
    private String sessionId;

    @Column(name = "prix")
    private Long amount; // Amount in TND

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "SIGNATURE")
    private String signature;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_PAIEMENT")
    private Date datePaiement;

    @ManyToOne
    @JoinColumn(name = "CONTRAT_ID", nullable = false)
    private Contrat contrat;

    @Column(name = "STATUS")
    private String status;

    private Date expirationTime;
}
