# 🐳 Multi-stage Docker build for Spring Boot Application
# This approach creates smaller, more secure images by separating build and runtime environments

# ═══════════════════════════════════════════════════════════════════════════════════
# STAGE 1: BUILD STAGE    # ═══════════════════════════════════════════════════════════════════════════════════
    # Use Maven with OpenJDK 21 for building the application
    FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy Maven configuration files first (better caching)
# Docker caches layers, so if these files don't change, 
# this layer won't be rebuilt, speeding up subsequent builds
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .

# Make Maven wrapper executable (important for Linux containers)
RUN chmod +x mvnw

# Download dependencies (this creates a separate cached layer)
# If only source code changes but dependencies remain same,
# Docker won't re-download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src/ src/

# Copy package.json and build CSS with Tailwind (if needed)
COPY package*.json ./
COPY tailwind.config.js ./

# Install Node.js for Tailwind CSS build (if you need to build CSS during Docker build)
RUN apt-get update && apt-get install -y nodejs npm && \
    npm install && \
    npm run build:css || echo "CSS build failed or not needed"

# Build the Spring Boot application
# -DskipTests skips tests during build (tests should run in CI/CD pipeline)
# clean package ensures a fresh build
RUN ./mvnw clean package -DskipTests

# ═══════════════════════════════════════════════════════════════════════════════════
# STAGE 2: RUNTIME STAGE    # ═══════════════════════════════════════════════════════════════════════════════════
    # Use a smaller JRE image for running the application
    FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks (useful for container orchestration)
RUN apk add --no-cache curl

# Create a non-root user for security
# Running containers as root is a security risk
RUN addgroup -S appuser && adduser -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
# The JAR file is typically in target/ directory
COPY --from=build /app/target/*.jar app.jar

# Copy static resources if they're built during Docker build
COPY --from=build /app/src/main/resources/static/ /app/static/

# Change ownership to appuser
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the port your Spring Boot app runs on
EXPOSE 5000

# Health check to ensure the application is running
# This helps container orchestrators know if the container is healthy
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:5000/actuator/health || exit 1

# Environment variables with defaults
# These can be overridden when running the container
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Run the application
# exec form ensures proper signal handling for graceful shutdowns
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
