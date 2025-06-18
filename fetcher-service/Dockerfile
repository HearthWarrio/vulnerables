# vuln-portal-java/fetcher-service/Dockerfile
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/fetcher-service-0.0.1-SNAPSHOT.jar ./fetcher-service.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/app/fetcher-service.jar"]
