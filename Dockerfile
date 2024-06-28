FROM openjdk:11-jre-slim

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/BackendApplication.jar"]

