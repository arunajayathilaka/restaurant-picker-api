FROM openjdk:21-jdk-slim AS build

COPY /target/*.jar app.jar

EXPOSE 8080

CMD ["java","-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}", "-jar", "app.jar"]
