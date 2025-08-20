-- Seed titles with safe insert pattern
INSERT INTO titles (name, level, is_active)
SELECT * FROM (SELECT 'Software Engineer' AS name, 'Mid' AS level, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM titles WHERE name = 'Software Engineer' LIMIT 1);

INSERT INTO titles (name, level, is_active)
SELECT * FROM (SELECT 'Senior Software Engineer' AS name, 'Senior' AS level, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM titles WHERE name = 'Senior Software Engineer' LIMIT 1);

INSERT INTO titles (name, level, is_active)
SELECT * FROM (SELECT 'Technical Lead' AS name, 'Lead' AS level, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM titles WHERE name = 'Technical Lead' LIMIT 1);

INSERT INTO titles (name, level, is_active)
SELECT * FROM (SELECT 'Project Manager' AS name, 'Senior' AS level, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM titles WHERE name = 'Project Manager' LIMIT 1);

INSERT INTO titles (name, level, is_active)
SELECT * FROM (SELECT 'Business Analyst' AS name, 'Mid' AS level, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM titles WHERE name = 'Business Analyst' LIMIT 1);

INSERT INTO titles (name, level, is_active)
SELECT * FROM (SELECT 'QA Engineer' AS name, 'Junior' AS level, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM titles WHERE name = 'QA Engineer' LIMIT 1);

-- Seed skillsets with safe insert pattern
INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'Java' AS name, 'Technical' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'Java' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'Spring Boot' AS name, 'Technical' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'Spring Boot' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'React' AS name, 'Technical' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'React' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'SQL' AS name, 'Technical' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'SQL' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'Agile Methodology' AS name, 'Functional' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'Agile Methodology' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'Project Management' AS name, 'Functional' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'Project Management' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'Banking Domain' AS name, 'Domain' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'Banking Domain' LIMIT 1);

INSERT INTO skillsets (name, category, is_active)
SELECT * FROM (SELECT 'Healthcare Domain' AS name, 'Domain' AS category, true AS is_active) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM skillsets WHERE name = 'Healthcare Domain' LIMIT 1);

-- Seed resources with safe insert pattern
INSERT INTO resources (employee_id, first_name, last_name, email, title_id, location_id, experience, allocation_percentage, is_active)
SELECT * FROM (
    SELECT 'EMP001' AS employee_id, 'John' AS first_name, 'Doe' AS last_name,
           'john.doe@example.com' AS email,
           (SELECT id FROM titles WHERE name = 'Software Engineer' LIMIT 1) AS title_id,
           1 AS location_id, 3.5 AS experience, 100 AS allocation_percentage, true AS is_active
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE employee_id = 'EMP001' LIMIT 1);

INSERT INTO resources (employee_id, first_name, last_name, email, title_id, location_id, experience, allocation_percentage, is_active)
SELECT * FROM (
    SELECT 'EMP002' AS employee_id, 'Jane' AS first_name, 'Smith' AS last_name,
           'jane.smith@example.com' AS email,
           (SELECT id FROM titles WHERE name = 'Senior Software Engineer' LIMIT 1) AS title_id,
           1 AS location_id, 6.2 AS experience, 75 AS allocation_percentage, true AS is_active
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE employee_id = 'EMP002' LIMIT 1);

INSERT INTO resources (employee_id, first_name, last_name, email, title_id, location_id, experience, allocation_percentage, is_active)
SELECT * FROM (
    SELECT 'EMP003' AS employee_id, 'Robert' AS first_name, 'Johnson' AS last_name,
           'robert.johnson@example.com' AS email,
           (SELECT id FROM titles WHERE name = 'Technical Lead' LIMIT 1) AS title_id,
           2 AS location_id, 8.0 AS experience, 100 AS allocation_percentage, true AS is_active
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE employee_id = 'EMP003' LIMIT 1);

INSERT INTO resources (employee_id, first_name, last_name, email, title_id, location_id, experience, allocation_percentage, is_active)
SELECT * FROM (
    SELECT 'EMP004' AS employee_id, 'Emily' AS first_name, 'Williams' AS last_name,
           'emily.williams@example.com' AS email,
           (SELECT id FROM titles WHERE name = 'Project Manager' LIMIT 1) AS title_id,
           2 AS location_id, 7.5 AS experience, 100 AS allocation_percentage, true AS is_active
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE employee_id = 'EMP004' LIMIT 1);

