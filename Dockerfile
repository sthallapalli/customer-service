FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY /target/customer-service-0.0.1-SNAPSHOT.jar /usr/src/customer-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/src/customer-service-0.0.1-SNAPSHOT.jar"]
