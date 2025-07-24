# Use OpenJDK 17 slim as the base image
FROM openjdk:17-jdk-slim

# Metadata
LABEL maintainer="yourname@example.com"
LABEL project="EventApp"

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR file into the image
COPY target/EventApp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# JVM options can be added here if needed
# ENV JAVA_OPTS=""

# Define default command to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
