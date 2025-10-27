FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy your already-built JAR into the container
COPY target/*.jar app.jar

# Set timezone (optional)
ENV TZ=Asia/Saigon

EXPOSE 3100

ENTRYPOINT ["java", "-jar", "app.jar"]
