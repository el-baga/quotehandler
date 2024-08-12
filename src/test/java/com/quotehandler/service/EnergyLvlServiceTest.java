package com.quotehandler.service;

import com.quotehandler.config.TestConfig;
import com.quotehandler.controller.EnergyLvlController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Sql(value = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EnergyLvlServiceTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private EnergyLvlController energyLvlController;

    @Test
    @DisplayName("Получение elvl по isin")
    void when_GetElvlByIsin_Expect_Ok() throws Exception {
        String isin = "AGHTNBKGHVRT";
        mock.perform(get(String.format("/api/elvl/%s", isin)))
                .andDo(print())
                .andExpect(jsonPath("$.data.isin").value(isin))
                .andExpect(jsonPath("$.data.best_price").value(100.5));
    }

    @Test
    @DisplayName("Получение elvl по некорректному isin")
    void when_GetElvlByIncorrectIsin_Expect_BadRequest() throws Exception {
        String isin = "INVALIDISIN";
        mock.perform(get(String.format("/api/elvl/%s", isin)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Получение всех elvls")
    void when_GetElvls_Expect_Ok() throws Exception {
        mock.perform(get("/api/elvl/all"))
                .andDo(print())
                .andExpect(jsonPath("$.data[1].isin").value("UUURNB5GHV34"))
                .andExpect(jsonPath("$.data[1].best_price").value(101.8));
    }
}
