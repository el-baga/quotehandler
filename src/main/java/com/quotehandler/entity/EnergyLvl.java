package com.quotehandler.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

    @OneToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "quote_id", referencedColumnName = "id")
    private Quote quote;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EnergyLvl energyLvl = (EnergyLvl) o;
        return getId() != null && Objects.equals(getId(), energyLvl.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
