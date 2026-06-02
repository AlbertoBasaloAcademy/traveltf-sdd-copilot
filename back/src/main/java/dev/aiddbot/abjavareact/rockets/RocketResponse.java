package dev.aiddbot.abjavareact.rockets;

public record RocketResponse(String id, String name, int capacity, RocketRange range, boolean decommissioned) {
}
