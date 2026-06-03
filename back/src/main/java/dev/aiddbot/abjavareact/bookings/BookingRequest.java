package dev.aiddbot.abjavareact.bookings;

public record BookingRequest(
    String passengerName,
    String passengerEmail,
    String passengerPhone
) {
}
