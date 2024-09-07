# Use an official Maven image to build the project
FROM maven:3.9.2-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and all module-specific pom.xml files
COPY pom.xml /app/
COPY proto/pom.xml /app/proto/
COPY grpc-client/pom.xml /app/grpc-client/

# Download all the dependencies before copying the source code to improve build caching
RUN mvn dependency:go-offline -B

# Copy the source code for the modules
COPY proto /app/proto/
COPY grpc-client /app/grpc-client/

# Build the project
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the Spring Boot app
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/grpc-client/target/grpc-client-0.0.1-SNAPSHOT.jar /app/core.jar

# Expose the application's port
EXPOSE 8080

# Set the entry point to run the app
ENTRYPOINT ["java", "-jar", "/app/core.jar"]
