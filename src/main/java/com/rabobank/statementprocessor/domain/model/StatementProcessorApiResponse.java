package com.rabobank.statementprocessor.domain.model;

import com.rabobank.statementprocessor.domain.error.ErrorRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class StatementProcessorApiResponse {
    @NonNull
    private final String result;

    private final List<ErrorRecord> errorRecords;
}
