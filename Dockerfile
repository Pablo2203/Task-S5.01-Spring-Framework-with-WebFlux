# Etapa 1: Build de la aplicación
FROM maven:3.9-eclipse-temurin-21 AS builder

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo pom.xml y las dependencias de Maven primero
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto de la aplicación
COPY src ./src

# Construir el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para ejecutar la aplicación
FROM eclipse-temurin:21-jre

# Configuración del directorio
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto en el que correrá la app
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]