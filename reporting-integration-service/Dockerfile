# ================================
# Stage 1: Build JAR using Maven
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom.xml first and download dependencies (cache layer)
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ================================
# Stage 2: Run the JAR
# ================================
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
