-- Create titles table
CREATE TABLE titles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    level VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_title_name UNIQUE (name)
);

-- Create skillsets table
CREATE TABLE skillsets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_skillset_name UNIQUE (name)
);

-- Create resources table
CREATE TABLE resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    title_id BIGINT,
    location_id BIGINT,
    practice_id BIGINT,
    experience DECIMAL(3,1),
    allocation_percentage INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    bench_status VARCHAR(50) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (title_id) REFERENCES titles(id)
);

-- Create resource_skillsets join table
CREATE TABLE resource_skillsets (
    resource_id BIGINT NOT NULL,
    skillset_id BIGINT NOT NULL,
    PRIMARY KEY (resource_id, skillset_id),
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE,
    FOREIGN KEY (skillset_id) REFERENCES skillsets(id) ON DELETE CASCADE
);

-- Create allocations table
CREATE TABLE allocations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    title_id BIGINT,
    allocation_percentage INTEGER,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    requested_user_id BIGINT,
    approved_user_id BIGINT,
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resource_id) REFERENCES resources(id),
    FOREIGN KEY (title_id) REFERENCES titles(id)
);

-- Create fulfillment_requests table
CREATE TABLE fulfillment_requests (
    id CHAR(36) PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title_id BIGINT,
    location_id BIGINT,
    status VARCHAR(50) DEFAULT 'OPEN',
    expected_closure DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shift_id BIGINT,
    experience DECIMAL(3,1) NOT NULL,
    positions INTEGER NOT NULL,
    FOREIGN KEY (title_id) REFERENCES titles(id)
);

-- Create fulfillment_skillsets join table
CREATE TABLE fulfillment_skillsets (
    fulfillment_id CHAR(36) NOT NULL,
    skillset_id BIGINT NOT NULL,
    PRIMARY KEY (fulfillment_id, skillset_id),
    FOREIGN KEY (fulfillment_id) REFERENCES fulfillment_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (skillset_id) REFERENCES skillsets(id) ON DELETE CASCADE
);

-- Create release_requests table
CREATE TABLE release_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    replacement_id BIGINT,
    reason VARCHAR(255),
    effective_date TIMESTAMP,
    notes TEXT,
    status VARCHAR(50),
    approval_comments TEXT,
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resource_id) REFERENCES resources(id),
    FOREIGN KEY (replacement_id) REFERENCES resources(id)
);