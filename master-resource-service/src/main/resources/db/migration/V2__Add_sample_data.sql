-- V2__Add_sample_data.sql
-- Sample Clients
INSERT INTO clients (name, code, contact_email, contact_phone, is_active, created_at) VALUES
('Acme Corporation', 'ACME001', 'contact@acme.com', '+1 (555) 123-4567', true, CURRENT_TIMESTAMP),
('Globex Corporation', 'GLOBEX002', 'info@globex.com', '+1 (555) 234-5678', true, CURRENT_TIMESTAMP),
('Soylent Corp', 'SOYLENT003', 'support@soylent.com', '+1 (555) 345-6789', false, CURRENT_TIMESTAMP),
('Initech', 'INITECH004', 'hr@initech.com', '+1 (555) 456-7890', true, CURRENT_TIMESTAMP),
('Umbrella Corporation', 'UMBRELLA005', 'biohazard@umbrella.com', '+1 (555) 567-8901', true, CURRENT_TIMESTAMP);

-- Sample Locations
INSERT INTO locations (name, is_active, created_at, created_by) VALUES
('New York Office', true, CURRENT_TIMESTAMP, 'admin@system.com'),
('London Office', true, CURRENT_TIMESTAMP, 'admin@system.com'),
('Tokyo Office', true, CURRENT_TIMESTAMP, 'admin@system.com'),
('Remote', true, CURRENT_TIMESTAMP, 'admin@system.com'),
('Bangalore Office', false, CURRENT_TIMESTAMP, 'admin@system.com');

-- Sample Shift Timings
INSERT INTO shift_timings (name, start_time, end_time, created_at, updated_at) VALUES
('Morning Shift', '08:00:00', '16:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Evening Shift', '16:00:00', '00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Night Shift', '00:00:00', '08:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Flexible Shift', '10:00:00', '18:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Part-time Shift', '12:00:00', '16:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sample Titles
INSERT INTO titles (name, level, is_active, created_at) VALUES
('Software Engineer', 'Mid', true, CURRENT_TIMESTAMP),
('Senior Software Engineer', 'Senior', true, CURRENT_TIMESTAMP),
('Engineering Manager', 'Lead', true, CURRENT_TIMESTAMP),
('Principal Architect', 'Principal', true, CURRENT_TIMESTAMP),
('Junior Developer', 'Junior', true, CURRENT_TIMESTAMP),
('QA Engineer', 'Mid', false, CURRENT_TIMESTAMP),
('DevOps Specialist', 'Senior', true, CURRENT_TIMESTAMP);

-- Sample Activity Logs
INSERT INTO activity_logs (action, performed_by, role, module, details, timestamp) VALUES
('CREATE', 'admin@system.com', 'ADMIN', 'Client', 'Created new client: Acme Corporation', CURRENT_TIMESTAMP),
('UPDATE', 'manager@system.com', 'MANAGER', 'Location', 'Updated London Office details', CURRENT_TIMESTAMP),
('DELETE', 'admin@system.com', 'ADMIN', 'Title', 'Deleted obsolete title: Legacy Developer', CURRENT_TIMESTAMP),
('LOGIN', 'user@system.com', 'USER', 'Authentication', 'User logged in from IP: 192.168.1.100', CURRENT_TIMESTAMP),
('CREATE', 'hr@system.com', 'HR', 'Shift', 'Added new shift timing: Flexible Shift', CURRENT_TIMESTAMP);