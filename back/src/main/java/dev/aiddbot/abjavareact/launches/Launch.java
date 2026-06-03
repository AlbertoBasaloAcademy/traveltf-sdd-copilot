package dev.aiddbot.abjavareact.launches;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "launch")
public class Launch {

  @Id
  private String id;

  @Column(name = "rocket_id", nullable = false)
  private String rocketId;

  @Column(name = "launch_time", nullable = false)
  private OffsetDateTime launchTime;

  @Column(name = "ticket_price", nullable = false)
  private int ticketPrice;

  @Column(name = "minimum_occupancy", nullable = false)
  private int minimumOccupancy;

  @Column(nullable = false)
  private LaunchStatus status;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  protected Launch() {
  }

  public Launch(String rocketId, OffsetDateTime launchTime, int ticketPrice, int minimumOccupancy) {
    this.id = UUID.randomUUID().toString();
    this.rocketId = rocketId;
    this.launchTime = launchTime;
    this.ticketPrice = ticketPrice;
    this.minimumOccupancy = minimumOccupancy;
    this.status = LaunchStatus.CREATED;
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = this.createdAt;
  }

  public String getId() {
    return id;
  }

  public String getRocketId() {
    return rocketId;
  }

  public OffsetDateTime getLaunchTime() {
    return launchTime;
  }

  public int getTicketPrice() {
    return ticketPrice;
  }

  public int getMinimumOccupancy() {
    return minimumOccupancy;
  }

  public LaunchStatus getStatus() {
    return status;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setLaunchTime(OffsetDateTime launchTime) {
    this.launchTime = launchTime;
    touch();
  }

  public void setTicketPrice(int ticketPrice) {
    this.ticketPrice = ticketPrice;
    touch();
  }

  public void setMinimumOccupancy(int minimumOccupancy) {
    this.minimumOccupancy = minimumOccupancy;
    touch();
  }

  public void setStatus(LaunchStatus status) {
    this.status = status;
    touch();
  }

  private void touch() {
    this.updatedAt = OffsetDateTime.now();
  }
}
