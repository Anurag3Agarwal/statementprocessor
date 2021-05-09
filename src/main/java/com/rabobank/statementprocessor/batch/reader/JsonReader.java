package com.rabobank.statementprocessor.batch.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.statementprocessor.domain.model.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class JsonReader implements ItemReader<List<Record>> {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("#{jobParameters['fileName']}")
    private String filename;

    private boolean readExecution;

    @Override
    public List<Record> read()
            throws Exception {

        if (readExecution) {
            return null;
        }
        log.info("Reading the file to validate");
        Resource res = resourceLoader.getResource("classpath:".concat(filename));
        Record[] users = objectMapper.readValue(res.getFile(), Record[].class);
        readExecution = true;

        return Arrays.asList(users);
    }
}
