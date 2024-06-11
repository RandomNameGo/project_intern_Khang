FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-ea-28-jdk-slim
COPY --from=build /target/project_intership_management-main-1.0-SNAPSHOT.jar project_intership_management-main.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","project_internship_management.jar" ]
