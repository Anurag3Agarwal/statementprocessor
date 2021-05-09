package com.rabobank.statementprocessor.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.rabobank.statementprocessor.domain.model.StatementProcessorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {


    @ExceptionHandler({JsonProcessingException.class, MismatchedInputException.class})
    public ResponseEntity<Object> handleJsonException(Exception exception) {
        log.error("Exception encountered while processing the request {}", exception.getMessage());
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception exception) {
        log.error("Exception encountered while processing the request {}", exception.getMessage());
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private StatementProcessorApiResponse createExceptionResponse(HttpStatus status) {
        return new StatementProcessorApiResponse(status.getReasonPhrase(), Collections.emptyList());

    }

}
