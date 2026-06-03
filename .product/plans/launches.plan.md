# Plan: Launches Feature Implementation

## TL;DR
Implement a complete launches scheduling system using the proven patterns from the rockets feature. This includes backend REST API (4 endpoints), database schema with rocket references, service-layer validation (status transitions, business rules), and frontend UI with create/edit/list views. Tests will follow the existing pyramid (unit tests for layers, NO E2E for full workflows). No modifications to existing rocket feature needed; launches simply reference rocket IDs.

---

## Steps

### **Phase 1: Backend Foundation** (Dependencies: none; Parallel: No)

1. **Create Launch Entity** — [back/src/main/java/dev/aiddbot/abjavareact/launches/Launch.java](back/src/main/java/dev/aiddbot/abjavareact/launches/Launch.java)
   - Reuse Rocket pattern: UUID id (generated in constructor), @Entity on launches table
   - Fields: rocket_id, launch_time, ticket_price, minimum_occupancy, status (enum), created_at, updated_at
   - Status enum: [LaunchStatus.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchStatus.java) with CREATED, CONFIRMED, COMPLETED, CANCELLED values
   - Add getter for rocket_id (no relationship loader; ID-based only per spec)

2. **Create DTOs** — Request & Response records
   - [LaunchRequest.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchRequest.java): @NotNull/@NotBlank for rocket_id, @FutureOrPresent on launch_time (validate future date), @Min(1) on ticket_price, @Min(1) on minimum_occupancy
   - [LaunchResponse.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchResponse.java): Read-only record with all fields

3. **Create Repository** — [back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchRepository.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchRepository.java)
   - Spring Data JPA interface: `JpaRepository<Launch, String>`
   - Add custom query method: `findAllOrderByLaunchTimeAsc()` to sort launches by time (for list endpoint)

4. **Create Service** — [back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchService.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchService.java)
   - Inject RocketService (or RocketRepository) for rocket existence validation
   - `create(LaunchRequest)`: Validate future launch_time, validate minimum_occupancy ≤ rocket capacity, persist with CREATED status
   - `listAll()`: Retrieve all launches ordered by launch_time, map to responses
   - `getById(String id)`: Retrieve by ID, throw LaunchNotFoundException if missing
   - `transitionStatus(String id, LaunchStatus newStatus)`: Validate state transitions (created → confirmed/cancelled, confirmed → completed/cancelled), reject modifications to completed/cancelled, throw LaunchStateException for invalid transitions
   - Private helper: `findOrThrow(id)`, `toResponse(launch)`, `validateRocketExists(rocket_id)`

5. **Create Exception Classes** — Custom domain exceptions
   - [LaunchNotFoundException.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchNotFoundException.java)
   - [LaunchStateException.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchStateException.java) — for invalid status transitions or modifications
   - [LaunchValidationException.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchValidationException.java) — for business rule violations (future date, capacity)

