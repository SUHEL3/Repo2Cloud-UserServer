package org.example.repo2cloud.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repo2cloud.dto.build_server_DTO.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Centralized exception handler for the User Server.
 *
 * <p>Translates communication failures and error responses from the Build Server
 * into structured {@link ApiErrorResponse} payloads without exposing raw stack traces.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler() {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper().findAndRegisterModules();
    }

    /**
     * Handles HTTP 4xx / 5xx responses returned by the Build Server.
     * Preserves the Build Server's HTTP status code and forwards its error payload
     * if available, or wraps the response into a structured ApiErrorResponse.
     */
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleRestClientResponseException(RestClientResponseException ex) {
        log.error("Build Server responded with error status {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());

        try {
            ApiErrorResponse buildServerResponse = objectMapper.readValue(
                    ex.getResponseBodyAsString(), ApiErrorResponse.class);
            if (buildServerResponse != null && buildServerResponse.getMessage() != null) {
                return ResponseEntity
                        .status(ex.getStatusCode())
                        .body(buildServerResponse);
            }
        } catch (Exception ignored) {
            // Not a JSON ApiErrorResponse; fall back to constructing one
        }

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatusCode().value())
                .error(ex.getStatusText())
                .message(ex.getResponseBodyAsString().isBlank() ? ex.getMessage() : ex.getResponseBodyAsString())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    /**
     * Handles connection refused, connect timeouts, and read timeouts
     * when contacting the Build Server.
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceAccessException(ResourceAccessException ex) {
        log.error("Failed to communicate with Build Server (I/O error / timeout / connection refused): {}", ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .error("Build Server Unavailable")
                .message("Unable to connect to the Build Server. Please ensure the Build Server is running on port 9090.")
                .details(List.of(ex.getMessage() != null ? ex.getMessage() : "Connection refused / timed out"))
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    /**
     * Handles explicit communication errors.
     */
    @ExceptionHandler(BuildServerCommunicationException.class)
    public ResponseEntity<ApiErrorResponse> handleBuildServerCommunicationException(BuildServerCommunicationException ex) {
        log.error("BuildServerCommunicationException: {}", ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatusCode())
                .error("Build Server Error")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    /**
     * Handles Jakarta Bean Validation errors on @Valid parameters.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid request payload")
                .details(details)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Catch-all fallback for uncaught runtime exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unhandled exception: ", ex);

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
