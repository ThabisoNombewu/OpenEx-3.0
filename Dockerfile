# ---------- Build Stage ----------
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy the entire project
COPY . .

# Make Gradle executable
RUN chmod +x gradlew

# Build the Spring Boot JAR
RUN ./gradlew clean bootJar --no-daemon

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]