FROM amazoncorretto:21-alpine-jdk
COPY target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
