FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 3100
ENV TZ=Asia/Ho_Chi_Minh

ENTRYPOINT ["java", "-jar", "app.jar"]
