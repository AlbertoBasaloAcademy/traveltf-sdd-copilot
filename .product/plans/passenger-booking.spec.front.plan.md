---
plan-type: spec
tier: front
---
# Spec - passenger-booking - front

## Specification

Implement the frontend React components and pages for the passenger booking system. The UI must provide a user-friendly booking form, confirmation page, and booking details view with proper validation and feedback.

**Context**: [passenger-booking.spec.md](../specs/passenger-booking.spec.md)

### Data model

**Frontend state** (TypeScript types):
- `CreateBookingRequest`: { passengerName, passengerEmail, passengerPhone }
- `BookingResponse`: { id, launchId, passengerName, passengerEmail, passengerPhone, status, createdAt }
- `BookingFormState`: { formData, errors, isSubmitting, isSuccess }
- `BookingListState`: { bookings, isLoading, page, pageSize }

## Implementation Steps

### Step 1: Create booking API client
Implement HTTP calls to backend booking endpoints.
- Paths:
    - `front/src/shared/api/`
    - `front/src/features/bookings/`
- [ ] Create `bookingApi.ts` with functions:
      - `createBooking(launchId: string, data: CreateBookingRequest): Promise<BookingResponse>`
      - `getBooking(bookingId: string): Promise<BookingResponse>`
      - `getBookingsByLaunch(launchId: string, page?: number, pageSize?: number): Promise<PaginatedResponse<BookingResponse>>`
- [ ] Add error handling and HTTP status code mapping
- [ ] Create `bookingApi.test.ts` with unit tests for API calls

### Step 2: Create booking types and constants
Define TypeScript types and validation constants.
- Paths:
    - `front/src/shared/types/`
    - `front/src/features/bookings/`
- [ ] Create `Booking.ts` type definitions:
      - `CreateBookingRequest`
      - `BookingResponse`
      - `BookingStatus` enum
- [ ] Create validation constants:
      - Email regex pattern
      - Phone number pattern (or library like libphonenumber-js)
      - Min/max field lengths
- [ ] Export types for use across components

### Step 3: Create booking form component
Implement the passenger booking form with validation.
- Paths:
    - `front/src/features/bookings/components/BookingForm.tsx`
    - `front/src/features/bookings/BookingForm.css`
- [ ] Create React component with form fields: passengerName, passengerEmail, passengerPhone
- [ ] Implement client-side validation:
      - All fields required
      - Email format validation
      - Phone format validation
- [ ] Add error message display for each field
- [ ] Implement submit handler:
      - Validate form data
      - Call API to create booking
      - Show loading state on submit button
      - Redirect to confirmation page on success
      - Display error message on failure
- [ ] Add CSS styling with BEM or similar convention
- [ ] Write component tests covering:
      - Form rendering and input changes
      - Validation error display
      - Submit button behavior
      - API call on form submission

### Step 4: Create booking confirmation page
Display confirmation after successful booking.
- Paths:
    - `front/src/features/bookings/pages/BookingConfirmation.tsx`
    - `front/src/features/bookings/BookingConfirmation.css`
- [ ] Receive booking ID from route params or state
- [ ] Fetch booking details from API
- [ ] Display booking information:
      - Booking ID with copy-to-clipboard button
      - Passenger name
      - Passenger email
      - Passenger phone
      - Creation timestamp (formatted)
- [ ] Add button to return to launch details or booking list
- [ ] Handle loading and error states
- [ ] Add CSS styling for confirmation page layout
- [ ] Write component tests for data display and interactions

### Step 5: Create booking details page
Allow passengers to view their booking information.
- Paths:
    - `front/src/features/bookings/pages/BookingDetails.tsx`
    - `front/src/features/bookings/BookingDetails.css`
- [ ] Create page component accepting booking ID from route params
- [ ] Fetch booking details from API on mount
- [ ] Display all booking information:
      - Booking ID
      - Launch ID
      - Passenger details (name, email, phone)
      - Status badge
      - Creation date (formatted)
- [ ] Handle loading, error, and not-found states
- [ ] Add navigation link to return to bookings list
- [ ] Add CSS styling consistent with app theme
- [ ] Write component tests

### Step 6: Create bookings list component (optional for operators)
Display all bookings for a launch.
- Paths:
    - `front/src/features/bookings/components/BookingsList.tsx`
    - `front/src/features/bookings/BookingsList.css`
- [ ] Create table component displaying bookings with columns:
      - Booking ID
      - Passenger Name
      - Email
      - Phone
      - Status
      - Created Date
- [ ] Implement pagination:
      - Previous/Next buttons
      - Page size selector
      - Current page display
- [ ] Add sorting/filtering options (basic: by creation date)
- [ ] Implement row click to navigate to booking details
- [ ] Add CSS styling for table layout
- [ ] Write component tests

### Step 7: Create booking routes and layout
Wire up routing and navigation.
- Paths:
    - `front/src/App.tsx` or `front/src/router/`
    - `front/src/features/bookings/`
- [ ] Add routes:
      - `/launches/:launchId/bookings/new` → BookingForm
      - `/bookings/:bookingId` → BookingDetails
      - `/launches/:launchId/bookings` → BookingsList (if implemented)
      - `/bookings/:bookingId/confirmation` → BookingConfirmation
- [ ] Add navigation from launch details to booking form
- [ ] Add breadcrumb navigation (optional)
- [ ] Ensure routing matches backend API structure

### Step 8: Add form validation utilities
Create reusable validation functions.
- Paths:
    - `front/src/shared/utils/validators.ts`
    - `front/src/shared/utils/validators.test.ts`
- [ ] Create `validateEmail(email: string): boolean` function
- [ ] Create `validatePhone(phone: string): boolean` function
- [ ] Create `getValidationError(field: string, value: string): string | null` function
- [ ] Write unit tests for validators

### Step 9: E2E tests for booking flow
Create Playwright tests covering the complete booking flow.
- Paths:
    - `e2e/tests/booking.spec.ts`
    - `e2e/pages/BookingPage.ts`
- [ ] Create page object for booking form (BookingPage.ts)
- [ ] Write E2E test: Navigate to launch, click book button, fill form, submit, verify confirmation
- [ ] Write E2E test: Verify invalid email/phone shows errors
- [ ] Write E2E test: Verify submitted booking can be retrieved
- [ ] Run tests against live backend instance
