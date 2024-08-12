package com.quotehandler.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteResponse {

    String isin;

    double bid;

    double ask;
}