6. **Create Exception Handler** — [back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchExceptionHandler.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchExceptionHandler.java)
   - Reuse RocketExceptionHandler pattern: @RestControllerAdvice
   - Map LaunchNotFoundException → 404
   - Map LaunchStateException → 409 Conflict (can't modify completed/cancelled)
   - Map LaunchValidationException → 400 Bad Request
   - Map MethodArgumentNotValidException → 400 with field-level errors
   - Return ApiError (reuse from rockets package)

7. **Create Controller** — [back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchController.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchController.java)
   - @RestController @RequestMapping("/api/launches")
   - `POST /api/launches` — Create: @Valid LaunchRequest, delegate to service, return 201 with LaunchResponse
   - `GET /api/launches` — List all: service.listAll(), return 200 with array
   - `GET /api/launches/{id}` — Get by ID: service.getById(id), return 200
   - `PATCH /api/launches/{id}/status` — Transition: payload with "status" field, validate and transition via service, return 200

8. **Create Status Request DTO** — [LaunchStatusRequest.java](back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchStatusRequest.java)
   - Single field: status (enum), @NotNull

### **Phase 2: Backend Unit Tests** (Dependencies: Phase 1; Parallel: No)

9. **Test Repository** — [back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchRepositoryTest.java](back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchRepositoryTest.java)
   - @DataJpaTest + real SQLite test DB
   - Test save & retrieve cycle
   - Test findAllOrderByLaunchTimeAsc() returns correctly ordered results
   - Test findById() returns correct launch

10. **Test Service** — [back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchServiceTest.java](back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchServiceTest.java)
   - Mockito + JUnit 5 (mock RocketRepository, mock LaunchRepository)
   - Test create: validates future date (rejects past), validates rocket exists, validates minimum_occupancy ≤ capacity
   - Test listAll: returns sorted results
   - Test getById: throws LaunchNotFoundException for non-existent ID
   - Test transitionStatus: valid transitions (created→confirmed, confirmed→completed), invalid transitions throw LaunchStateException, completed/cancelled cannot be modified
   - Use ArgumentCaptor to verify persisted entity state

11. **Test Controller** — [back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchControllerTest.java](back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchControllerTest.java)
   - @WebMvcTest(LaunchController.class) with mocked LaunchService
   - Test POST /api/launches: valid request returns 201, invalid request returns 400, non-existent rocket returns appropriate error
   - Test GET /api/launches: returns 200 with array of launches
   - Test GET /api/launches/{id}: returns 200, 404 for non-existent ID
   - Test PATCH /api/launches/{id}/status: valid transition returns 200, invalid transition returns 400, completed launch returns 409

### **Phase 3: Frontend Foundation** (Dependencies: Phase 1 BE API working; Parallel: Yes with Phase 2)

12. **Create Type Definitions** — [front/src/shared/types/launch.ts](front/src/shared/types/launch.ts)
   - `LaunchStatus` type: 'created' | 'confirmed' | 'completed' | 'cancelled'
   - `Launch` interface: id, rocket_id, launch_time, ticket_price, minimum_occupancy, status, created_at, updated_at
   - `LaunchPayload` interface (for create/edit): rocket_id, launch_time, ticket_price, minimum_occupancy
   - `LaunchStatusPayload`: { status: LaunchStatus }

13. **Create API Layer** — [front/src/features/launches/launchesApi.ts](front/src/features/launches/launchesApi.ts)
   - Reuse httpClient pattern from rocketsApi
   - `getLaunches(): Promise<Launch[]>` — GET /api/launches
   - `createLaunch(payload: LaunchPayload): Promise<Launch>` — POST /api/launches
   - `getLaunchById(id: string): Promise<Launch>` — GET /api/launches/{id}
   - `transitionLaunchStatus(id: string, status: LaunchStatus): Promise<Launch>` — PATCH /api/launches/{id}/status with body { status }

14. **Create Custom Hook** — [front/src/features/launches/useLaunches.ts](front/src/features/launches/useLaunches.ts)
   - Inspired by useHealth (extraction of stateful logic, unlike RocketsFleet which is all in component)
   - State: launches (array), loading, error, submitting
   - Functions: loadLaunches(), createLaunch(payload), transitionStatus(id, status)
   - Load on mount with error handling

15. **Create Component** — [front/src/features/launches/LaunchesScheduler.tsx](front/src/features/launches/LaunchesScheduler.tsx)
   - Main UI component consuming useLaunches hook
   - Form section: rocket selector (fetch list from rocketsApi), datetime picker (launch_time), number inputs (ticket_price, minimum_occupancy)
   - Create/Edit mode (dual-mode form, reuse RocketsFleet pattern)
   - Launches list: table/grid displaying all launches with status badges
   - Actions: view details button, status transition button (dropdown or modal) — disabled for completed/cancelled
   - Error & loading states
   - Stylesheet: [front/src/features/launches/LaunchesScheduler.css](front/src/features/launches/LaunchesScheduler.css)

### **Phase 4: Frontend Tests** (Dependencies: Phase 3; Parallel: No)

16. **Test API Layer** — [front/src/features/launches/launchesApi.test.ts](front/src/features/launches/launchesApi.test.ts)
   - Mock httpClient module
   - Test getLaunches() calls GET /api/launches
   - Test createLaunch() calls POST with correct payload structure
   - Test getLaunchById(id) calls GET /api/launches/{id}
   - Test transitionLaunchStatus(id, status) calls PATCH with { status } body

17. **Test Hook** — [front/src/features/launches/useLaunches.test.ts](front/src/features/launches/useLaunches.test.ts)
   - renderHook with mocked launchesApi
   - Test loadLaunches: loads data, sets loading state, handles errors
   - Test createLaunch: submits payload, updates state
   - Test transitionStatus: calls API with correct status

18. **Test Component** — [front/src/features/launches/LaunchesScheduler.test.tsx](front/src/features/launches/LaunchesScheduler.test.tsx)
   - Mock entire launchesApi + rocketsApi modules
   - Test load on mount: displays launches
   - Test create form: fills fields, submits, appears in list
   - Test edit mode: loads existing launch data, updates
   - Test status transition: transitions from created→confirmed, confirms action, updates UI
   - Test error state: displays error message
   - Test loading state: shows spinner while fetching
   - Test completed/cancelled launches: status transition button disabled

## Relevant Files

**Backend** (to be created):
- `back/src/main/java/dev/aiddbot/abjavareact/launches/Launch.java` — Entity
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchStatus.java` — Status enum
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchRequest.java` — Request DTO
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchResponse.java` — Response DTO
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchStatusRequest.java` — Status transition DTO
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchRepository.java` — Repository
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchService.java` — Service (orchestrates validation, delegates to repo)
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchController.java` — REST controller (4 endpoints)
- `back/src/main/java/dev/aiddbot/abjavareact/launches/Launch*Exception.java` — Custom exceptions (3 classes)
- `back/src/main/java/dev/aiddbot/abjavareact/launches/LaunchExceptionHandler.java` — Exception mapper

**Backend Tests** (to be created):
- `back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchRepositoryTest.java`
- `back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchServiceTest.java`
- `back/src/test/java/dev/aiddbot/abjavareact/launches/LaunchControllerTest.java`

**Frontend** (to be created):
- `front/src/shared/types/launch.ts` — Type definitions
- `front/src/features/launches/launchesApi.ts` — API abstraction
- `front/src/features/launches/useLaunches.ts` — Custom hook
- `front/src/features/launches/LaunchesScheduler.tsx` — Main component
- `front/src/features/launches/LaunchesScheduler.css` — Styles

**Frontend Tests** (to be created):
- `front/src/features/launches/launchesApi.test.ts`
- `front/src/features/launches/useLaunches.test.ts`
- `front/src/features/launches/LaunchesScheduler.test.tsx`

**E2E Tests** (to be created):
- `e2e/pages/LaunchesPage.ts` — Page object
- `e2e/tests/launches.spec.ts` — Test suite

**Modified Files**:
- `front/src/App.tsx` — Add launches route & navigation

---

## Verification

**Automated Verification** (run in order):
1. Backend unit tests: `cd back && mvn clean test` — All launch *Test.java files pass (repository, service, controller)
2. Backend smoke test: `cd back && mvn spring-boot:run` — Server starts, DB initializes with launches table
3. Frontend unit tests: `cd front && npm test` — All launches feature tests pass (api, hook, component)
4. Frontend build: `cd front && npm run build` — No TypeScript errors, no ESLint warnings
5. E2E tests: `cd e2e && npm test` — All launches.spec.ts tests pass (auto-boots full stack)

**Manual Verification** (spot checks):
- Navigate to /launches in browser → form visible, no console errors
- Create a launch with valid inputs (select rocket, future date, price > 0, occupancy ≤ rocket capacity) → appears in list with CREATED status
- Attempt to create with past date → validation error displayed
- Attempt to create with occupancy > rocket capacity → validation error displayed
- Transition launch from CREATED → CONFIRMED → COMPLETED → list reflects updated status
- Completed launch: status transition button disabled

---

## Decisions & Assumptions

1. **No rocket relationship loading**: Launches reference rocket_id only (no eager loading of rocket entity). Service validates rocket_id exists via RocketRepository lookup; API does NOT return full rocket object embedded.
   - *Rationale*: Simpler schema, API contract clearer, avoids N+1 queries
   
2. **Status transitions are strict**: Only valid transitions allowed (created→confirmed/cancelled, confirmed→completed/cancelled). Rejects invalid transitions (e.g., confirmed→created).
   - *Rationale*: Matches spec requirement; prevents accidental state machine violations
   
3. **Completed/cancelled launches are immutable**: Attempting to modify (create/edit) a completed or cancelled launch returns 409 Conflict.
   - *Rationale*: Business rule in spec; prevents data corruption
   
4. **Launch dates validated as future**: `@FutureOrPresent` on LaunchRequest ensures launch_time is not in the past.
   - *Rationale*: Spec requirement "launch_time must be in the future"
   
5. **Minimum occupancy validated against rocket capacity**: Service checks that minimum_occupancy ≤ referenced rocket's capacity during create.
   - *Rationale*: Spec requirement; prevents invalid occupancy constraints
   
6. **Frontend uses hook pattern** (useLaunches): Inspired by useHealth, cleanly separates API/state logic from rendering logic.
   - *Rationale*: Reusability, testability, consistency with health feature's extraction pattern
   
7. **Launches list is ordered by launch_time ascending**: Custom repository query ensures predictable order.
   - *Rationale*: Spec: "all launches returned ordered by launch_time ascending"
   
8. **E2E includes network delay simulation**: Tests verify loading state visibility during slow responses.
   - *Rationale*: Matches health.spec.ts pattern; catches state management edge cases

---

## Further Considerations

1. **Pagination (out of scope)**: Spec doesn't mention pagination. If launches list grows large (100+), consider adding page/limit query parameters in Phase 2. Default: return all launches ordered by time.

2. **Filtering by status (out of scope)**: Spec shows listing all launches. If operators want to filter (e.g., "show only CREATED launches"), add optional `status` query parameter to GET /api/launches.

3. **Concurrency (out of scope)**: Spec doesn't mention concurrent edits. If multiple users modify same launch, current code does last-write-wins. For data integrity, consider pessimistic locking or optimistic versioning (ETag) in Phase 2.
