-- V1__Initial_schema.sql
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    contact_email VARCHAR(255),
    contact_phone VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    CONSTRAINT uk_client_code UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS shift_timings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_shift_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS titles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    level VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    action VARCHAR(255),
    performed_by VARCHAR(255),
    role VARCHAR(255),
    module VARCHAR(255),
    details VARCHAR(1000),
    timestamp TIMESTAMP
);

-- Add indexes for better performance
CREATE INDEX idx_client_active ON clients(is_active);
CREATE INDEX idx_location_active ON locations(is_active);
CREATE INDEX idx_title_active ON titles(is_active);
CREATE INDEX idx_activity_log_timestamp ON activity_logs(timestamp);