---
plan-type: spec
tier: db
---
# Spec - passenger-booking - db

## Specification

Implement the persistent data layer for the passenger booking system. The bookings table must store passenger ticket reservations with validation constraints and efficient lookup by launch ID.

**Context**: [passenger-booking.spec.md](../specs/passenger-booking.spec.md)

### Data model

**bookings table**:
- `id` (UUID, PRIMARY KEY) — Unique identifier for each booking
- `launch_id` (UUID, FOREIGN KEY → launches.id) — Reference to the launch being booked
- `passenger_name` (VARCHAR, NOT NULL) — Full name of the passenger
- `passenger_email` (VARCHAR, NOT NULL) — Email address with validation
- `passenger_phone` (VARCHAR, NOT NULL) — Phone number with validation
- `status` (VARCHAR, NOT NULL, DEFAULT 'created') — Booking status: 'created' or 'cancelled'
- `created_at` (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP) — When the booking was created
- `updated_at` (TIMESTAMP, NULL) — When the booking was last modified
- **Index on launch_id** for efficient querying of bookings by launch

## Implementation Steps

### Step 1: Create bookings table migration
Create a new database migration file to define the bookings table schema with all columns, constraints, and indexes.
- Paths:
    - `back/src/main/resources/db/migration/` (Flyway or similar)
- [ ] Define bookings table with UUID primary key
- [ ] Add foreign key constraint to launches table
- [ ] Add NOT NULL constraints on passenger_name, passenger_email, passenger_phone, and status
- [ ] Set status column default value to 'created'
- [ ] Add created_at timestamp with default CURRENT_TIMESTAMP
- [ ] Add nullable updated_at timestamp column
- [ ] Create index on launch_id for query performance
- [ ] Add check constraints or documentation for email/phone format validation (handle in application layer)

### Step 2: Add launch-bookings relationship
Ensure the launches table is properly referenced by the bookings table with cascading constraints.
- Paths:
    - `back/src/main/resources/db/migration/`
- [ ] Verify launches table exists with UUID id column
- [ ] Add ON DELETE CASCADE or ON DELETE RESTRICT for referential integrity (decide based on business logic)
- [ ] Document the cascading delete policy in code comments

### Step 3: Configure database connection and Flyway
Ensure the backend project is configured to auto-run migrations on startup.
- Paths:
    - `back/pom.xml`
    - `back/src/main/resources/application.yml`
- [ ] Verify Flyway dependency is present in pom.xml
- [ ] Configure Flyway in application.yml to scan migration files
- [ ] Enable auto-migration on application startup

### Step 4: Verify migration in test environment
Add SQL test fixtures or verify migration runs correctly in test environment.
- Paths:
    - `back/src/test/resources/db/migration/`
    - `back/src/test/resources/application.yml`
- [ ] Ensure test environment has separate migration path or uses same Flyway configuration
- [ ] Document test data setup if needed for integration tests
