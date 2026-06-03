---
spec-slug: passenger-booking
---

# Specification for Passenger Booking

## Problem definition

Users need a way to book passenger tickets for rocket launches. The booking system should capture essential passenger information and provide confirmation of their booking. This enables launch operators to track passenger reservations and passengers to secure their spots on launches.

### User Stories

- As a **passenger**, I want to **book a ticket for a rocket launch** by providing my name, email, and phone number, so that I can reserve my spot on the launch.
- As a **passenger**, I want to **receive a booking confirmation with a unique booking ID**, so that I can reference my reservation.
- As a **launch operator**, I want to **view all bookings for a specific launch**, so that I can manage passenger manifest and logistics.
- As a **passenger**, I want to **view my booking details**, so that I can verify the information I provided.

## Solution overview

### Data Model

**Booking**: Represents a passenger ticket reservation for a launch
- id: uuid#
- launch_id: uuid (foreign key to Launch)
- passenger_name: string
- passenger_email: string (email format)
- passenger_phone: string
- status: enum [created, cancelled]
- created_at: datetime
- updated_at: datetime?
- Rules:
  - Each booking is uniquely identified by id
  - A passenger can book multiple times for different launches
  - Booking status defaults to "created"
  - Email and phone must be in valid format

### Backend API

- **POST /api/launches/{launchId}/bookings** — Create a new passenger booking
  - Request body: { passengerName, passengerEmail, passengerPhone }
  - Response: { id, launchId, passengerName, passengerEmail, passengerPhone, status, createdAt }
  - Status codes: 201 (Created), 400 (Bad Request - invalid data), 404 (Not Found - launch not found), 409 (Conflict - booking limit reached)

- **GET /api/launches/{launchId}/bookings** — Retrieve all bookings for a launch
  - Response: Array of booking objects with pagination support
  - Status codes: 200 (OK), 404 (Not Found - launch not found)

- **GET /api/bookings/{bookingId}** — Retrieve a specific booking by ID
  - Response: { id, launchId, passengerName, passengerEmail, passengerPhone, status, createdAt }
  - Status codes: 200 (OK), 404 (Not Found - booking not found)

### Frontend Application

- **Booking Form Component** — Allow passengers to enter their details
  - Input fields: Name (text), Email (email), Phone (text)
  - Form validation: All fields required, email format validation, phone format validation
  - Submit button with loading state

- **Booking Confirmation Page** — Display confirmation after successful booking
  - Show booking ID, passenger name, email, phone, and creation timestamp
  - Provide option to return to launch details
  - Include copy-to-clipboard functionality for booking ID

- **Bookings List View** — Display all bookings for a launch (if accessible to operators)
  - Table with columns: Booking ID, Passenger Name, Email, Phone, Status, Created Date
  - Pagination and filtering options

- **Booking Details Page** — Allow passengers to view their booking information
  - Display all booking details
  - Show status badge

### Database Schema

**bookings table**:
```
- id (UUID, PRIMARY KEY)
- launch_id (UUID, FOREIGN KEY -> launches.id)
- passenger_name (VARCHAR, NOT NULL)
- passenger_email (VARCHAR, NOT NULL)
- passenger_phone (VARCHAR, NOT NULL)
- status (VARCHAR, NOT NULL, DEFAULT 'created')
- created_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
- updated_at (TIMESTAMP, NULL)
- INDEX on (launch_id) for efficient querying by launch
```

## Acceptance and Release

- [ ] WHEN a passenger submits a valid booking form, THEN a new booking is created with status "created" and a unique booking ID is returned.
- [ ] WHEN booking is created successfully, THEN the system displays a confirmation page with the booking ID and passenger details.
- [ ] WHEN a passenger provides invalid data (missing fields, invalid email/phone format), THEN the system displays appropriate validation error messages.
- [ ] WHEN a booking is submitted for a non-existent launch, THEN the system returns a 404 error.
- [ ] WHEN booking details are retrieved by ID, THEN the system returns complete booking information with all passenger details.
- [ ] WHEN all bookings for a launch are retrieved, THEN the system returns a list of all bookings for that specific launch.
- [ ] WHEN a booking ID is provided, THEN the system must be able to retrieve and display the associated booking confirmation and details.
- [ ] WHEN the booking form is submitted, THEN passenger information (name, email, phone) is validated and persisted in the database.
- [ ] WHEN a passenger views booking details, THEN the information matches what was submitted at booking time.
