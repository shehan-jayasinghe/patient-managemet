# ========= Build stage =========
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the Maven descriptor first to leverage build cache
COPY pom.xml ./
# Copy source
COPY src ./src

# Build the application (skip tests for faster container builds)
RUN mvn -B -DskipTests clean package

# ========= Runtime stage =========
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/patient-service-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application port
EXPOSE 4000

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
