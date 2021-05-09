package com.rabobank.statementprocessor.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
public class StatementProcessorServiceTest {

    @Autowired
    private StatementProcessorService service;

    @Test
    @SneakyThrows
    public void incorrectFileTypeTest() {
        MockMultipartFile file =
                new MockMultipartFile(
                        "file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> service.validateAndProcessStatements(file));
    }
}
