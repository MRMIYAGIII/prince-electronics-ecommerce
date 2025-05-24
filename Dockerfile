# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS builder

# Set working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application (this will generate the JAR in the target directory)
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/beta-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render will override this with the PORT environment variable)
EXPOSE 8084

# Run the application
CMD ["java", "-jar", "app.jar"]