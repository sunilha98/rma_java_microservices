# ---------- Base Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Service build (one stage per service)
# ----------------- Eureka Server-----------------
FROM builder AS eureka-server-build
WORKDIR /app/eureka-server
COPY eureka-server/pom.xml .
COPY eureka-server/src ./src
RUN mvn -B -DskipTests clean package

# ----------------- API Gateway -----------------
FROM builder AS api-gateway-build
WORKDIR /app/api-gateway
COPY api-gateway/pom.xml .
COPY api-gateway/src ./src
RUN mvn -B -DskipTests clean package

# ----------------- User Management Service -----------------
FROM builder AS user-mgmt-service-build
WORKDIR /app/user-mgmt-service
COPY user-mgmt-service/pom.xml .
COPY user-mgmt-service/src ./src
RUN mvn -B -DskipTests clean package

# ----------------- Master Resource Service -----------------
FROM builder AS master-resource-service-build
WORKDIR /app/master-resource-service
COPY master-resource-service/pom.xml .
COPY master-resource-service/src ./src
RUN mvn -B -DskipTests clean package

# ----------------- Project SoW Service -----------------
FROM builder AS project-sow-service-build
WORKDIR /app/project-sow-service
COPY project-sow-service/pom.xml .
COPY project-sow-service/src ./src
RUN mvn -B -DskipTests clean package

# ----------------- Resource Allocation Service -----------------
FROM builder AS resource-allocation-service-build
WORKDIR /app/resource-allocation-service
COPY resource-allocation-service/pom.xml .
COPY resource-allocation-service/src ./src
RUN mvn -B -DskipTests clean package

# ----------------- Reporting Integration Service -----------------
FROM builder AS reporting-integration-service-build
WORKDIR /app/reporting-integration-service
COPY reporting-integration-service/pom.xml .
COPY reporting-integration-service/src ./src
RUN mvn -B -DskipTests clean package

# ---------- Runtime Images ----------
# Eureka Server
FROM openjdk:17-jdk-slim AS eureka-server
WORKDIR /app
COPY --from=eureka-server-build /app/eureka-server/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# API Gateway
FROM openjdk:17-jdk-slim AS api-gateway
WORKDIR /app
COPY --from=api-gateway-build /app/api-gateway/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# User Management Service
FROM openjdk:17-jdk-slim AS user-mgmt-service
WORKDIR /app
COPY --from=user-mgmt-service-build /app/user-mgmt-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# Master Resource Service
FROM openjdk:17-jdk-slim AS master-resource-service
WORKDIR /app
COPY --from=master-resource-service-build /app/master-resource-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# Project SoW Service
FROM openjdk:17-jdk-slim AS project-sow-service
WORKDIR /app
COPY --from=project-sow-service-build /app/project-sow-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

# Resource Allocation Service
FROM openjdk:17-jdk-slim AS resource-allocation-service
WORKDIR /app
COPY --from=resource-allocation-service-build /app/resource-allocation-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

#Reporting Integration Service
FROM openjdk:17-jdk-slim AS reporting-integration-service
WORKDIR /app
COPY --from=reporting-integration-service-build /app/reporting-integration-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]