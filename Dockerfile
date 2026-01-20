FROM amazoncorretto:21-alpine-jdk
RUN apk add --no-cache bash
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
COPY target/genealogy-app.jar genealogy-app.jar
ENTRYPOINT ["/wait-for-it.sh", "eureka-server:8761", "-t", "60", "--", \
            "/wait-for-it.sh", "neo4j:7687", "-t", "60", "--", \
            "java", "-jar", "/genealogy-app.jar"]
