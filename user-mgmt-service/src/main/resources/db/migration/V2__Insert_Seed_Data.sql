-- Insert seed data for testing purposes
-- Passwords are hashed using bcrypt with 'password123' as the plain text

INSERT INTO users (username, email, password, first_name, last_name, role, is_active, created_by, updated_by)
VALUES 
    ('admin', 'admin@resourcemanagement.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYrWswsE7J0kR8uW7W1dT6Kj7X1/2O', 'System', 'Administrator', 'SUPER_ADMIN', true, 'system', 'system'),
    ('pm_user', 'pm@resourcemanagement.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYrWswsE7J0kR8uW7W1dT6Kj7X1/2O', 'Project', 'Manager', 'PROJECT_MANAGER', true, 'system', 'system'),
    ('resource_user', 'resource@resourcemanagement.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYrWswsE7J0kR8uW7W1dT6Kj7X1/2O', 'John', 'Doe', 'RMT', true, 'system', 'system'),
    ('finance_user', 'finance@resourcemanagement.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYrWswsE7J0kR8uW7W1dT6Kj7X1/2O', 'Jane', 'Smith', 'Finance Controllers', true, 'system', 'system')
ON CONFLICT (username) DO NOTHING;
