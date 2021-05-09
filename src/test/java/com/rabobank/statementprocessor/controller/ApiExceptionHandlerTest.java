package com.rabobank.statementprocessor.controller;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApiExceptionHandler.class}, loader = AnnotationConfigContextLoader.class)
public class ApiExceptionHandlerTest {

    @Autowired
    private ApiExceptionHandler apiExceptionHandler;

    @Mock
    private MismatchedInputException mismatchedInputException;

    @Test
    public void handleAllExceptionsTest() {
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiExceptionHandler.handleAllException(new NumberFormatException()).getStatusCode());
    }

    @Test
    public void handleJsonExceptionTest() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, apiExceptionHandler.handleJsonException(mismatchedInputException).getStatusCode());


    }
}

