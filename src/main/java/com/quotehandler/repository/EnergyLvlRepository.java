package com.quotehandler.repository;

import com.quotehandler.entity.EnergyLvl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyLvlRepository extends JpaRepository<EnergyLvl, Long> {

    EnergyLvl findByQuoteId(Long quoteId);
}
