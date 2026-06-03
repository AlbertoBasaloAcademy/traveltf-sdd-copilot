package dev.aiddbot.abjavareact.launches;

import jakarta.validation.constraints.NotNull;

public record LaunchStatusRequest(@NotNull(message = "status is required") LaunchStatus status) {
}
