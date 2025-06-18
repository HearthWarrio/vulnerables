# vuln-portal-java/file-upload-service/Dockerfile
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/file-upload-service-0.0.1-SNAPSHOT.jar ./file-upload-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/file-upload-service.jar"]
