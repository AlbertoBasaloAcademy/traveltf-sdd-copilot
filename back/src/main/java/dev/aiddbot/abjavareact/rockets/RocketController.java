package dev.aiddbot.abjavareact.rockets;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rockets")
public class RocketController {

  private final RocketService service;

  public RocketController(RocketService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<RocketResponse> create(@Valid @RequestBody RocketRequest request) {
    RocketResponse created = service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping
  public List<RocketResponse> list() {
    return service.list();
  }

  @GetMapping("/{id}")
  public RocketResponse getById(@PathVariable String id) {
    return service.getById(id);
  }

  @PutMapping("/{id}")
  public RocketResponse update(@PathVariable String id, @Valid @RequestBody RocketRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> decommission(@PathVariable String id) {
    service.decommission(id);
    return ResponseEntity.noContent().build();
  }
}
