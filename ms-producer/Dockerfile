FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/ms-producer-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]