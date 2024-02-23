# Use a suitable Java image as the base image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /java/com/demo

# Copy the application JAR file into the container at /app
COPY target/Springdemo.jar /app/Springdemo.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "Springdemo.jar"]
