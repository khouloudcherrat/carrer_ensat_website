FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY mvnw .
RUN chmod +x mvnw

COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8081
CMD ["java", "-jar", "target/EApp-0.0.1-SNAPSHOT.jar"]