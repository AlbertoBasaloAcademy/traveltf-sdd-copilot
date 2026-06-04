---
description: Rules to write good Java code
applyTo: '**/*.java' 
---


# Backend Java (Spring Boot)

Package-per-feature, layered slice: `*Controller` → `*Service` → `*Repository` → JPA entity, under `dev.aiddbot.abjavareact.{feature}`. Indent with 2 spaces.

## Layering
- **Controller** (`@RestController`, `@RequestMapping("/api/{plural}")`): only HTTP concerns. Validate input with `@Valid`, delegate to the service, map to `ResponseEntity`. No business logic.
- **Service** (`@Service`): owns business rules and `@Transactional` boundaries (`readOnly = true` for queries). Maps entities to `*Response` via a private `toResponse(...)`.
- **Repository**: Spring Data JPA interface.
- Use constructor injection (no `@Autowired` fields).

## DTOs
- Requests/responses are immutable `record`s: `*Request` (Jakarta Validation annotations), `*Response`.
- Never leak JPA entities through controllers — always map to `*Response`.

## Errors
- Throw domain exceptions: `*NotFoundException`, `*ValidationException`, `*StateException`.
- Map them to HTTP in a per-feature `@RestControllerAdvice` returning `ApiError`. Log rejections via SLF4J before throwing.

```java
// ✅ Service: business rules + transaction, map to response
@Transactional(readOnly = true)
public RocketResponse getById(String id) {
  return toResponse(findOrThrow(id));
}

private Rocket findOrThrow(String id) {
  return repository.findById(id).orElseThrow(() -> new RocketNotFoundException(id));
}
```

## Persistence
- Schema is owned by Flyway (`db/migration/V*__*.sql`); keep `ddl-auto: validate`.
- Map snake_case columns with `@Column(name = ...)`; store enums as strings.

## Testing
- Mirror the package tree with `*Test.java` per layer (controller, service, repository).

## Avoid
- Business logic in controllers; field injection; leaking entities; `ddl-auto` other than `validate`.
