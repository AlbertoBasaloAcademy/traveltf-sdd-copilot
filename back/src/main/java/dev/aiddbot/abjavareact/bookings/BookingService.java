package dev.aiddbot.abjavareact.bookings;

import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Za-z0-9+_.-]+@(.+)$"
  );
  private static final Pattern PHONE_PATTERN = Pattern.compile(
      "^[+]?[\\d\\s.\\-()]{9,}$"
  );

  public BookingService(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  public BookingResponse createBooking(String launchId, BookingRequest request) {
    validateBookingRequest(request);
    
    Booking booking = new Booking(
        launchId,
        request.passengerName(),
        request.passengerEmail(),
        request.passengerPhone()
    );
    
    Booking saved = bookingRepository.save(booking);
    return BookingResponse.from(saved);
  }

  public List<BookingResponse> getBookingsByLaunchId(String launchId) {
    return bookingRepository.findByLaunchId(launchId)
        .stream()
        .map(BookingResponse::from)
        .toList();
  }

  public BookingResponse getBookingById(String bookingId) {
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new BookingNotFoundException(bookingId));
    return BookingResponse.from(booking);
  }

  public void validateBookingRequest(BookingRequest request) {
    if (request.passengerName() == null || request.passengerName().isBlank()) {
      throw new BookingValidationException("Passenger name is required");
    }
    
    if (!EMAIL_PATTERN.matcher(request.passengerEmail()).matches()) {
      throw new BookingValidationException("Invalid email format: " + request.passengerEmail());
    }
    
    if (!PHONE_PATTERN.matcher(request.passengerPhone()).matches()) {
      throw new BookingValidationException("Invalid phone format: " + request.passengerPhone());
    }
  }
}
