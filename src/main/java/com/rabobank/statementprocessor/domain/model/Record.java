package com.rabobank.statementprocessor.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Record {
    @NonNull
    private Long transactionReference;
    @NonNull
    private String accountNumber;
    @NonNull
    private BigDecimal startBalance;
    @NonNull
    private BigDecimal mutation;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal endBalance;
    @JsonIgnore
    private boolean isUniquetransactionReference;

    private Record() {
    }
}
