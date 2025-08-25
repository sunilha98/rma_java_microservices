# Flyway Database Migration Setup

This directory contains Flyway migration scripts for the master resource management service.

## Directory Structure
```
src/main/resources/db/migration/
├── V1__Create_initial_schema.sql    # Initial database schema
├── V2__Add_sample_data.sql          # Sample data and views
└── README.md                        # This documentation
```

## Migration Files

### V1__Create_initial_schema.sql
Creates the initial database schema with the following tables:
- `resource_types`: Master data for resource type categorization
- `resources`: Main table for storing resource information
- `resource_attributes`: Flexible attributes for resources
- `resource_audit_log`: Audit trail for resource changes

### V2__Add_sample_data.sql
Adds sample data and creates:
- Sample resources across different types
- Sample resource attributes
- Resource summary view for reporting

## Configuration

The Flyway configuration is set in `application.yml`:
- **Enabled**: true
- **Locations**: classpath:db/migration
- **Baseline on migrate**: true
- **Validate on migrate**: true

## Usage

### Running Migrations
When the application starts, Flyway will automatically run any pending migrations.

### Manual Migration Commands
```bash
# Using Maven
mvn flyway:migrate

# Using Spring Boot
./mvnw spring-boot:run
```

### Creating New Migrations
1. Create a new SQL file following the naming convention: `V{version}__{description}.sql`
2. Place the file in `src/main/resources/db/migration/`
3. Increment the version number sequentially

### Migration Best Practices
- Always test migrations in a development environment first
- Use transactions for complex migrations
- Include rollback scripts for critical changes
- Document breaking changes in migration files

### Database Reset (Development Only)
```bash
# Drop and recreate database (PostgreSQL)
psql -U postgres -c "DROP DATABASE IF EXISTS master_service_db;"
psql -U postgres -c "CREATE DATABASE master_service_db;"
```

## Troubleshooting

### Common Issues
1. **Migration checksum mismatch**: Ensure no manual changes to migration files
2. **Database locked**: Check for active connections
3. **Version conflicts**: Verify migration version sequence

### Debug Mode
Enable Flyway debug logging:
```yaml
logging:
  level:
    org.flywaydb: DEBUG
```

## Database Schema

### Entity Relationships
```
resource_types (1) ---- (N) resources (1) ---- (N) resource_attributes
```

### Key Tables
- **resources**: Main entity with foreign key to resource_types
- **resource_attributes**: Key-value pairs linked to resources
- **resource_audit_log**: Tracks all changes to resources
