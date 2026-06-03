package dev.aiddbot.abjavareact.bookings;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookingController {

  private final BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PostMapping("/launches/{launchId}/bookings")
  @ResponseStatus(HttpStatus.CREATED)
  public BookingResponse createBooking(@PathVariable String launchId, @RequestBody BookingRequest request) {
    return bookingService.createBooking(launchId, request);
  }

  @GetMapping("/launches/{launchId}/bookings")
  public List<BookingResponse> getBookingsByLaunch(@PathVariable String launchId) {
    return bookingService.getBookingsByLaunchId(launchId);
  }

  @GetMapping("/bookings/{bookingId}")
  public BookingResponse getBooking(@PathVariable String bookingId) {
    return bookingService.getBookingById(bookingId);
  }
}
