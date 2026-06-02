package dev.aiddbot.abjavareact.rockets;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "rocket")
public class Rocket {

  @Id
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int capacity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RocketRange range;

  @Column(nullable = false)
  private boolean decommissioned;

  protected Rocket() {
  }

  public Rocket(String name, int capacity, RocketRange range) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.capacity = capacity;
    this.range = range;
    this.decommissioned = false;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCapacity() {
    return capacity;
  }

  public RocketRange getRange() {
    return range;
  }

  public boolean isDecommissioned() {
    return decommissioned;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setRange(RocketRange range) {
    this.range = range;
  }

  public void setDecommissioned(boolean decommissioned) {
    this.decommissioned = decommissioned;
  }
}
