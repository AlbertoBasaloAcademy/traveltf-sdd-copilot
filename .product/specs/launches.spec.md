---
spec-slug: launches
---

# Specification for Launch Scheduling and Management

## Problem definition

Operators and users need to schedule rocket launches with specific operational and commercial parameters. Currently, there's no way to track launch schedules, pricing, capacity requirements, or status transitions. This makes it impossible to plan and manage launch operations effectively.

### User Stories

- As an **operator**, I want to **create a launch with specific date, time, rocket, ticket price, and minimum occupancy** so that I can plan launch operations.
- As an **operator**, I want to **view all scheduled launches** to manage the launch calendar.
- As an **operator**, I want to **change a launch status** (created → confirmed → completed, or cancelled) so that I can track the launch lifecycle.
- As an **operator**, I want to **retrieve launch details by ID** to view complete launch information.
- As an **user**, I want to **browse available launches** so that I can see upcoming launch opportunities.

## Solution overview

### Data Model

- **Launch**: Represents a scheduled rocket flight event
  - id: uuid#
  - rocket_id: uuid (reference to Rocket)
  - launch_time: datetime
  - ticket_price: number [1..∞]
  - minimum_occupancy: number [1..capacity_of_rocket]
  - status: enum [created, confirmed, completed, cancelled]
  - created_at: datetime
  - updated_at: datetime
  - Rules:
    - A launch must reference an existing rocket
    - launch_time must be in the future
    - minimum_occupancy cannot exceed the referenced rocket's capacity
    - Status transitions follow the lifecycle: created → {confirmed, cancelled} → {completed, cancelled}
    - Once completed or cancelled, a launch cannot be modified

### Backend API

- **POST /api/launches** — Create a new launch with rocket, time, pricing, and occupancy requirements
- **GET /api/launches** — List all launches (with optional filtering by status or date range)
- **GET /api/launches/{id}** — Retrieve full details of a specific launch
- **PATCH /api/launches/{id}/status** — Transition launch status (created → confirmed → completed, or to cancelled)

### Frontend Application

- **Launch Scheduler Form** — Create new launches with dropdowns for rocket selection, datetime picker, price and occupancy inputs
- **Launches List View** — Display all launches with summary info (rocket, launch_time, status) and actions (view details, change status)
- **Launch Detail View** — Show complete launch information including all parameters and status history

### Database Schema

- **launches** table
  - id: UUID, primary key
  - rocket_id: UUID, foreign key → rockets(id), NOT NULL
  - launch_time: TIMESTAMP, NOT NULL
  - ticket_price: DECIMAL(10,2), NOT NULL, CHECK (ticket_price > 0)
  - minimum_occupancy: INTEGER, NOT NULL, CHECK (minimum_occupancy > 0)
  - status: VARCHAR(20), NOT NULL, default 'created', enum check (created, confirmed, completed, cancelled)
  - created_at: TIMESTAMP, default current_timestamp
  - updated_at: TIMESTAMP, default current_timestamp
  - Indices: (rocket_id), (status), (launch_time)

## Acceptance and Release

- [ ] **WHEN** a launch is created with valid rocket_id, launch_time, ticket_price, and minimum_occupancy, **THEN** it is persisted with status 'created' and returned with id and timestamps.
- [ ] **WHEN** launch_time is in the past, **THEN** the system rejects the request with a validation error.
- [ ] **WHEN** minimum_occupancy exceeds the rocket's capacity, **THEN** the system rejects the request with a validation error.
- [ ] **WHEN** the launches list is requested, **THEN** all launches are returned ordered by launch_time ascending.
- [ ] **WHEN** a specific launch is retrieved by id, **THEN** all launch details are returned including rocket info and status.
- [ ] **WHEN** a launch status is transitioned from 'created' to 'confirmed', **THEN** the status is updated and updated_at is set to current time.
- [ ] **WHEN** a launch status is transitioned to 'cancelled', **THEN** the status is updated regardless of current status (except if already completed).
- [ ] **WHEN** attempting to modify a completed or cancelled launch, **THEN** the system rejects with a 409 Conflict error.
- [ ] **WHEN** a launch is requested with a non-existent id, **THEN** the system returns 404 Not Found.
- [ ] **WHEN** invalid status transition is attempted, **THEN** the system returns 400 Bad Request with descriptive error message.

