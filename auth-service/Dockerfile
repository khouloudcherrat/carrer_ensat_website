# Build stage
FROM openjdk:17-jdk-alpine AS builder
WORKDIR /api
COPY . .
RUN ./mvnw clean package

# Runtime stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /api .
EXPOSE 8081
CMD ["./mvnw", "spring-boot:run"]
