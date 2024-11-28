# Use a base image with JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Set a working directory inside the container
WORKDIR /app

# Copy the application JAR file to the container
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE}  spring-webflux-security.jar

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8080

# Set the default command to run the application
ENTRYPOINT ["java", "-jar", "spring-webflux-security.jar"]
