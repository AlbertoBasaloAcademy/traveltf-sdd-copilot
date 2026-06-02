package dev.aiddbot.abjavareact.rockets;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RocketExceptionHandler {

  @ExceptionHandler(RocketNotFoundException.class)
  ResponseEntity<ApiError> notFound(RocketNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage(), Map.of()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
    Map<String, String> fields = new LinkedHashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));
    return ResponseEntity.badRequest().body(new ApiError("Validation failed", fields));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException ex) {
    if (ex.getCause() instanceof InvalidFormatException) {
      return ResponseEntity.badRequest().body(new ApiError("Invalid payload format", Map.of()));
    }
    return ResponseEntity.badRequest().body(new ApiError("Malformed JSON payload", Map.of()));
  }
}
