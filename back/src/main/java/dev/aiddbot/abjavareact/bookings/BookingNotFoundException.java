package dev.aiddbot.abjavareact.bookings;

public class BookingNotFoundException extends RuntimeException {
  public BookingNotFoundException(String id) {
    super("Booking not found: " + id);
  }
}
