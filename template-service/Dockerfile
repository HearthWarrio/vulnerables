# vuln-portal-java/template-service/Dockerfile
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/template-service-0.0.1-SNAPSHOT.jar ./template-service.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app/template-service.jar"]
