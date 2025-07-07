FROM maven:3.9.6-amazoncorretto-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app

# Como a ideia é testar antes o projeto através de uma action no github, por exemplo. Nesse momento, será feita a criação do nosso container docker
RUN mvn clean install -DskipTests
# Application image


FROM openjdk:21-slim
COPY --from=build /app/target/api-ecommerce-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

