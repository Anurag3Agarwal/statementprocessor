package com.rabobank.statementprocessor.controller.statementprocessor;

import com.rabobank.statementprocessor.domain.model.StatementProcessorApiResponse;
import com.rabobank.statementprocessor.service.StatementProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("customer-statements")
@Slf4j
public class StatementProcessorController {

    @Autowired
    StatementProcessorService statementProcessorService;

    /**
     * Rabobank receives monthly deliveries of customer statement records. This information is
     * delivered in JSON format. These records need to be validated.
     *
     * @param file
     * @return StatementProcessorApiResponse
     */

    @PostMapping(
            path = "/v1/process",
            consumes = {"multipart/form-data"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatementProcessorApiResponse processData(@RequestParam("file") MultipartFile file)
            throws Throwable {
        log.info(
                "File validation started for file {} with content type {}",
                file.getOriginalFilename(),
                file.getContentType());

        return statementProcessorService.validateAndProcessStatements(file);
    }
}
