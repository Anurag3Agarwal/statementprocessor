package com.rabobank.statementprocessor.batch.processor;

import com.rabobank.statementprocessor.domain.Status;
import com.rabobank.statementprocessor.domain.error.ErrorRecord;
import com.rabobank.statementprocessor.domain.model.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Validator Processor validates the records as per given requirements Duplicate References Valdated
 * End Balance Validated
 */
@Slf4j
public class RecordProcessor implements ItemProcessor<List<Record>, List<Record>> {

    private JobExecution jobExecution;

    private Predicate<Record> isValidEndBalance =
            record ->
                    record.getStartBalance().add(record.getMutation()).compareTo(record.getEndBalance()) == 0;

    private BiPredicate<Set<Long>, Record> isValidReference =
            (set, record) -> set.add(record.getTransactionReference());

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    private boolean isErrorRecord(Set<Long> setReferences, Record r) {
        boolean isValidReferenceRecord = isValidReference.test(setReferences, r);

        r.setUniquetransactionReference(isValidReferenceRecord);

        return isValidEndBalance.negate().test(r) || !isValidReferenceRecord;
    }

    @Override
    public List<Record> process(List<Record> records) throws Exception {
        log.info("In processor, number of records to be processed {}", records.size());
        Set<Long> uniqueReferences = new HashSet<>();
        List<Record> invalidRecords =
                records.stream()
                        .filter(r -> isErrorRecord(uniqueReferences, r))
                        .collect(Collectors.toList());
        List<ErrorRecord> errorRecords =
                invalidRecords.stream()
                        .map(r -> new ErrorRecord(r.getTransactionReference().toString(), r.getAccountNumber()))
                        .collect(Collectors.toList());
        boolean allEndBalanceIncorrectRecords =
                invalidRecords.stream().allMatch(r -> r.isUniquetransactionReference());
        boolean allRecordReferenceMismatch =
                invalidRecords.stream().allMatch(r -> !r.isUniquetransactionReference());
        Status status = Status.SUCCESSFUL;
        if (invalidRecords.size() > 0) {
            status =
                    allEndBalanceIncorrectRecords
                            ? Status.INCORRECT_END_BALANCE
                            : (allRecordReferenceMismatch
                            ? Status.DUPLICATE_REFERENCE
                            : Status.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
        }
        jobExecution.getExecutionContext().put("Failed_records", errorRecords);
        jobExecution.getExecutionContext().put("status", status);
        List validRecords = new ArrayList(records);
        validRecords.removeIf(record -> invalidRecords.contains(record));
        return validRecords;
    }
}
