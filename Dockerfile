FROM openjdk:17-alpine
VOLUME /tmp
COPY build/libs/backendv2.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app.jar"]

