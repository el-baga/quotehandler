package com.quotehandler.controller;

import com.quotehandler.controller.advice.ControllerAdviser;
import com.quotehandler.dto.request.QuoteRequest;
import com.quotehandler.service.QuoteService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuoteController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@MockBeans({
        @MockBean(QuoteController.class),
        @MockBean(QuoteService.class)
})
class QuoteControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    @DisplayName("Введены корректные данные котировки")
    void when_QuoteDataValid_Expect_Ok() throws Exception {
        String isin = "QRTYUJG78G9D";
        double bid = 100.1;
        double ask = 100.4;

        this.mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Длина isin не равна 12")
    void when_IsinLengthNotValid_Expect_BadRequest() throws Exception {
        String isin = "QRYYT";
        double bid = 100.1;
        double ask = 100.4;

        this.mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String getQuoteRq(String isin, double bid, double ask) throws JsonProcessingException {
        QuoteRequest quoteRequest = new QuoteRequest(isin, bid, ask);
        return new ObjectMapper().writeValueAsString(quoteRequest);
    }
}
