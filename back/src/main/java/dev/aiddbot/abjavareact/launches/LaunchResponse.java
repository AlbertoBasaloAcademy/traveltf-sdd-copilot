package dev.aiddbot.abjavareact.launches;

import java.time.OffsetDateTime;

public record LaunchResponse(
    String id,
    String rocketId,
    OffsetDateTime launchTime,
    int ticketPrice,
    int minimumOccupancy,
    LaunchStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
}
