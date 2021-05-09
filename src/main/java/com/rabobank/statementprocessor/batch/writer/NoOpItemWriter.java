package com.rabobank.statementprocessor.batch.writer;

import com.rabobank.statementprocessor.domain.model.Record;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The writer can be used to write the successful or failed records
 * to a meesaging queue or a DB as per requirement.
 */

@Slf4j
public class NoOpItemWriter implements org.springframework.batch.item.ItemWriter<List<Record>> {


    @Override
    public void write(List<? extends List<Record>> list) throws Exception {
    }
}
