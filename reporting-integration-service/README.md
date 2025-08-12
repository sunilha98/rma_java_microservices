# Resource Management API - Spring Boot Backend

## Overview

This is a complete Spring Boot backend implementation for the Resource Management Application, providing REST APIs for managing project resources, allocations, and dashboards with role-based authentication.

## Features

- **JWT Authentication & Authorization**
- **Role-based Access Control** (RBAC)
- **RESTful API Design**
- **PostgreSQL Database Integration**
- **Spring Security Configuration**
- **Comprehensive Entity Management**
- **Dashboard Metrics & Analytics**

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Security 6**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (JSON Web Tokens)**
- **Maven**

## Project Structure

```
src/main/java/com/resourcemanagement/
├── entity/                 # JPA Entity classes
│   ├── User.java
│   ├── Resource.java
│   ├── Project.java
│   ├── Allocation.java
│   ├── Client.java
│   ├── Location.java
│   ├── Title.java
│   ├── Practice.java
│   ├── Skillset.java
│   └── Sow.java
├── dto/                   # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── UserDTO.java
│   └── DashboardMetricsDTO.java
├── repository/            # JPA Repositories
│   ├── UserRepository.java
│   ├── ResourceRepository.java
│   ├── ProjectRepository.java
│   └── AllocationRepository.java
├── service/              # Business Logic Services
│   ├── AuthService.java
│   ├── DashboardService.java
│   └── UserDetailsServiceImpl.java
├── controller/           # REST Controllers
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── ProjectController.java
│   ├── ResourceController.java
│   └── AllocationController.java
├── security/            # Security Configuration
│   ├── SecurityConfig.java
│   ├── JwtUtils.java
│   ├── JwtAuthenticationEntryPoint.java
│   └── JwtRequestFilter.java
└── ResourceManagementApiApplication.java
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `GET /api/auth/me` - Get current user info

### Dashboard
- `GET /api/dashboard/metrics` - Get dashboard KPI metrics

### Projects
- `GET /api/projects` - List all projects
- `GET /api/projects/{id}` - Get project by ID
- `POST /api/projects` - Create new project
- `PUT /api/projects/{id}` - Update project

### Resources
- `GET /api/resources` - List all resources
- `GET /api/resources/bench` - List available resources
- `GET /api/resources/{id}` - Get resource by ID
- `POST /api/resources` - Create new resource
- `PUT /api/resources/{id}` - Update resource

### Allocations
- `GET /api/allocations` - List all allocations
- `GET /api/allocations/project/{projectId}` - Get allocations by project
- `GET /api/allocations/resource/{resourceId}` - Get allocations by resource
- `POST /api/allocations` - Create new allocation
- `PUT /api/allocations/{id}` - Update allocation

## User Roles & Permissions

### Role Hierarchy
1. **SUPER_ADMIN** - Full system access
2. **RMT** - Resource management and project oversight
3. **PROJECT_MANAGER** - Project management and team coordination
4. **RESOURCE** - Limited access to own information
5. **FINANCE_CONTROLLER** - Financial data access
6. **PRACTICE_HEAD** - Practice-specific management

### Permission Matrix
| Endpoint | SUPER_ADMIN | RMT | PROJECT_MANAGER | RESOURCE |
|----------|-------------|-----|-----------------|----------|
| Dashboard Metrics | ✅ | ✅ | ✅ | ❌ |
| Projects (Read) | ✅ | ✅ | ✅ | ❌ |
| Projects (Write) | ✅ | ✅ | ❌ | ❌ |
| Resources (Read) | ✅ | ✅ | ✅ | ❌ |
| Resources (Write) | ✅ | ✅ | ❌ | ❌ |
| Allocations (Read) | ✅ | ✅ | ✅ | ❌ |
| Allocations (Write) | ✅ | ✅ | ❌ | ❌ |

## Database Schema

### Core Entities
- **users** - Authentication and user management
- **resources** - Employee/contractor information
- **projects** - Project details and tracking
- **allocations** - Resource assignments to projects
- **sows** - Statements of Work
- **clients** - Client information

### Master Data
- **locations** - Office locations
- **titles** - Job titles and levels
- **skillsets** - Technical and functional skills
- **practices** - Business practices/departments

## Configuration

### Environment Variables
```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/resource_management
PGUSER=postgres
PGPASSWORD=password
JWT_SECRET=mySecretKey123456789012345678901234567890
PORT=8080
```

### Application Properties
Key configurations in `application.yml`:
- Database connection settings
- JWT token configuration
- Security settings
- JPA/Hibernate configuration

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Installation
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd java-backend
   ```

2. **Configure database**
   - Create PostgreSQL database
   - Update connection details in `application.yml`

3. **Set environment variables**
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:5432/resource_management
   export JWT_SECRET=your-secret-key
   ```

4. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Docker Setup (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/resource-management-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Security Features

### JWT Authentication
- Stateless authentication using JWT tokens
- Token expiration handling
- Secure token generation and validation

### Authorization
- Method-level security with `@PreAuthorize`
- Role-based access control
- CORS configuration for cross-origin requests

### Password Security
- BCrypt password hashing
- Strong password encoding

## Testing

### Sample Data
Create test users with hashed passwords:
```sql
INSERT INTO users (username, email, password, first_name, last_name, role) VALUES
('admin', 'admin@example.com', '$2b$10$...', 'Admin', 'User', 'SUPER_ADMIN'),
('rmt_user', 'rmt@example.com', '$2b$10$...', 'RMT', 'Manager', 'RMT'),
('pm_user', 'pm@example.com', '$2b$10$...', 'Project', 'Manager', 'PROJECT_MANAGER');
```

### API Testing
Use tools like Postman or curl to test endpoints:
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'

# Access protected endpoint
curl -X GET http://localhost:8080/api/dashboard/metrics \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Development Guidelines

### Code Style
- Follow Java naming conventions
- Use proper package structure
- Implement proper exception handling
- Add validation annotations

### Database
- Use JPA entities with proper relationships
- Implement audit fields (createdAt, updatedAt)
- Use appropriate data types and constraints

### API Design
- Follow RESTful principles
- Use appropriate HTTP status codes
- Implement proper error responses
- Document endpoints with clear descriptions

## Deployment

### Production Configuration
- Use environment-specific profiles
- Configure proper logging levels
- Set up health checks and monitoring
- Use production-grade database settings

### Performance Optimization
- Enable connection pooling
- Configure JPA batch processing
- Implement caching where appropriate
- Use pagination for large result sets

## Monitoring & Maintenance

### Health Checks
- Spring Boot Actuator endpoints
- Database connectivity monitoring
- Custom health indicators

### Logging
- Structured logging with appropriate levels
- Request/response logging for debugging
- Security event logging

This Spring Boot backend provides a robust, scalable foundation for the Resource Management Application with enterprise-grade security, comprehensive API coverage, and production-ready features.