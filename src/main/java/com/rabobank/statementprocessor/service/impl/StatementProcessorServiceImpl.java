package com.rabobank.statementprocessor.service.impl;

import com.rabobank.statementprocessor.domain.error.ErrorRecord;
import com.rabobank.statementprocessor.domain.model.StatementProcessorApiResponse;
import com.rabobank.statementprocessor.service.StatementProcessorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
@AllArgsConstructor
public class StatementProcessorServiceImpl implements StatementProcessorService {

    @Autowired
    private JobLauncher jobLauncher;

    private Job job;
    /**
     * Predicate to validate the content type
     */
    private final Predicate<MultipartFile> validateFileContentType =
            file -> file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE);

    @Override
    public StatementProcessorApiResponse validateAndProcessStatements(MultipartFile file)
            throws Throwable {
        validateAnduploadFileForBatchProcessing(file);
        JobParameters params =
                new JobParametersBuilder()
                        .addString("JobID", String.valueOf(System.currentTimeMillis()))
                        .addString("fileName", file.getOriginalFilename())
                        .toJobParameters();

        return handleRespone(jobLauncher.run(job, params));
    }

    /**
     * To handle the response scenarios
     *
     * @param run
     * @return StatementProcessorApiResponse
     * @throws Throwable
     */
    public StatementProcessorApiResponse handleRespone(JobExecution run) throws Throwable {
        if (run.getStatus() == BatchStatus.COMPLETED) {
            List<ErrorRecord> failedRecords =
                    (List<ErrorRecord>) run.getExecutionContext().get("Failed_records");
            String status = run.getExecutionContext().get("status").toString();
            return StatementProcessorApiResponse.builder()
                    .result(status)
                    .errorRecords(failedRecords)
                    .build();
        } else {
            throw run.getStepExecutions()
                    .parallelStream()
                    .map(stepExecution -> stepExecution.getFailureExceptions().get(0))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    /**
     * Method to upload file to the path specified for further processing
     *
     * @param file
     * @throws IOException
     */
    private void validateAnduploadFileForBatchProcessing(MultipartFile file) throws IOException {
        if (validateFileContentType.test(file)) {
            String path =
                    new File("src/main/resources").getCanonicalPath() + "/" + file.getOriginalFilename();
            File newfile = new File(path);

            newfile.createNewFile();
            file.transferTo(newfile);

        } else {
            throw new IllegalArgumentException("Content Type Not Supported");
        }
    }
}
