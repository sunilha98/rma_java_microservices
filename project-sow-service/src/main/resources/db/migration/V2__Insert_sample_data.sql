-- Insert sample practices
INSERT INTO practices (name, description, is_active) VALUES
('Software Development', 'Custom software development and application services', true),
('Data Analytics', 'Business intelligence and data analytics services', true),
('Cloud Solutions', 'Cloud migration and infrastructure services', true),
('Quality Assurance', 'Testing and quality assurance services', true),
('DevOps', 'DevOps and automation services', true);

-- Insert sample SOWs
INSERT INTO sows (sow_number, client_id, title, description, priority, start_date, end_date, status, created_by_user_id) VALUES
('SOW-2024-001', 1001, 'E-commerce Platform Development', 'Development of a comprehensive e-commerce platform with payment integration', 'HIGH', '2024-01-15 09:00:00', '2024-06-30 18:00:00', 'ACTIVE', 1),
('SOW-2024-002', 1002, 'Data Warehouse Implementation', 'Implementation of enterprise data warehouse solution', 'MEDIUM', '2024-02-01 09:00:00', '2024-08-31 18:00:00', 'APPROVED', 1),
('SOW-2024-003', 1003, 'Mobile App Development', 'Cross-platform mobile application development', 'HIGH', '2024-03-01 09:00:00', '2024-07-31 18:00:00', 'DRAFT', 1);

-- Insert sample projects
INSERT INTO projects (project_code, name, description, sow_id, client_id, practice_id, status, start_date, end_date, budget, actual_cost, progress, project_manager_user_id, created_by_user_id) VALUES
('PROJ-2024-001', 'E-commerce Frontend', 'Frontend development for e-commerce platform', 1, 1001, 1, 'IN_PROGRESS', '2024-01-15 09:00:00', '2024-04-30 18:00:00', 150000.00, 75000.00, 45, 2, 1),
('PROJ-2024-002', 'E-commerce Backend', 'Backend API development for e-commerce platform', 1, 1001, 1, 'IN_PROGRESS', '2024-02-01 09:00:00', '2024-06-30 18:00:00', 200000.00, 80000.00, 30, 3, 1),
('PROJ-2024-003', 'Data Pipeline Setup', 'Setting up data ingestion and processing pipelines', 2, 1002, 2, 'PLANNING', '2024-02-15 09:00:00', '2024-05-31 18:00:00', 180000.00, 0.00, 0, 4, 1);

-- Insert sample project status updates
INSERT INTO project_status_update (project_id, milestone, deliverables, progress, risks, issues, updated_by) VALUES
(1, 'Sprint 3 Completion', 'User authentication module, product catalog API', 45, 'Third-party API integration delays', 'Performance optimization needed for search functionality', 'pm_user'),
(2, 'Database Design', 'Database schema design and initial setup', 30, 'Complex data relationships', 'Need clarification on data retention policies', 'pm_user'),
(1, 'Sprint 2 Completion', 'Basic UI components, navigation structure', 30, 'Design approval delays', 'Client feedback on UI design', 'pm_user');

-- Insert sample lesson learned
INSERT INTO lesson_learned (title, description, category, created_by, project_id) VALUES
('Early Client Feedback Integration', 'Regular client demos helped identify issues early and reduced rework', 'Process Improvement', 'pm_user', 1),
('API Documentation Standards', 'Establishing clear API documentation standards improved team collaboration', 'Technical', 'dev_lead', 1),
('Testing Environment Setup', 'Setting up comprehensive testing environments early saved debugging time', 'Technical', 'qa_lead', 2);
