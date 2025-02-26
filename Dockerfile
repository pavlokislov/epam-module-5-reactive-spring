FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Item-app-0.0.1-SNAPSHOT.jar item-app.jar

EXPOSE 8080 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app/item-app.jar"]

