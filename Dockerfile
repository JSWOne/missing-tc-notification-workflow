FROM maven:3.8.3-openjdk-17

WORKDIR /app

COPY target/missing-tc-notification-workflow-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
