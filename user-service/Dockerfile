# vuln-portal-java/user-service/Dockerfile
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar ./user-service.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/user-service.jar"]
