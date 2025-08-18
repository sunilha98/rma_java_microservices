-- Create practices table
CREATE TABLE IF NOT EXISTS practices (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create sows table
CREATE TABLE IF NOT EXISTS sows (
    id BIGSERIAL PRIMARY KEY,
    sow_number VARCHAR(255) NOT NULL UNIQUE,
    client_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'APPROVED', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
    document_url VARCHAR(500),
    created_by_user_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    project_code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sow_id BIGINT,
    client_id BIGINT,
    practice_id BIGINT,
    status VARCHAR(50),
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    budget DECIMAL(12, 2),
    actual_cost DECIMAL(12, 2) DEFAULT 0,
    progress INTEGER DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
    project_manager_user_id BIGINT,
    created_by_user_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_projects_sow FOREIGN KEY (sow_id) REFERENCES sows(id) ON DELETE SET NULL,
    CONSTRAINT fk_projects_practice FOREIGN KEY (practice_id) REFERENCES practices(id) ON DELETE SET NULL
);

-- Create project_status_updates table
CREATE TABLE IF NOT EXISTS project_status_update (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    milestone VARCHAR(255),
    deliverables TEXT,
    progress INTEGER CHECK (progress >= 0 AND progress <= 100),
    risks TEXT,
    issues TEXT,
    updated_by VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_status_updates_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create lesson_learned table
CREATE TABLE IF NOT EXISTS lesson_learned (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    category VARCHAR(100),
    created_by VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    project_id BIGINT NOT NULL,
    CONSTRAINT fk_lesson_learned_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_projects_project_code ON projects(project_code);
CREATE INDEX IF NOT EXISTS idx_projects_sow_id ON projects(sow_id);
CREATE INDEX IF NOT EXISTS idx_projects_practice_id ON projects(practice_id);
CREATE INDEX IF NOT EXISTS idx_sows_sow_number ON sows(sow_number);
CREATE INDEX IF NOT EXISTS idx_project_status_update_project_id ON project_status_update(project_id);
CREATE INDEX IF NOT EXISTS idx_lesson_learned_project_id ON lesson_learned(project_id);
