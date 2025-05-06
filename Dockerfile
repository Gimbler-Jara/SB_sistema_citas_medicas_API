# ===============================
# 🏗️ ETAPA DE CONSTRUCCIÓN
# ===============================

# Usamos una imagen base oficial de Java 17 para compilar el proyecto (Maven no es necesario si usas el wrapper)
FROM eclipse-temurin:17-jdk AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todos los archivos del proyecto local al contenedor (código fuente, mvnw, pom.xml, etc.)
COPY . .

# Da permisos de ejecución al archivo mvnw (wrapper de Maven)
RUN chmod +x ./mvnw

# Ejecuta el empaquetado de la aplicación sin ejecutar tests
RUN ./mvnw clean package -DskipTests

# ===============================
# 🚀 ETAPA FINAL DE EJECUCIÓN
# ===============================

# Usamos otra vez Java 17 pero esta vez solo para ejecutar el JAR (contenedor más limpio)
FROM eclipse-temurin:17-jdk

# Establece el directorio de trabajo donde se colocará el JAR
WORKDIR /app

# Copia el archivo .jar generado en la etapa de build a esta nueva etapa
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto 8080 (debe coincidir con tu application.properties)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
