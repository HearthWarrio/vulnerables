FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/auth-service-1.0.0.jar ./auth-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/auth-service.jar"]