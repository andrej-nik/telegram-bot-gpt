FROM openjdk:17
MAINTAINER Andrej Nikonov
COPY target/telegramgptbot-1.0.jar app.jar
COPY telegram.org.chain.pem telegram.org.chain.pem
RUN $JAVA_HOME/bin/keytool -import -file telegram.org.chain.pem -alias telegram.org -keystore $JAVA_HOME/lib/security/cacerts -trustcacerts -storepass changeit -noprompt
RUN mkdir /logs
ENTRYPOINT ["java","-jar","/app.jar"]
