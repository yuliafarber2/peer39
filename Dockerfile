# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the build jar file from the host machine into the container
COPY build/libs/web-text-extractor.jar /app/web-text-extractor.jar

# Make port 8080 available to the world outside the container
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "web-text-extractor.jar"]
