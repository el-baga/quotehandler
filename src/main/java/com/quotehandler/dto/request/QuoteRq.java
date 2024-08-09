package com.quotehandler.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRq {

    @JsonProperty("isin")
    @NotBlank(message = "isin не может быть пустым")
    @NotNull(message = "isin не может быть пустым")
    @Length(min = 12, max = 12, message = "Значение isin указанной котировки должно состоять из 12 символов")
    private String isin;

    @JsonProperty("bid")
    private double bid;

    @JsonProperty("ask")
    private double ask;
}
