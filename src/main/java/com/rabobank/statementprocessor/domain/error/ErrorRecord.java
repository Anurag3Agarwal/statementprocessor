package com.rabobank.statementprocessor.domain.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ErrorRecord {

    @NonNull
    private final String reference;

    @NonNull
    private final String accountNumber;
}
