package com.quotehandler.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnergyLvlRs {

    private String isin;

    @JsonProperty("best_price")
    private double bestPrice;
}
