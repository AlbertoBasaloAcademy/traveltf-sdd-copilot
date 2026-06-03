package dev.aiddbot.abjavareact.bookings;

import java.time.OffsetDateTime;

public record BookingResponse(
    String id,
    String launchId,
    String passengerName,
    String passengerEmail,
    String passengerPhone,
    String status,
    OffsetDateTime createdAt
) {

  public static BookingResponse from(Booking booking) {
    return new BookingResponse(
        booking.getId(),
        booking.getLaunchId(),
        booking.getPassengerName(),
        booking.getPassengerEmail(),
        booking.getPassengerPhone(),
        booking.getStatus().name(),
        booking.getCreatedAt()
    );
  }
}
