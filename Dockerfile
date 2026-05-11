# Utiliza una imagen de Maven para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copia los archivos de configuración de Maven
COPY pom.xml .
# Copia el resto del código fuente
COPY src ./src
# Ejecuta Maven para empaquetar la aplicación (omite los tests para acelerar)
RUN mvn clean package -DskipTests

# Utiliza una imagen más ligera de Java para ejecutar la aplicación
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia el archivo .jar generado en la fase anterior
COPY --from=build /app/target/*.jar app.jar
# Expone el puerto en el que corre la aplicación
EXPOSE 8080
# Define el comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]