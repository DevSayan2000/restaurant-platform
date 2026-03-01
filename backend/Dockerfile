# Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only pom.xml first to cache dependency downloads
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source and build (dependencies are cached)
COPY src src
RUN mvn clean package -DskipTests -o

# Runtime stage - use JRE (smaller image)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]


