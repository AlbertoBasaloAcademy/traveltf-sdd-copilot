package dev.aiddbot.abjavareact.rockets;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RocketRequest(
    @NotBlank(message = "name is required") String name,
    @Min(value = 1, message = "capacity must be between 1 and 9")
    @Max(value = 9, message = "capacity must be between 1 and 9")
    int capacity,
    @NotNull(message = "range is required") RocketRange range
) {
}
