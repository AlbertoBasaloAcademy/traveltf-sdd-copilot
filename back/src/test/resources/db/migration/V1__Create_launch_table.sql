-- Create rocket table
CREATE TABLE IF NOT EXISTS rocket (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    range VARCHAR(50) NOT NULL,
    decommissioned BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create launch table
CREATE TABLE IF NOT EXISTS launch (
    id VARCHAR(36) PRIMARY KEY,
    rocket_id VARCHAR(36) NOT NULL,
    launch_time TIMESTAMP NOT NULL,
    ticket_price INTEGER NOT NULL,
    minimum_occupancy INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (rocket_id) REFERENCES rocket(id)
);

-- Create index on rocket_id for efficient lookups
CREATE INDEX IF NOT EXISTS idx_launch_rocket_id ON launch(rocket_id);

-- Create health_check table
CREATE TABLE IF NOT EXISTS health_check (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status VARCHAR(50) NOT NULL,
    database_status VARCHAR(50) NOT NULL,
    uptime_seconds INTEGER NOT NULL,
    checked_at VARCHAR(255) NOT NULL
);

