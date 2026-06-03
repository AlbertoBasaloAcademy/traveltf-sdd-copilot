package dev.aiddbot.abjavareact.bookings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock
  private BookingRepository bookingRepository;

  private BookingService bookingService;

  @BeforeEach
  void setUp() {
    bookingService = new BookingService(bookingRepository);
  }

  @Test
  void createBooking_shouldCreateAndReturnBooking() {
    String launchId = "launch-123";
    BookingRequest request = new BookingRequest(
        "John Doe",
        "john@example.com",
        "+1 555-123-4567"
    );

    Booking booking = new Booking(launchId, request.passengerName(), 
        request.passengerEmail(), request.passengerPhone());
    when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

    BookingResponse response = bookingService.createBooking(launchId, request);

    assertNotNull(response.id());
    assertEquals(launchId, response.launchId());
    assertEquals("John Doe", response.passengerName());
    assertEquals("john@example.com", response.passengerEmail());
    verify(bookingRepository).save(any(Booking.class));
  }

  @Test
  void createBooking_withInvalidEmail_shouldThrowException() {
    String launchId = "launch-123";
    BookingRequest request = new BookingRequest(
        "John Doe",
        "invalid-email",
        "+1 555-123-4567"
    );

    assertThrows(BookingValidationException.class, 
        () -> bookingService.createBooking(launchId, request));
  }

  @Test
  void createBooking_withInvalidPhone_shouldThrowException() {
    String launchId = "launch-123";
    BookingRequest request = new BookingRequest(
        "John Doe",
        "john@example.com",
        "123"
    );

    assertThrows(BookingValidationException.class, 
        () -> bookingService.createBooking(launchId, request));
  }

  @Test
  void createBooking_withBlankName_shouldThrowException() {
    String launchId = "launch-123";
    BookingRequest request = new BookingRequest(
        "",
        "john@example.com",
        "+1 555-123-4567"
    );

    assertThrows(BookingValidationException.class, 
        () -> bookingService.createBooking(launchId, request));
  }

  @Test
  void getBookingById_shouldReturnBooking() {
    String bookingId = "booking-123";
    Booking booking = new Booking("launch-123", "John Doe", "john@example.com", "+1 555-123-4567");
    when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

    BookingResponse response = bookingService.getBookingById(bookingId);

    assertEquals("John Doe", response.passengerName());
  }

  @Test
  void getBookingById_withNonExistentId_shouldThrowException() {
    String bookingId = "non-existent";
    when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

    assertThrows(BookingNotFoundException.class, 
        () -> bookingService.getBookingById(bookingId));
  }
}
