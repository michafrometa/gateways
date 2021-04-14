FROM openjdk:11-alpine
COPY ./target/gateways-0.0.1-SNAPSHOT.jar gateways-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "gateways-0.0.1-SNAPSHOT.jar"]
