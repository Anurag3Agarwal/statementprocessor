# Customer Statement Processor
Bank receives monthly deliveries of customer statement records. This information is delivered in JSON Format.
These records need to be validated.

## About the application
The application accepts JSON files as POST body, processed the file, validates the JSON and returns the JSON output with message and HTTP status codes as per the
assignment.

## Steps to run the application

Clone the application using the URL https://github.com/Anurag3Agarwal/statementprocessor.git

Build the Application using Maven `mvn clean install`

Run Unit Test `mvn clean test`

For running the application `mvn spring-boot:run`

## How to test

Download the scenarios file from the resource folder, pass it as the body of POST request http://localhost:8080/customer-statements/v1/process with key file and value as one of the files downloaded via tools like POSTMAN. Json Response with status and error records, if any, is rendered.

Optional : H2 console is available at http://localhost:8080/h2-console/login.do to verify the 

## Design Decisions

1) Use Spring Batch with REST to take advantage of the framework for batch file processing and inline with separation of concern design principle.
2) Monthly deliverables are assumed to be files.
3) The solution is scalable and audit for jobs and invalid jobs are kept at BATCH_JOB_EXECUTION_CONTEXT, another advantage of using Spring Batch.

## Improvement Points
1) In production like environment, it is better to read from a NFS location instead of passing the file as POST request.
2) For huge files, it would be better to make use of taskexecutor in Batch Step.
3) We can integrate the application with Grafana and ELK for better monitoring.
4) In memory DB is used in the project for simplicity, in higher enviroments we can use an enterprise offering. 
