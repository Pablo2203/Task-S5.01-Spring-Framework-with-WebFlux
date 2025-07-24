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

                          # Exponer el puerto dinámico (usado en Clever Cloud)
                          EXPOSE 8081

                          # Agregar argumentos para variables de entorno relacionadas con Clever Cloud
                          ARG PORT
                          ENV PORT=${PORT}

                          # Variables de entorno para la conexión MySQL
                          ARG MYSQL_ADDON_HOST
                          ENV MYSQL_ADDON_HOST=${MYSQL_ADDON_HOST}
                          ARG MYSQL_ADDON_PORT
                          ENV MYSQL_ADDON_PORT=${MYSQL_ADDON_PORT}
                          ARG MYSQL_ADDON_DB
                          ENV MYSQL_ADDON_DB=${MYSQL_ADDON_DB}
                          ARG MYSQL_ADDON_USER
                          ENV MYSQL_ADDON_USER=${MYSQL_ADDON_USER}
                          ARG MYSQL_ADDON_PASSWORD
                          ENV MYSQL_ADDON_PASSWORD=${MYSQL_ADDON_PASSWORD}

                          # Variables de entorno para la conexión a MongoDB Atlas
                          ARG MONGO_USER
                          ENV MONGO_USER=${MONGO_USER}
                          ARG MONGO_PASSWORD
                          ENV MONGO_PASSWORD=${MONGO_PASSWORD}
                          ARG MONGO_DB
                          ENV MONGO_DB=${MONGO_DB}

                          # Comando para ejecutar la aplicación
                          ENTRYPOINT ["java", "-jar", "app.jar"]