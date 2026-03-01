# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the application
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=10s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]


