package dev.aiddbot.abjavareact.rockets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RocketRange {
  EARTH("Earth"),
  MOON("Moon"),
  MARS("Mars");

  private final String value;

  RocketRange(String value) {
    this.value = value;
  }

  @JsonValue
  public String value() {
    return value;
  }

  @JsonCreator
  public static RocketRange fromValue(String raw) {
    if (raw == null) {
      return null;
    }
    for (RocketRange candidate : values()) {
      if (candidate.value.equalsIgnoreCase(raw) || candidate.name().equalsIgnoreCase(raw)) {
        return candidate;
      }
    }
    throw new IllegalArgumentException("Unsupported range: " + raw);
  }
}
