package com.quotehandler.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnergyLvlResponse {

    String isin;

    @JsonProperty("best_price")
    double bestPrice;
}
