# Base image
FROM eclipse-temurin:21-jdk-ubi9-minimal

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
COPY target/app.jar app.jar

# Expose the port the application runs on
EXPOSE 8194

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]