package dev.aiddbot.abjavareact.rockets;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RocketService {

  private final RocketRepository repository;

  public RocketService(RocketRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public RocketResponse create(RocketRequest request) {
    Rocket rocket = new Rocket(request.name().trim(), request.capacity(), request.range());
    Rocket saved = repository.save(rocket);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<RocketResponse> list() {
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public RocketResponse getById(String id) {
    return toResponse(findOrThrow(id));
  }

  @Transactional
  public RocketResponse update(String id, RocketRequest request) {
    Rocket rocket = findOrThrow(id);
    rocket.setName(request.name().trim());
    rocket.setCapacity(request.capacity());
    rocket.setRange(request.range());
    Rocket saved = repository.save(rocket);
    return toResponse(saved);
  }

  @Transactional
  public void decommission(String id) {
    Rocket rocket = findOrThrow(id);
    if (!rocket.isDecommissioned()) {
      rocket.setDecommissioned(true);
      repository.save(rocket);
    }
  }

  private Rocket findOrThrow(String id) {
    return repository.findById(id).orElseThrow(() -> new RocketNotFoundException(id));
  }

  private RocketResponse toResponse(Rocket rocket) {
    return new RocketResponse(
        rocket.getId(),
        rocket.getName(),
        rocket.getCapacity(),
        rocket.getRange(),
        rocket.isDecommissioned()
    );
  }
}
