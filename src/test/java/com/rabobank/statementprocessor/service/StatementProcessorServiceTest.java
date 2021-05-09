package com.rabobank.statementprocessor.service;

import com.rabobank.statementprocessor.domain.Status;
import com.rabobank.statementprocessor.service.impl.StatementProcessorServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;

@SpringBootTest
public class StatementProcessorServiceTest {

    @Autowired
    private StatementProcessorService service;

    @Mock
    private JobExecution jobExecution;

    @Mock
    private ExecutionContext executionContext;

    @InjectMocks
    private StatementProcessorServiceImpl statementProcessorService;

    @Test
    @SneakyThrows
    public void incorrectFileTypeTest() {
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> service.validateAndProcessStatements(file));
    }

    @Test
    @SneakyThrows
    public void handleFailureResponseTest() {
        Mockito.when(jobExecution.getStatus()).thenReturn(BatchStatus.FAILED);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> statementProcessorService.handleRespone(jobExecution));
    }

    @Test
    @SneakyThrows
    public void handleSuccessResponseTest() {
        Mockito.when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        Mockito.when(jobExecution.getExecutionContext()).thenReturn(executionContext);
        Mockito.when(executionContext.get("failed_records")).thenReturn(Collections.emptyList());
        Mockito.when(executionContext.get("status")).thenReturn(Status.SUCCESSFUL);

        Assertions.assertEquals(
                "SUCCESSFUL",
                statementProcessorService.handleRespone(jobExecution).getResult());
    }
}
