package com.quotehandler.service;

import com.quotehandler.config.TestConfig;
import com.quotehandler.controller.QuoteController;
import com.quotehandler.dto.request.QuoteRequest;
import com.quotehandler.repository.EnergyLvlRepository;
import com.quotehandler.repository.QuoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Sql(value = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class QuoteServiceTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private QuoteController quoteController;

    @Autowired
    private EnergyLvlRepository energyLvlRepository;

    @Test
    @DisplayName("Введены корректные данные котировки")
    void when_QuoteDataValid_Expect_Ok() throws Exception {
        String isin = "QRTYUJG78G9D";
        double bid = 100.3;
        double ask = 100.4;
        mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(jsonPath("$.data.isin").value(isin))
                .andExpect(jsonPath("$.data.bid").value(bid))
                .andExpect(jsonPath("$.data.ask").value(ask));
    }

    @Test
    @DisplayName("Введены данные с 0.0 bid котировки")
    void when_SetAskToBestPriceIfBidEmpty_Expect_Ok() throws Exception {
        String isin = "MNBVTHGKLU91";
        double bid = 0.0;
        double ask = 100.7;
        mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(jsonPath("$.data.isin").value(isin))
                .andExpect(jsonPath("$.data.bid").value(bid))
                .andExpect(jsonPath("$.data.ask").value(ask));

        assertEquals(ask , energyLvlRepository.findByQuoteId(3L).getBestPrice());
    }

    @Test
    @DisplayName("Введен существующий isin котировки")
    void when_UpdateQuoteData_Expect_Ok() throws Exception {
        String isin = "AGHTNBKGHVRT";
        double bid = 100.6;
        double ask = 100.7;
        mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(jsonPath("$.data.bid").value(bid))
                .andExpect(jsonPath("$.data.ask").value(ask));

        assertEquals(bid , energyLvlRepository.findByQuoteId(1L).getBestPrice());
    }

    @Test
    @DisplayName("bid больше ask")
    void when_BidGreaterThanAsk_Expect_BadRequest() throws Exception {
        String isin = "QRTYUJG78G9D";
        double bid = 111;
        double ask = 100.4;
        mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String getQuoteRq(String isin, double bid, double ask) throws JsonProcessingException {
        QuoteRequest quoteRequest = new QuoteRequest();
        quoteRequest.setIsin(isin);
        quoteRequest.setBid(bid);
        quoteRequest.setAsk(ask);
        return new ObjectMapper().writeValueAsString(quoteRequest);
    }
}
