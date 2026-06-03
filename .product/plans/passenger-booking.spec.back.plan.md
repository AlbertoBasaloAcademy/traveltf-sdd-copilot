---
plan-type: spec
tier: back
---
# Spec - passenger-booking - back

## Specification

Implement the backend REST API and business logic for the passenger booking system. The system must handle booking creation, retrieval, and listing with proper validation, error handling, and persistence to the database.

**Context**: [passenger-booking.spec.md](../specs/passenger-booking.spec.md)

### Data model

**Booking entity** (Java class):
- `id`: UUID
- `launchId`: UUID (foreign key)
- `passengerName`: String (required, non-empty)
- `passengerEmail`: String (required, valid email format)
- `passengerPhone`: String (required, valid phone format)
- `status`: BookingStatus enum ('created', 'cancelled')
- `createdAt`: LocalDateTime (auto-set)
- `updatedAt`: LocalDateTime (nullable, updated on changes)

**Validation rules**:
- All passenger fields required
- Email must match standard email pattern
- Phone must match valid phone pattern
- Launch must exist when booking is created

## Implementation Steps

### Step 1: Create Booking entity and value objects
Define the Booking JPA entity, status enum, and supporting value objects.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/`
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/model/`
- [ ] Create `BookingStatus.java` enum with values: CREATED, CANCELLED
- [ ] Create `Booking.java` JPA entity with `@Entity @Table(name = "bookings")`
- [ ] Add UUID id field with `@Id @GeneratedValue(strategy = GenerationType.UUID)`
- [ ] Add launchId field as foreign key to launches table
- [ ] Add passengerName, passengerEmail, passengerPhone fields with @NotBlank annotations
- [ ] Add status field with @Enumerated(EnumType.STRING) and default value CREATED
- [ ] Add createdAt and updatedAt fields with @CreationTimestamp and @UpdateTimestamp
- [ ] Implement equals, hashCode, and toString methods

### Step 2: Create Booking repository
Define the JPA repository interface for database access.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/repository/`
- [ ] Create `BookingRepository.java` extending `JpaRepository<Booking, UUID>`
- [ ] Add custom query method `findByLaunchId(UUID launchId)` with pagination support
- [ ] Add custom query method `findByLaunchIdOrderByCreatedAtDesc(UUID launchId, Pageable pageable)`
- [ ] Write unit tests in `BookingRepositoryTest.java`

### Step 3: Create booking validation and exception classes
Define custom exceptions and validation logic.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/exception/`
- [ ] Create `BookingNotFoundException.java` extending a domain exception
- [ ] Create `InvalidBookingDataException.java` for validation errors
- [ ] Create `LaunchNotFoundException.java` for missing launch references
- [ ] Create `BookingValidationService.java` with methods:
      - `validatePassengerEmail(String email): void` — throws InvalidBookingDataException
      - `validatePassengerPhone(String phone): void` — throws InvalidBookingDataException
      - `validateRequiredFields(CreateBookingRequest): void` — throws InvalidBookingDataException

### Step 4: Create Booking service layer
Implement business logic for booking operations.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/service/`
- [ ] Create `BookingService.java` with methods:
      - `createBooking(UUID launchId, CreateBookingRequest request): BookingResponse` — validates launch exists, validates passenger data, persists booking
      - `getBooking(UUID bookingId): BookingResponse` — retrieves booking or throws BookingNotFoundException
      - `getBookingsByLaunch(UUID launchId, Pageable pageable): Page<BookingResponse>` — retrieves all bookings for a launch with pagination
- [ ] Inject `BookingRepository`, `RocketService` (or similar to verify launch exists), and `BookingValidationService`
- [ ] Handle exceptions and translate to appropriate errors
- [ ] Write service tests in `BookingServiceTest.java` covering:
      - Happy path: create, retrieve, list bookings
      - Error cases: invalid data, non-existent launch, non-existent booking

### Step 5: Create DTOs and mappers
Define request/response objects and conversion logic.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/dto/`
- [ ] Create `CreateBookingRequest.java` with fields: passengerName, passengerEmail, passengerPhone
- [ ] Create `BookingResponse.java` with fields: id, launchId, passengerName, passengerEmail, passengerPhone, status, createdAt
- [ ] Create `BookingMapper.java` with:
      - `toResponse(Booking entity): BookingResponse`
      - `toEntity(UUID launchId, CreateBookingRequest request): Booking`

### Step 6: Create Booking controller
Implement REST API endpoints.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/bookings/controller/`
- [ ] Create `BookingController.java` with `@RestController @RequestMapping("/api/bookings")`
- [ ] Implement endpoint: `POST /api/launches/{launchId}/bookings` — creates booking, returns 201
- [ ] Implement endpoint: `GET /api/bookings/{bookingId}` — retrieves booking, returns 200 or 404
- [ ] Implement endpoint: `GET /api/launches/{launchId}/bookings` — lists bookings with pagination, returns 200 or 404
- [ ] Handle exceptions with appropriate HTTP status codes:
      - 400: Invalid request data
      - 404: Launch or booking not found
      - 409: Conflict (e.g., booking limit reached, if applicable)
- [ ] Write controller tests in `BookingControllerTest.java`

### Step 7: Add global exception handler mappings
Ensure booking exceptions are properly translated to HTTP responses.
- Paths:
    - `back/src/main/java/dev/aiddbot/abjavareact/config/` or existing exception handler
- [ ] Map `BookingNotFoundException` to 404
- [ ] Map `InvalidBookingDataException` to 400
- [ ] Map `LaunchNotFoundException` to 404
- [ ] Return consistent error response format

### Step 8: Integration testing
Create end-to-end tests for booking endpoints.
- Paths:
    - `back/src/test/java/dev/aiddbot/abjavareact/bookings/`
- [ ] Write integration tests for all three endpoints
- [ ] Test valid bookings creation and retrieval
- [ ] Test validation error responses
- [ ] Test listing bookings with pagination
- [ ] Verify database state after operations
