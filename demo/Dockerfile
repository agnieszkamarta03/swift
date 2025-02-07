# Dockerfile for Spring Boot application with Java 21 and Maven

# --- Stage 1: Build Stage ---
FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
COPY Interns_2025_SWIFT_CODES.xlsx .
RUN mvn dependency:go-offline -B -e

# Copy source code
COPY src ./src

# Package the application
RUN mvn package -DskipTests # Skipping tests for faster build in Docker, you might remove -DskipTests for production builds

# --- Stage 2: Runtime Stage ---
FROM eclipse-temurin:21-jammy

WORKDIR /app

# Copy the packaged JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]