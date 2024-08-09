package com.quotehandler.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRs<T> {

    @Builder.Default
    private Long timestamp = System.currentTimeMillis();

    private T data;
}
