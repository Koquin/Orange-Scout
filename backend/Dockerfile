# Etapa 1 - Build
FROM gradle:8.0.2-jdk17 AS build
WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

# Etapa 2 - Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
