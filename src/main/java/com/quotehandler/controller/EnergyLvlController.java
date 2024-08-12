package com.quotehandler.controller;

import com.quotehandler.dto.response.ApiResponse;
import com.quotehandler.dto.response.EnergyLvlResponse;
import com.quotehandler.service.EnergyLvlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elvl")
@RequiredArgsConstructor
public class EnergyLvlController {

    private final EnergyLvlService energyLvlService;

    @GetMapping(value = "/{isin}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<EnergyLvlResponse> getElvl(@PathVariable String isin) {
        return energyLvlService.getElvl(isin);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<EnergyLvlResponse>> getElvls() {
        return energyLvlService.getElvls();
    }
}
