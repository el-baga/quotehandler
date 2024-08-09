package com.quotehandler.service;

import com.quotehandler.dto.request.QuoteRq;
import com.quotehandler.dto.response.ApiRs;
import com.quotehandler.dto.response.QuoteRs;
import com.quotehandler.entity.EnergyLvl;
import com.quotehandler.entity.Quote;
import com.quotehandler.exception.BadRequestException;
import com.quotehandler.repository.EnergyLvlRepository;
import com.quotehandler.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuoteService {

    private final QuoteRepository quoteRepository;

    private final EnergyLvlRepository energyLvlRepository;

    @Transactional
    @Caching(cacheable = {
            @Cacheable(cacheNames = "customerCache", cacheManager = "caffeineCacheManager")
    })
    public ApiRs<QuoteRs> addQuote(QuoteRq quoteRq) {
        String isin = quoteRq.getIsin();
        double bid = quoteRq.getBid();
        double ask = quoteRq.getAsk();
        if (bid >= ask) {
            throw new BadRequestException("Значение bid указанной котировки должно быть меньше значения ask");
        }

        ApiRs<QuoteRs> quoteApiRs;
        Optional<Quote> quoteOptional = quoteRepository.findByIsin(isin);
        quoteApiRs = quoteOptional
                .map(quote -> updateQuoteInDatabase(quote, bid, ask))
                .orElseGet(() -> saveQuoteInDatabase(isin, bid, ask));
        return quoteApiRs;
    }

    private ApiRs<QuoteRs> saveQuoteInDatabase(String isin, double bid, double ask) {
        Quote quote = new Quote();
        quote.setIsin(isin);
        quote.setBid(bid);
        quote.setAsk(ask);
        quoteRepository.saveAndFlush(quote);
        EnergyLvl energyLvl = new EnergyLvl();
        energyLvl.setQuote(quote);
        energyLvl.setBestPrice(bid);
        if (bid == 0.0) {
            energyLvl.setBestPrice(ask);
        }
        energyLvlRepository.saveAndFlush(energyLvl);
        log.info("Новая котировка с isin - {} и elvl - {} были успешно сохранены", isin, energyLvl.getBestPrice());
        return getQuoteApiRs(quote);
    }

    private ApiRs<QuoteRs> updateQuoteInDatabase(Quote quote, double bid, double ask) {
        quote.setAsk(ask);
        quote.setBid(bid);
        quoteRepository.saveAndFlush(quote);
        EnergyLvl energyLvl = energyLvlRepository.findByQuoteId(quote.getId());
        energyLvl.setBestPrice(calculateBestPrice(bid, ask, energyLvl.getBestPrice()));
        energyLvlRepository.saveAndFlush(energyLvl);
        log.info("Котировка с isin - {} и elvl - {} были успешно обновлены", quote.getIsin(), energyLvl.getBestPrice());
        return getQuoteApiRs(quote);
    }

    private double calculateBestPrice(double bid, double ask, double bestPrice) {
        if (bid > bestPrice) {
            bestPrice = bid;
        } else if (ask < bestPrice) {
            bestPrice = ask;
        }
        return bestPrice;
    }

    private ApiRs<QuoteRs> getQuoteApiRs(Quote quote) {
        return ApiRs.<QuoteRs>builder()
                .data(QuoteRs.builder()
                        .isin(quote.getIsin())
                        .bid(quote.getBid())
                        .ask(quote.getAsk())
                        .build())
                .build();
    }
}
