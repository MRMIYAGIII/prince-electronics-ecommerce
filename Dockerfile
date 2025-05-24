

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/beta-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

CMD ["java", "-jar", "app.jar"]