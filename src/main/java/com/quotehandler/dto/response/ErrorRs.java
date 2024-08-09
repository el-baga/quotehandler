package com.quotehandler.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorRs {
    private String error;
    private Long timestamp;
    private String  errorDescription;
}
