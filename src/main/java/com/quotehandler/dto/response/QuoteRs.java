package com.quotehandler.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteRs {

    private String isin;

    private double bid;

    private double ask;
}
