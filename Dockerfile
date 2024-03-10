FROM openjdk:17
MAINTAINER Andrej Nikonov
COPY target/telegramgptbot-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
