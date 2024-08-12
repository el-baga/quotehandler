package com.quotehandler.service;

import com.quotehandler.dto.response.ApiResponse;
import com.quotehandler.dto.response.EnergyLvlResponse;
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

    public ApiResponse<EnergyLvlResponse> getElvl(String isin) {
        Quote quote = quoteRepository.findByIsin(isin)
                .orElseThrow(() -> new BadRequestException("Указанный isin не существует"));
        EnergyLvl energyLvl = energyLvlRepository.findByQuoteId(quote.getId());
        double bestPrice = energyLvl.getBestPrice();
        log.info("Лучшая цена isin: {} равна - {}", isin, bestPrice);

        return ApiResponse.<EnergyLvlResponse>builder()
                .data(EnergyLvlResponse.builder()
                        .isin(isin)
                        .bestPrice(bestPrice)
                        .build())
                .build();
    }

    public ApiResponse<List<EnergyLvlResponse>> getElvls() {
        List<EnergyLvl> energyLvls = energyLvlRepository.findAll();
        List<EnergyLvlResponse> energyLvlResponseList = new ArrayList<>(energyLvls.size());
        energyLvls.forEach(energyLvl ->
                energyLvlResponseList.add(EnergyLvlResponse.builder()
                        .isin(energyLvl.getQuote().getIsin())
                        .bestPrice(energyLvl.getBestPrice())
                .build()));

        return ApiResponse.<List<EnergyLvlResponse>>builder()
                .data(energyLvlResponseList)
                .build();
    }
}
