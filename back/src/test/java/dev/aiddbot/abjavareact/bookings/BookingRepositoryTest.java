package dev.aiddbot.abjavareact.bookings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BookingRepositoryTest {

  @Autowired
  private BookingRepository bookingRepository;

  @Test
  void saveBooking_shouldPersistToDatabase() {
    Booking booking = new Booking(
        "launch-123",
        "John Doe",
        "john@example.com",
        "+1 555-123-4567"
    );

    Booking saved = bookingRepository.save(booking);

    assertNotNull(saved.getId());
    assertEquals("John Doe", saved.getPassengerName());
  }

  @Test
  void findByLaunchId_shouldReturnBookingsForLaunch() {
    String launchId = "launch-123";
    Booking booking1 = new Booking(launchId, "John Doe", "john@example.com", "+1 555-123-4567");
    Booking booking2 = new Booking(launchId, "Jane Smith", "jane@example.com", "+1 555-987-6543");

    bookingRepository.save(booking1);
    bookingRepository.save(booking2);

    List<Booking> bookings = bookingRepository.findByLaunchId(launchId);

    assertEquals(2, bookings.size());
  }

  @Test
  void findByIdAndLaunchId_shouldReturnBookingIfMatch() {
    String launchId = "launch-123";
    Booking booking = new Booking(launchId, "John Doe", "john@example.com", "+1 555-123-4567");
    Booking saved = bookingRepository.save(booking);

    var result = bookingRepository.findByIdAndLaunchId(saved.getId(), launchId);

    assert result.isPresent();
    assertEquals("John Doe", result.get().getPassengerName());
  }
}
