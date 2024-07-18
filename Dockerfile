FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-ea-28-jdk-slim
COPY --from=build target/internmanagement-0.0.1-SNAPSHOT.jar project_intership_management-main.jar
EXPOSE 8090
ENTRYPOINT [ "java", "jar", "project_intership_management-main.jar" ]
