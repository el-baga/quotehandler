package com.quotehandler.exception;

import com.quotehandler.dto.response.ErrorRs;
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
public class ExceptionHandlerController {

    @ExceptionHandler({BadRequestException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorRs> handleException(Exception ex) {
        log.warn(ex.getMessage());
        ErrorRs errorRs = ErrorRs.builder()
                .error("BadRequestException")
                .timestamp(System.currentTimeMillis())
                .errorDescription(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorRs, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRs> handleValidationException(MethodArgumentNotValidException ex) {
        String cause = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.warn(ex.getMessage());
        ErrorRs errorRs = ErrorRs.builder()
                .error("BadRequestException")
                .timestamp(System.currentTimeMillis())
                .errorDescription(cause)
                .build();
        return new ResponseEntity<>(errorRs, HttpStatus.BAD_REQUEST);
    }
}
