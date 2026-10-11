package org.example.repo2cloud.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.repo2cloud.dto.build_server_DTO.ApiErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        exceptionHandler = new GlobalExceptionHandler(objectMapper);
    }

    @Test
    @DisplayName("handleRestClientResponseException translates Build Server error response")
    void testHandleRestClientResponseException() {
        String jsonBody = """
                {
                    "status": 404,
                    "error": "Not Found",
                    "message": "Deployment with ID 123 not found"
                }
                """;
        HttpClientErrorException notFound = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                HttpHeaders.EMPTY,
                jsonBody.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleRestClientResponseException(notFound);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Deployment with ID 123 not found");
    }

    @Test
    @DisplayName("handleResourceAccessException translates connection refused / timeout to 503 SERVICE_UNAVAILABLE")
    void testHandleResourceAccessException() {
        ResourceAccessException ex = new ResourceAccessException("Connection refused: connect");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleResourceAccessException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Build Server Unavailable");
        assertThat(response.getBody().getMessage()).contains("Please ensure the Build Server is running on port 9090");
    }

    @Test
    @DisplayName("handleBuildServerCommunicationException returns specified status code")
    void testHandleBuildServerCommunicationException() {
        BuildServerCommunicationException ex = new BuildServerCommunicationException("Custom build failure", 502);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleBuildServerCommunicationException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(502);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Custom build failure");
    }
}
