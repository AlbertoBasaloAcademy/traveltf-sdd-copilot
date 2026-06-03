package dev.aiddbot.abjavareact.bookings;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private BookingService bookingService;

  @Test
  void createBooking_shouldReturn201() throws Exception {
    String launchId = "launch-123";
    BookingRequest request = new BookingRequest(
        "John Doe",
        "john@example.com",
        "+1-555-123-4567"
    );

    BookingResponse response = new BookingResponse(
        "booking-123",
        launchId,
        "John Doe",
        "john@example.com",
        "+1-555-123-4567",
        "CREATED",
        null
    );

    org.mockito.Mockito.when(bookingService.createBooking(launchId, request))
        .thenReturn(response);

    mockMvc.perform(post("/api/launches/{launchId}/bookings", launchId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("booking-123"))
        .andExpect(jsonPath("$.passengerName").value("John Doe"));
  }

  @Test
  void getBooking_shouldReturn200() throws Exception {
    String bookingId = "booking-123";
    BookingResponse response = new BookingResponse(
        bookingId,
        "launch-123",
        "John Doe",
        "john@example.com",
        "+1-555-123-4567",
        "CREATED",
        null
    );

    org.mockito.Mockito.when(bookingService.getBookingById(bookingId))
        .thenReturn(response);

    mockMvc.perform(get("/api/bookings/{bookingId}", bookingId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookingId))
        .andExpect(jsonPath("$.passengerName").value("John Doe"));
  }

  @Test
  void getBooking_withNonExistentId_shouldReturn404() throws Exception {
    String bookingId = "non-existent";

    org.mockito.Mockito.when(bookingService.getBookingById(bookingId))
        .thenThrow(new BookingNotFoundException(bookingId));

    mockMvc.perform(get("/api/bookings/{bookingId}", bookingId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("BOOKING_NOT_FOUND"));
  }
}
