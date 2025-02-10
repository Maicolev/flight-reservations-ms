
# Proyecto de Reserva de Vuelos - Microservicios

Este proyecto tiene como objetivo gestionar las reservas de vuelos mediante una arquitectura de microservicios. Incluye dos microservicios: un servicio de **reservas** y un servicio de **procesamiento**. Los servicios se comunican mediante colas de mensajería y se encargan de validar la entrada, procesar la reserva y almacenarla en una base de datos. 

## Propósito del Proyecto

El proyecto se encarga de:

- Validar el formato y la existencia de la reserva antes de enviarla a la cola.
- Procesar las reservas de vuelo, confirmarlas y almacenarlas en la base de datos.
- Eliminarlas y consultar vuelos
- Utiliza colas para asegurar la correcta entrega y procesamiento de las reservas.

## Tecnologías y Dependencias
### Tecnologías Utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Docker

### Dependencias Principales
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-amqp
- postgresql
- hibernate-core
- jackson-databind

## Endpoints

### Microservicio de Reservas (Reserva Service)

- **POST** `/api/reservations`:
  - Crea una nueva reserva.
  - Ejemplo de solicitud:
    ```json
    {
      "flightId": 1,
      "seatNumber": "1A",
      "email": "ASDASD.doe@example.com"
    }
    ```

### Microservicio de Procesamiento (Processing Service)

- **GET** `/api/processing/confirmed`: 
  - Este endpoint se encarga de procesar las reservas confirmadas.
  
- **DELETE** `/api/processing/confirmed`: 
  - Elimina un procesamiento confirmado.
  - Requiere un parámetro `flightId` para eliminar el procesamiento asociado.te endpoint se encarga de procesar las reservas confirmadas.
  
## Colas

El sistema utiliza tres colas para manejar las reservas:

1. **reservations.pending**: Cola de reservas pendientes.
2. **reservations.confirmed**: Cola de reservas confirmadas.
3. **reservations.errors**: Cola de errores para las reservas fallidas.

## Configuración de Docker

El proyecto incluye un archivo Docker con dos perfiles: uno para ejecución local y otro para Docker. El Dockerfile está dividido en tres etapas:

1. **Stage 1: Construcción del módulo común y servicios**
2. **Stage 2: Servicio de Reservas**
3. **Stage 3: Servicio de Procesamiento**

### Dockerfile

```dockerfile
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
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 3: Processing Service
FROM openjdk:17-jdk-slim AS processing-service
COPY --from=build /app/processing-service/target/processing-service-*.jar /app/app.jar
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

## Base de Datos

El proyecto utiliza PostgreSQL como base de datos, y las conexiones están configuradas tanto para el entorno local como para Docker.

```java
@Bean
@ConfigurationProperties(prefix = "spring.datasource")
@Profile("local")
public DataSource postgresDataSourceLocal() {
    return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url("jdbc:postgresql://localhost:5432/reservations")
            .username("postgres")
            .password("postgres")
            .build();
}

@Bean
@ConfigurationProperties(prefix = "spring.datasource")
@Profile("docker")
public DataSource postgresDataSourceDocker() {
    return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url("jdbc:postgresql://flight-reservations-ms-postgres-1:5432/reservations")
            .username("postgres")
            .password("postgres")
            .build();
}
```

## Dependencias y Tecnologías

- **Spring Boot**: Framework principal para construir los microservicios.
- **RabbitMQ**: Para la mensajería entre servicios mediante colas.
- **PostgreSQL**: Base de datos para almacenar las reservas.
- **Docker**: Para la construcción y despliegue del proyecto en contenedores.
- **Maven**: Herramienta de construcción para gestionar las dependencias.

## Instrucciones para Ejecutar el Proyecto

### Local

1. Clona el repositorio.
2. Ejecuta los servicios de forma local usando los perfiles locales de Spring.
3. Ejecuta las siguientes instrucciones para iniciar el servicio de reservas y el servicio de procesamiento:

```bash
# Para el servicio de reservas
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Para el servicio de procesamiento
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Docker

1. Construye y ejecuta el proyecto usando Docker con los siguientes comandos:

```bash
# Construir la imagen de Docker
docker-compose build

# Iniciar los contenedores de Docker
docker-compose up
```

2. Accede a los servicios en los siguientes puertos:

- Servicio de reservas: `http://localhost:8080`
- Servicio de procesamiento: `http://localhost:8081`

## Datos de Prueba

En el proyecto se incluye un archivo `init.sql` con datos de prueba para cargar en la base de datos.

## Diagramas

### Arquitectura del Proyecto

```plaintext
   +--------------------+         +-------------------+         +---------------------+
   | Microservicio de    |         | Cola de Mensajería |         | Microservicio de     |
   | Reservas           +--------->  reservations      +--------->  Procesamiento       |
   | (Reserva Service)  |         | .pending           |         | (Processing Service) |
   +--------------------+         +-------------------+         +---------------------+
```

Este gráfico muestra cómo los microservicios se comunican a través de la cola `reservations.pending`.


## Contribuciones

Si deseas contribuir al proyecto, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature/nueva-caracteristica`).
3. Haz tus cambios.
4. Haz commit de tus cambios (`git commit -m 'Agregando nueva característica'`).
5. Haz push a la rama (`git push origin feature/nueva-caracteristica`).
6. Crea un pull request.

---

## Licencia

Este proyecto está bajo la Licencia MIT - consulta el archivo [LICENSE](LICENSE) para más detalles.
