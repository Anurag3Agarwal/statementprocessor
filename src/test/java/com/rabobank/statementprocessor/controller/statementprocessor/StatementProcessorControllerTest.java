package com.rabobank.statementprocessor.controller.statementprocessor;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.rabobank.statementprocessor.domain.model.StatementProcessorApiResponse;
import com.rabobank.statementprocessor.service.StatementProcessorService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatementProcessorController.class)
public class StatementProcessorControllerTest {

    @MockBean
    private StatementProcessorService service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMultipartFile file;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        file =
                new MockMultipartFile(
                        "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void processAndValidateDataTest() {

        Mockito.when(service.validateAndProcessStatements(Mockito.any()))
                .thenReturn(StatementProcessorApiResponse.builder().result("SUCCESS").build());
        mockMvc
                .perform(multipart("/customer-statements/v1/process").file(file))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void processAndValidateDataInternalServerErrorTest() {

        Mockito.when(service.validateAndProcessStatements(Mockito.any()))
                .thenThrow(IllegalArgumentException.class);
        mockMvc
                .perform(multipart("/customer-statements/v1/process").file(file))
                .andExpect(status().is5xxServerError());
    }

    @SneakyThrows
    @Test
    public void processAndValidateDataBadRequestTest() {

        Mockito.when(service.validateAndProcessStatements(Mockito.any()))
                .thenThrow(MismatchedInputException.class);
        mockMvc
                .perform(multipart("/customer-statements/v1/process").file(file))
                .andExpect(status().is4xxClientError());
    }
}
