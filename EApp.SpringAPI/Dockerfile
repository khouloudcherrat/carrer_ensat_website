# Build stage
FROM openjdk:17-jdk-alpine AS builder
WORKDIR /api
COPY . .
RUN ./mvnw clean package

# Runtime stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /api/target/EApp-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "EApp-0.0.1-SNAPSHOT.jar"]
