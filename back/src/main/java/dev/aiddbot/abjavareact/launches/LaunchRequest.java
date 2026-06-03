package dev.aiddbot.abjavareact.launches;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record LaunchRequest(
    @NotBlank(message = "rocket_id is required") String rocketId,
    @NotNull(message = "launch_time is required") @FutureOrPresent(message = "launch_time must be in the present or future") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime launchTime,
    @Min(value = 1, message = "ticket_price must be at least 1") int ticketPrice,
    @Min(value = 1, message = "minimum_occupancy must be at least 1") int minimumOccupancy
) {
}
