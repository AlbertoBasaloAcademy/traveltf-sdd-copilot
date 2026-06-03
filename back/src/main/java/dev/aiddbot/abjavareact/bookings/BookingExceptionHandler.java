package dev.aiddbot.abjavareact.bookings;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingExceptionHandler {

  @ExceptionHandler(BookingNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookingNotFound(BookingNotFoundException e) {
    ErrorResponse error = new ErrorResponse("BOOKING_NOT_FOUND", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(BookingValidationException.class)
  public ResponseEntity<ErrorResponse> handleBookingValidation(BookingValidationException e) {
    ErrorResponse error = new ErrorResponse("BOOKING_VALIDATION_ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  public record ErrorResponse(String code, String message) {
  }
}
