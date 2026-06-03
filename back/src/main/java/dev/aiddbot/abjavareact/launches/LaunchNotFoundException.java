package dev.aiddbot.abjavareact.launches;

public class LaunchNotFoundException extends RuntimeException {

  public LaunchNotFoundException(String id) {
    super("Launch not found: " + id);
  }
}
