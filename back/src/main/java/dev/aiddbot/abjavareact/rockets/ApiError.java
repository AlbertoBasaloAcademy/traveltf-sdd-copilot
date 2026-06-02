package dev.aiddbot.abjavareact.rockets;

import java.util.Map;

public record ApiError(String message, Map<String, String> fieldErrors) {
}
