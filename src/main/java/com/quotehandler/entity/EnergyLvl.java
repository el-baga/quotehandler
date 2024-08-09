package com.quotehandler.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "energy_lvl")
public class EnergyLvl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "energy_lvl_id_seq")
    @SequenceGenerator(name = "energy_lvl_id_seq", sequenceName = "energy_lvl_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "best_price")
    private double bestPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "quote_id", referencedColumnName = "id")
    private Quote quote;
}
