# Use the official OpenJDK image for Java 8
FROM openjdk:8-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/accounting_system-1-docker.jar /app/counterparty-app.jar

# Expose the port your Spring Boot application is running on
EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "counterparty-app.jar"]
