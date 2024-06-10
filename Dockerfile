FROM ubuntu:latest
LABEL authors="Admin"



FROM maven:3.8.3-openjdk-17 as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.6-jdk-slim
COPY --from=build /target/project_intern-0.0.1-SNAPSHOT.jar demo.jar
ENTRYPOINT ["java","-jar","demo.jar"]