package com.quotehandler.controller;

import com.quotehandler.dto.request.QuoteRq;
import com.quotehandler.service.QuoteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(value = "/sql/quoteController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/quoteController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class QuoteControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private MockMvc mock;

    @Autowired
    private QuoteService quoteService;

    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16.3");

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void testAddQuote() throws Exception {
        String isin = "QRTYUJG78G9D";
        double bid = 100.1;
        double ask = 100.4;

        this.mock.perform(post("/api/quotes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getQuoteRq(isin, bid, ask)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isin").value(isin));
    }

    private String getQuoteRq(String isin, double bid, double ask) throws JsonProcessingException {
        QuoteRq quoteRq = new QuoteRq(isin, bid, ask);
        return new ObjectMapper().writeValueAsString(quoteRq);
    }
}
