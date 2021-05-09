package com.rabobank.statementprocessor.batch.processor;

import com.rabobank.statementprocessor.domain.model.Record;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class RecordProcessorTest {

    @InjectMocks
    RecordProcessor recordProcessor;

    @Mock
    JobExecution jobExecution;

    @Test
    @SneakyThrows
    public void processTestAllRecordsValid() {

        Mockito.when(jobExecution.getExecutionContext())
                .thenReturn(Mockito.mock(ExecutionContext.class
                ));
        Assertions.assertEquals(
                1,
                recordProcessor.process(
                        Stream.of(
                                recordBuilder()
                                        .transactionReference(1212L)
                                        .mutation(BigDecimal.valueOf(100))
                                        .endBalance(BigDecimal.valueOf(1100))
                                        .build()).collect(Collectors.toList())).size());
    }


    @Test
    @SneakyThrows
    public void processTestIncorrectEndBalance() {

        Mockito.when(jobExecution.getExecutionContext())
                .thenReturn(Mockito.mock(ExecutionContext.class
                ));
        Assertions.assertEquals(
                0,
                recordProcessor.process(
                        Stream.of(
                                recordBuilder()
                                        .transactionReference(1212L)
                                        .mutation(BigDecimal.valueOf(10))
                                        .endBalance(BigDecimal.valueOf(100))
                                        .build()).collect(Collectors.toList())).size());
    }

    @Test
    @SneakyThrows
    public void processTestDuplicateReference() {

        Mockito.when(jobExecution.getExecutionContext())
                .thenReturn(Mockito.mock(ExecutionContext.class));
        Assertions.assertEquals(
                1,
                recordProcessor
                        .process(
                                Stream.of(
                                        recordBuilder()
                                                .transactionReference(1212L)
                                                .mutation(BigDecimal.valueOf(100))
                                                .endBalance(BigDecimal.valueOf(1100))
                                                .build(),
                                        recordBuilder()
                                                .transactionReference(1212L)
                                                .mutation(BigDecimal.valueOf(100))
                                                .endBalance(BigDecimal.valueOf(1100))
                                                .build())
                                        .collect(Collectors.toList()))
                        .size());
    }

    @Test
    @SneakyThrows
    public void processTestDuplicateReferenceAndIncorrectBalance() {

        Mockito.when(jobExecution.getExecutionContext())
                .thenReturn(Mockito.mock(ExecutionContext.class));
        Assertions.assertEquals(
                0,
                recordProcessor
                        .process(
                                Stream.of(
                                        recordBuilder()
                                                .transactionReference(1212L)
                                                .mutation(BigDecimal.valueOf(100))
                                                .endBalance(BigDecimal.valueOf(1000))
                                                .build(),
                                        recordBuilder()
                                                .transactionReference(1212L)
                                                .mutation(BigDecimal.valueOf(100))
                                                .endBalance(BigDecimal.valueOf(1100))
                                                .build())
                                        .collect(Collectors.toList()))
                        .size());
    }

    private Record.RecordBuilder recordBuilder() {
        return Record.builder().description("test").accountNumber("1234").startBalance(BigDecimal.valueOf(1000));
    }

}
