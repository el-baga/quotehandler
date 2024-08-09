package com.quotehandler.entity;

import lombok.*;
import jakarta.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quote_id_seq")
    @SequenceGenerator(name = "quote_id_seq", sequenceName = "quote_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "isin", nullable = false)
    private String isin;

    @Column(name = "bid")
    private double bid;

    @Column(name = "ask")
    private double ask;

    @OneToOne(mappedBy = "quote")
    @ToString.Exclude
    private EnergyLvl energyLvl;
}
