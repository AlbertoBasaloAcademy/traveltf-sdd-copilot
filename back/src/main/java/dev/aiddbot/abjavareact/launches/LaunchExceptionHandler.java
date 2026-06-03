package dev.aiddbot.abjavareact.launches;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.aiddbot.abjavareact.rockets.ApiError;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LaunchExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(LaunchExceptionHandler.class);

  @ExceptionHandler(LaunchNotFoundException.class)
  ResponseEntity<ApiError> notFound(LaunchNotFoundException ex) {
    LOG.warn("Launch not found: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage(), Map.of()));
  }

  @ExceptionHandler(LaunchStateException.class)
  ResponseEntity<ApiError> conflict(LaunchStateException ex) {
    LOG.warn("Launch state error: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage(), Map.of()));
  }

  @ExceptionHandler(LaunchValidationException.class)
  ResponseEntity<ApiError> validationFailure(LaunchValidationException ex) {
    LOG.warn("Launch validation failed: {}", ex.getMessage());
    return ResponseEntity.badRequest().body(new ApiError(ex.getMessage(), Map.of()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
    Map<String, String> fields = new LinkedHashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));
    LOG.warn("Method argument not valid: {}", fields);
    return ResponseEntity.badRequest().body(new ApiError("Validation failed", fields));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException ex) {
    LOG.error("Malformed or unreadable JSON payload", ex);
    if (ex.getCause() instanceof InvalidFormatException) {
      return ResponseEntity.badRequest().body(new ApiError("Invalid payload format", Map.of()));
    }
    return ResponseEntity.badRequest().body(new ApiError("Malformed JSON payload", Map.of()));
  }
}
