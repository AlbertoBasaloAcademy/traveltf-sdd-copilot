package dev.aiddbot.abjavareact.bookings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

  @Id
  private String id;

  @Column(name = "launch_id", nullable = false)
  private String launchId;

  @Column(name = "passenger_name", nullable = false)
  private String passengerName;

  @Column(name = "passenger_email", nullable = false)
  private String passengerEmail;

  @Column(name = "passenger_phone", nullable = false)
  private String passengerPhone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BookingStatus status;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  protected Booking() {
  }

  public Booking(String launchId, String passengerName, String passengerEmail, String passengerPhone) {
    this.id = UUID.randomUUID().toString();
    this.launchId = launchId;
    this.passengerName = passengerName;
    this.passengerEmail = passengerEmail;
    this.passengerPhone = passengerPhone;
    this.status = BookingStatus.CREATED;
    this.createdAt = OffsetDateTime.now();
  }

  public String getId() {
    return id;
  }

  public String getLaunchId() {
    return launchId;
  }

  public String getPassengerName() {
    return passengerName;
  }

  public String getPassengerEmail() {
    return passengerEmail;
  }

  public String getPassengerPhone() {
    return passengerPhone;
  }

  public BookingStatus getStatus() {
    return status;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void cancel() {
    this.status = BookingStatus.CANCELLED;
    this.updatedAt = OffsetDateTime.now();
  }
}
