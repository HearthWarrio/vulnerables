# vuln-portal-java/xml-import-service/Dockerfile
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/xml-import-service-0.0.1-SNAPSHOT.jar ./xml-import-service.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/xml-import-service.jar"]
