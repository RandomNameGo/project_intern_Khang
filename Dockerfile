FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-ea-28-jdk-slim
COPY --from=build /target/project_internship_management-1.0-SNAPSHOT.jar /app/project_internship_management.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","project_internship_management.jar" ]
