# Stage 1: Build common module and services
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiar todos los POMs primero para cachear dependencias
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY processing-service/pom.xml processing-service/pom.xml
COPY reservation-service/pom.xml reservation-service/pom.xml

# Instalar common primero
RUN mvn -B -pl common install

# Descargar dependencias
RUN mvn dependency:go-offline -B -pl common,reservation-service,processing-service

# Copiar todo el código fuente
COPY . .

# Compilar el proyecto
RUN mvn clean install -DskipTests

# Stage 2: Reservation Service
FROM openjdk:17-jdk-slim AS reservation-service
COPY --from=build /app/reservation-service/target/reservation-service-*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 3: Processing Service
FROM openjdk:17-jdk-slim AS processing-service
COPY --from=build /app/processing-service/target/processing-service-*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]