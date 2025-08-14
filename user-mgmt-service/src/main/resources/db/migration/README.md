# Flyway Database Migrations

This directory contains all database migration scripts for the User Management Service.

## Migration Scripts

### V1__Create_User_Table.sql
- Creates the `users` table with all necessary columns
- Sets up indexes for performance optimization
- Creates a trigger to automatically update the `updated_at` timestamp

### V2__Insert_Seed_Data.sql
- Inserts initial seed data for testing purposes
- Includes 4 test users with different roles
- Uses bcrypt hashed passwords (password123 for all users)

## Running Migrations

Migrations are automatically executed when the application starts. Flyway will:
1. Create a `flyway_schema_history` table to track applied migrations
2. Apply any pending migrations in version order
3. Validate the database schema against the migration scripts

## Development Guidelines

### Adding New Migrations
1. Create a new SQL file with the naming convention: `V{version}__{description}.sql`
2. Increment the version number sequentially
3. Write idempotent SQL (safe to run multiple times)
4. Test migrations locally before committing

### Common Commands
```bash
# Check migration status
mvn flyway:info

# Validate migrations
mvn flyway:validate

# Repair migration checksums
mvn flyway:repair

# Clean database (WARNING: Destroys all data)
mvn flyway:clean
```

### Troubleshooting
- If migrations fail, check the `flyway_schema_history` table
- Ensure database connectivity in application.yml
- Verify PostgreSQL is running and accessible
- Check migration file naming conventions
