package com.quotehandler.service;

import com.quotehandler.dto.response.ApiRs;
import com.quotehandler.dto.response.EnergyLvlRs;
import com.quotehandler.entity.EnergyLvl;
import com.quotehandler.entity.Quote;
import com.quotehandler.exception.BadRequestException;
import com.quotehandler.repository.EnergyLvlRepository;
import com.quotehandler.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnergyLvlService {

    private final QuoteRepository quoteRepository;

    private final EnergyLvlRepository energyLvlRepository;

    public ApiRs<EnergyLvlRs> getElvl(String isin) {
        Quote quote = quoteRepository.findByIsin(isin)
                .orElseThrow(() -> new BadRequestException("Указанный isin не существует"));
        EnergyLvl energyLvl = energyLvlRepository.findByQuoteId(quote.getId());
        double bestPrice = energyLvl.getBestPrice();
        log.info("Лучшая цена isin: {} равна - {}", isin, bestPrice);

        return ApiRs.<EnergyLvlRs>builder()
                .data(EnergyLvlRs.builder()
                        .isin(isin)
                        .bestPrice(bestPrice)
                        .build())
                .build();
    }

    public ApiRs<List<EnergyLvlRs>> getElvls() {
        List<EnergyLvl> energyLvls = energyLvlRepository.findAll();
        List<EnergyLvlRs> energyLvlRsList = new ArrayList<>(energyLvls.size());
        energyLvls.forEach(energyLvl ->
                energyLvlRsList.add(EnergyLvlRs.builder()
                        .isin(energyLvl.getQuote().getIsin())
                        .bestPrice(energyLvl.getBestPrice())
                .build()));

        return ApiRs.<List<EnergyLvlRs>>builder()
                .data(energyLvlRsList)
                .build();
    }
}
