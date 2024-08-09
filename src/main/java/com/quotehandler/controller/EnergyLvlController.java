package com.quotehandler.controller;

import com.quotehandler.dto.response.ApiRs;
import com.quotehandler.dto.response.EnergyLvlRs;
import com.quotehandler.service.EnergyLvlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/elvls")
@RequiredArgsConstructor
public class EnergyLvlController {

    private final EnergyLvlService energyLvlService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiRs<EnergyLvlRs> getElvl(@RequestParam String isin) {
        return energyLvlService.getElvl(isin);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiRs<List<EnergyLvlRs>> getElvls() {
        return energyLvlService.getElvls();
    }
}
