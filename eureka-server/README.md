# Eureka Server - Resource Management Microservices

A Spring Boot Eureka Server implementation for service discovery in the Resource Management microservices architecture.

## Overview

This Eureka Server acts as the service registry for the Resource Management microservices ecosystem. It enables service discovery and registration, allowing microservices to find and communicate with each other dynamically.

## Features

- **Service Registration**: Microservices can register themselves with the Eureka Server
- **Service Discovery**: Enables dynamic discovery of available services
- **Health Monitoring**: Tracks the health status of registered services
- **Load Balancing**: Provides client-side load balancing capabilities
- **High Availability**: Supports peer-to-peer replication for production environments

## Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Service Discovery**: Netflix Eureka Server (Spring Cloud 2025.0.0)
- **Build Tool**: Maven
- **Java Version**: Java 17
- **Cloud Platform**: Spring Cloud Netflix

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Spring Boot 3.x compatible environment

## Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd eureka-server
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Using Java
java -jar target/eureka-server-*.jar
```

### 4. Access Eureka Dashboard
Once the server is running, you can access the Eureka dashboard at:
```
http://localhost:8761
```

## Configuration

### Application Properties
The server is configured through `application.yml`:

```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    wait-time-in-ms-when-sync-empty: 0
```

### Environment Variables
You can override configuration using environment variables:
- `SERVER_PORT`: Server port (default: 8761)
- `EUREKA_CLIENT_REGISTER_WITH_EUREKA`: Whether to register with Eureka (default: false)
- `EUREKA_CLIENT_FETCH_REGISTRY`: Whether to fetch registry (default: false)

## API Endpoints

### Eureka Server Endpoints
- `GET /`: Eureka dashboard
- `GET /eureka/apps`: List all registered applications
- `GET /eureka/apps/{appName}`: Get details for a specific application
- `POST /eureka/apps/{appName}`: Register a new application
- `PUT /eureka/apps/{appName}/{instanceId}`: Send heartbeat
- `DELETE /eureka/apps/{appName}/{instanceId}`: Unregister application

## Development

### Project Structure
```
eureka-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/resourcemanagement/eureka_server/
│   │   │       └── EurekaServerApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com/resourcemanagement/eureka_server/
│               └── EurekaServerApplicationTests.java
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

### Running Tests
```bash
mvn test
```

### Building Docker Image
```bash
mvn spring-boot:build-image
```

## Production Deployment

### High Availability Setup
For production environments, configure multiple Eureka servers in a peer-to-peer setup:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://eureka-peer1:8761/eureka/,http://eureka-peer2:8761/eureka/
```

### Health Checks
The Eureka server provides health check endpoints:
- `GET /actuator/health`: Application health status
- `GET /actuator/info`: Application information

## Monitoring

### Metrics
The server exposes metrics via:
- `GET /actuator/metrics`: Application metrics
- `GET /actuator/prometheus`: Prometheus-compatible metrics

### Logging
Logs are configured to output to console with the following format:
```
2024-01-15 10:30:45.123 INFO [eureka-server] [main] c.r.e.EurekaServerApplication : Started EurekaServerApplication in 3.456 seconds
```

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Change the port in `application.yml` or use `--server.port=8080`

2. **Services Not Registering**
   - Ensure services have correct `eureka.client.service-url.defaultZone`
   - Check network connectivity between services

3. **Eureka Dashboard Not Loading**
   - Verify the server is running on the correct port
   - Check firewall settings

### Debug Mode
Enable debug logging:
```bash
java -jar target/eureka-server-*.jar --debug
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is part of the Resource Management microservices suite. Please refer to the main repository for license information.

## Support

For support and questions, please contact the development team or create an issue in the project repository.
