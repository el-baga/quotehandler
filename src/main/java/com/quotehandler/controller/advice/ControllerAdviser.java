package com.quotehandler.controller.advice;

import com.quotehandler.dto.response.ErrorResponse;
import com.quotehandler.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ControllerAdviser {

    @ExceptionHandler({BadRequestException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.warn(ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("")
                .timestamp(System.currentTimeMillis())
                .errorDescription(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String cause = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.warn(ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("")
                .timestamp(System.currentTimeMillis())
                .errorDescription(cause)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