INSERT INTO resources (employee_id, first_name, last_name, email, title_id, location_id, experience, allocation_percentage, is_active)
SELECT * FROM (
    SELECT 'EMP005' AS employee_id, 'Michael' AS first_name, 'Brown' AS last_name,
           'michael.brown@example.com' AS email,
           (SELECT id FROM titles WHERE name = 'Software Engineer' LIMIT 1) AS title_id,
           3 AS location_id, 2.0 AS experience, 0 AS allocation_percentage, true AS is_active
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM resources WHERE employee_id = 'EMP005' LIMIT 1);

-- Seed resource_skillsets with safe insert pattern
-- John Doe: Java, Spring Boot, SQL
INSERT INTO resource_skillsets (resource_id, skillset_id)
SELECT * FROM (
    SELECT (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1) AS resource_id,
           (SELECT id FROM skillsets WHERE name = 'Java' LIMIT 1) AS skillset_id
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM resource_skillsets
    WHERE resource_id = (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1)
    AND skillset_id = (SELECT id FROM skillsets WHERE name = 'Java' LIMIT 1)
    LIMIT 1
);

INSERT INTO resource_skillsets (resource_id, skillset_id)
SELECT * FROM (
    SELECT (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1) AS resource_id,
           (SELECT id FROM skillsets WHERE name = 'Spring Boot' LIMIT 1) AS skillset_id
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM resource_skillsets
    WHERE resource_id = (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1)
    AND skillset_id = (SELECT id FROM skillsets WHERE name = 'Spring Boot' LIMIT 1)
    LIMIT 1
);

INSERT INTO resource_skillsets (resource_id, skillset_id)
SELECT * FROM (
    SELECT (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1) AS resource_id,
           (SELECT id FROM skillsets WHERE name = 'SQL' LIMIT 1) AS skillset_id
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM resource_skillsets
    WHERE resource_id = (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1)
    AND skillset_id = (SELECT id FROM skillsets WHERE name = 'SQL' LIMIT 1)
    LIMIT 1
);

-- Seed allocations with safe insert pattern
INSERT INTO allocations (project_id, resource_id, title_id, allocation_percentage, start_date, end_date, status)
SELECT * FROM (
    SELECT 101 AS project_id,
           (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1) AS resource_id,
           (SELECT id FROM titles WHERE name = 'Software Engineer' LIMIT 1) AS title_id,
           100 AS allocation_percentage,
           '2023-01-01 00:00:00'::timestamp AS start_date,
           '2023-12-31 00:00:00'::timestamp AS end_date,
           'ACTIVE' AS status
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM allocations
    WHERE project_id = 101
    AND resource_id = (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1)
    LIMIT 1
);

-- Seed fulfillment_requests with safe insert pattern
INSERT INTO fulfillment_requests (id, project_id, title_id, location_id, status, expected_closure, experience, positions)
SELECT gen_random_uuid(), 101,
           (SELECT id FROM titles WHERE name = 'Senior Software Engineer' LIMIT 1) AS title_id,
           1 AS location_id,
           'OPEN' AS status,
           '2023-06-30'::date AS expected_closure,
           5.0 AS experience,
           2 AS positions
WHERE NOT EXISTS (
    SELECT 1 FROM fulfillment_requests
    WHERE project_id = 101
    AND title_id = (SELECT id FROM titles WHERE name = 'Senior Software Engineer' LIMIT 1)
    LIMIT 1
);

-- Seed release_requests with safe insert pattern
INSERT INTO release_requests (project_id, resource_id, reason, effective_date, status)
SELECT 101 AS project_id,
           (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1) AS resource_id,
           'Project completion' AS reason,
           '2023-12-31 00:00:00'::timestamp AS effective_date,
           'PENDING' AS status
WHERE NOT EXISTS (
    SELECT 1 FROM release_requests
    WHERE project_id = 101
    AND resource_id = (SELECT id FROM resources WHERE employee_id = 'EMP001' LIMIT 1)
    LIMIT 1
);
