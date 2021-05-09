package com.rabobank.statementprocessor.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public enum Status {
    SUCCESSFUL,
    DUPLICATE_REFERENCE,
    INCORRECT_END_BALANCE,
    DUPLICATE_REFERENCE_INCORRECT_END_BALANCE
}
