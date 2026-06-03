package dev.aiddbot.abjavareact.launches;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/launches")
public class LaunchController {

  private static final Logger LOG = LoggerFactory.getLogger(LaunchController.class);

  private final LaunchService service;

  public LaunchController(LaunchService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<LaunchResponse> create(@Valid @RequestBody LaunchRequest request) {
    LOG.info("Create launch requested: rocketId={}, launchTime={}, ticketPrice={}, minimumOccupancy={}",
        request.rocketId(), request.launchTime(), request.ticketPrice(), request.minimumOccupancy());
    LaunchResponse response = service.create(request);
    LOG.info("Launch created: id={}, rocketId={}, status={}", response.id(), response.rocketId(), response.status());
    return ResponseEntity.created(URI.create("/api/launches/" + response.id())).body(response);
  }

  @GetMapping
  public List<LaunchResponse> list() {
    return service.listAll();
  }

  @GetMapping("/{id}")
  public LaunchResponse getById(@PathVariable String id) {
    return service.getById(id);
  }

  @PatchMapping("/{id}/status")
  public LaunchResponse transitionStatus(@PathVariable String id, @Valid @RequestBody LaunchStatusRequest request) {
    return service.transitionStatus(id, request.status());
  }
}
