package com.rabobank.statementprocessor.service;

import com.rabobank.statementprocessor.domain.model.StatementProcessorApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StatementProcessorService {
    StatementProcessorApiResponse validateAndProcessStatements(MultipartFile file) throws Throwable;
}
