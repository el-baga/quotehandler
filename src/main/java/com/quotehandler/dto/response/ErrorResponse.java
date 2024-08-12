package com.quotehandler.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {

    String error;

    Long timestamp;

    String errorDescription;
}
