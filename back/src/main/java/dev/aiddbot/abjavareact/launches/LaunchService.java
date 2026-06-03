
package dev.aiddbot.abjavareact.launches;

import dev.aiddbot.abjavareact.rockets.Rocket;
import dev.aiddbot.abjavareact.rockets.RocketNotFoundException;
import dev.aiddbot.abjavareact.rockets.RocketRepository;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaunchService {

  private final LaunchRepository repository;
  private final RocketRepository rocketRepository;

  private static final Logger LOG = LoggerFactory.getLogger(LaunchService.class);

  public LaunchService(LaunchRepository repository, RocketRepository rocketRepository) {
    this.repository = repository;
    this.rocketRepository = rocketRepository;
  }

  @Transactional
  public LaunchResponse create(LaunchRequest request) {
    LOG.debug("Creating launch: {}", request);

    // Convert LocalDateTime from request into OffsetDateTime using server default zone
    LocalDateTime localLaunchTime = request.launchTime();
    OffsetDateTime launchTime = localLaunchTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();

    OffsetDateTime now = OffsetDateTime.now();
    if (launchTime.isBefore(now)) {
      LOG.warn("Rejected create: launch_time {} is before now {}", launchTime, now);
      throw new LaunchValidationException("launch_time must be present or future");
    }

    Rocket rocket;
    try {
      rocket = rocketRepository.findById(request.rocketId()).orElseThrow(() -> new RocketNotFoundException(request.rocketId()));
    } catch (RocketNotFoundException ex) {
      LOG.warn("Rejected create: rocket not found {}", request.rocketId());
      throw ex;
    }

    if (request.minimumOccupancy() > rocket.getCapacity()) {
      LOG.warn("Rejected create: minimumOccupancy {} > rocket.capacity {}", request.minimumOccupancy(), rocket.getCapacity());
      throw new LaunchValidationException("minimum_occupancy cannot exceed rocket capacity");
    }

    Launch launch = new Launch(request.rocketId(), launchTime, request.ticketPrice(), request.minimumOccupancy());
    Launch saved = repository.save(launch);
    LOG.info("Persisted launch id={} rocketId={} launchTime={}", saved.getId(), saved.getRocketId(), saved.getLaunchTime());
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<LaunchResponse> listAll() {
    return repository.findAllByOrderByLaunchTimeAsc().stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public LaunchResponse getById(String id) {
    return toResponse(findOrThrow(id));
  }

  @Transactional
  public LaunchResponse transitionStatus(String id, LaunchStatus newStatus) {
    Launch launch = findOrThrow(id);
    LaunchStatus current = launch.getStatus();

    if (current == LaunchStatus.COMPLETED || current == LaunchStatus.CANCELLED) {
      throw new LaunchStateException("Cannot modify completed or cancelled launch");
    }

    boolean allowed = switch (current) {
      case CREATED -> newStatus == LaunchStatus.CONFIRMED || newStatus == LaunchStatus.CANCELLED;
      case CONFIRMED -> newStatus == LaunchStatus.COMPLETED || newStatus == LaunchStatus.CANCELLED;
      default -> false;
    };

    if (!allowed) {
      throw new LaunchStateException("Invalid status transition from " + current + " to " + newStatus);
    }

    launch.setStatus(newStatus);
    Launch saved = repository.save(launch);
    return toResponse(saved);
  }

  private Launch findOrThrow(String id) {
    return repository.findById(id).orElseThrow(() -> new LaunchNotFoundException(id));
  }

  private LaunchResponse toResponse(Launch launch) {
    return new LaunchResponse(
        launch.getId(),
        launch.getRocketId(),
        launch.getLaunchTime(),
        launch.getTicketPrice(),
        launch.getMinimumOccupancy(),
        launch.getStatus(),
        launch.getCreatedAt(),
        launch.getUpdatedAt()
    );
  }
}
