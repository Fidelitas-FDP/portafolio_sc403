# Etapa 1: Compilación (Build)
FROM maven:3.9.16-eclipse-temurin-21-alpine AS build
WORKDIR /app

# cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# compilacion
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de Ejecución (Runtime), con jre en vez de jdk correspondientemente
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# usuario de seguridad no-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos el JAR desde la etapa de compilación
# El nombre 'app.jar' es un estándar para facilitar el despliegue
# se debe corresponder con <finalName>app</finalName> dentro de <build/> en pom para dejar como "/app/target/app.jar app.jar"
COPY --from=build /app/target/app.jar app.jar

# Exponemos el puerto definido en tu application.properties (80)
# Se salta porque render configura puerto dinamico que ya se inyecta en application.properties y expone contenedor en el mismo nativamente
# EXPOSE 8081

# Parámetros de optimización de memoria para contenedores
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
