package com.quotehandler.controller;

import com.quotehandler.dto.request.QuoteRequest;
import com.quotehandler.dto.response.ApiResponse;
import com.quotehandler.dto.response.QuoteResponse;
import com.quotehandler.service.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<QuoteResponse> addQuote(@Valid @RequestBody QuoteRequest quoteRequest) {
        return quoteService.addQuote(quoteRequest);
    }
}
