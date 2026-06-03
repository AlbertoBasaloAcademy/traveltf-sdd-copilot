-- Create bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id VARCHAR(36) PRIMARY KEY,
    launch_id VARCHAR(36) NOT NULL,
    passenger_name VARCHAR(255) NOT NULL,
    passenger_email VARCHAR(255) NOT NULL,
    passenger_phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'created',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (launch_id) REFERENCES launch(id) ON DELETE CASCADE
);

-- Create index on launch_id for efficient lookups by launch
CREATE INDEX IF NOT EXISTS idx_bookings_launch_id ON bookings(launch_id);

-- Create index on email for quick lookups
CREATE INDEX IF NOT EXISTS idx_bookings_email ON bookings(passenger_email);
