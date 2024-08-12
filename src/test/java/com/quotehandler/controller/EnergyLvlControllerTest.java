package com.quotehandler.controller;

import com.quotehandler.service.EnergyLvlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnergyLvlController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@MockBeans({
        @MockBean(EnergyLvlController.class),
        @MockBean(EnergyLvlService.class)
})
class EnergyLvlControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    @DisplayName("Получение energy_lvl по isin")
    void when_EnergyLvlIxistsByIsin_Expect_Ok() throws Exception {
        String isin = "QRTYUJG78G9D";
        mock.perform(get(String.format("/api/elvl/%s", isin)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение всех energy_lvl")
    void when_GetAllEnergyLvlsCalled_Expect_Ok() throws Exception {
        mock.perform(get("/api/elvl/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
