package dev.aiddbot.abjavareact.rockets;

public class RocketNotFoundException extends RuntimeException {

  public RocketNotFoundException(String id) {
    super("Rocket not found: " + id);
  }
}
