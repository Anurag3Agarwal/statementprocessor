package com.rabobank.statementprocessor.batch.config;

import com.rabobank.statementprocessor.batch.processor.RecordProcessor;
import com.rabobank.statementprocessor.batch.reader.JsonReader;
import com.rabobank.statementprocessor.batch.writer.NoOpItemWriter;
import com.rabobank.statementprocessor.domain.model.Record;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Batch Configuration class for Spring Batch job
 */

@Configuration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public RecordProcessor validatorProcessor() {
        return new RecordProcessor();
    }

    @Bean
    @StepScope
    public JsonReader jsonItemReader() {
        return new JsonReader();
    }

    @Bean
    @StepScope
    public ItemWriter<List<Record>> itemWriter() {
        return new NoOpItemWriter();
    }

    @Bean
    public Step jsonStep() {
        return stepBuilderFactory
                .get("jsonStep")
                .<List<Record>, List<Record>>chunk(1)
                .reader(jsonItemReader())
                .processor(validatorProcessor())
                .writer(itemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job processTransactions() {
        return jobBuilderFactory
                .get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(jsonStep())
                .end()
                .build();
    }
}
