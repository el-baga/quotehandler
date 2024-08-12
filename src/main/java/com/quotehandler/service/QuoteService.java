package com.quotehandler.service;

import com.quotehandler.dto.request.QuoteRequest;
import com.quotehandler.dto.response.ApiResponse;
import com.quotehandler.dto.response.QuoteResponse;
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
            @Cacheable(cacheNames = "quoteCache", cacheManager = "caffeineCacheManager")
    })
    public ApiResponse<QuoteResponse> addQuote(QuoteRequest quoteRequest) {
        String isin = quoteRequest.getIsin();
        double bid = quoteRequest.getBid();
        double ask = quoteRequest.getAsk();
        if (bid >= ask) {
            throw new BadRequestException("Значение bid указанной котировки должно быть меньше значения ask");
        }

        ApiResponse<QuoteResponse> quoteApiResponse;
        Optional<Quote> quoteOptional = quoteRepository.findByIsin(isin);
        quoteApiResponse = quoteOptional
                .map(quote -> updateQuoteInDatabase(quote, bid, ask))
                .orElseGet(() -> saveQuoteInDatabase(isin, bid, ask));
        return quoteApiResponse;
    }

    private ApiResponse<QuoteResponse> saveQuoteInDatabase(String isin, double bid, double ask) {
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

    private ApiResponse<QuoteResponse> updateQuoteInDatabase(Quote quote, double bid, double ask) {
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

    private ApiResponse<QuoteResponse> getQuoteApiRs(Quote quote) {
        return ApiResponse.<QuoteResponse>builder()
                .data(QuoteResponse.builder()
                        .isin(quote.getIsin())
                        .bid(quote.getBid())
                        .ask(quote.getAsk())
                        .build())
                .build();
    }
}
