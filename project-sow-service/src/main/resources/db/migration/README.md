# Flyway Database Migrations

This directory contains Flyway database migration scripts for the Project SOW Service.

## Migration Files

### V1__Create_initial_tables.sql
Creates the initial database schema with the following tables:
- `practices` - Stores practice/department information
- `sows` - Stores Statement of Work (SOW) records
- `projects` - Stores project information linked to SOWs and practices
- `project_status_updates` - Stores periodic status updates for projects
- `lesson_learned` - Stores lessons learned from projects

### V2__Insert_sample_data.sql
Inserts sample data for development and testing purposes:
- 5 sample practices
- 3 sample SOWs
- 3 sample projects
- 3 sample status updates
- 3 sample lessons learned

## Running Migrations

The migrations will automatically run when the Spring Boot application starts, as Flyway is configured in `application.yml`.

### Manual Migration Commands

If you need to run migrations manually, you can use the following Maven commands:

```bash
# Run all pending migrations
mvn flyway:migrate

# Validate migrations
mvn flyway:validate

# Display migration info
mvn flyway:info

# Clean the database (WARNING: This will drop all tables)
mvn flyway:clean
```

### Configuration

Flyway is configured in `src/main/resources/application.yml`:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
    out-of-order: false
    clean-disabled: true
```

## Adding New Migrations

1. Create a new SQL file following the naming convention: `V{version}__{description}.sql`
2. Place the file in the `src/main/resources/db/migration` directory
3. The version number should be higher than the last migration
4. Use descriptive names for the migration (e.g., `V3__Add_user_roles.sql`)

## Database Schema

### Entity Relationships

- **Projects** → **SOWs** (Many-to-One)
- **Projects** → **Practices** (Many-to-One)
- **ProjectStatusUpdates** → **Projects** (Many-to-One)
- **LessonLearned** → **Projects** (Many-to-One)

### Data Types Used

- `BIGSERIAL` - Auto-incrementing primary keys
- `VARCHAR(n)` - Variable-length strings
- `TEXT` - Long text fields
- `DECIMAL(12,2)` - Monetary values
- `INTEGER` - Numeric values
- `BOOLEAN` - True/false values
- `TIMESTAMP WITHOUT TIME ZONE` - Date and time values
- `ENUM` - Enumerated values (implemented as VARCHAR with CHECK constraints)

## Troubleshooting

### Common Issues

1. **Migration fails with "relation already exists"**
   - This usually means the database already has tables created by JPA/Hibernate
   - Solution: Drop the existing tables or use `spring.jpa.hibernate.ddl-auto=validate`

2. **Flyway validation fails**
   - Check that migration checksums match
   - Ensure no manual changes were made to already-run migrations

3. **Database connection issues**
   - Verify PostgreSQL is running on localhost:5432
   - Check database credentials in application.yml
   - Ensure the database `project_sow_db` exists
