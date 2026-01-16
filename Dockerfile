FROM maven:3-amazoncorretto-21 AS maven
WORKDIR /usr/src/app
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /usr/src/app
COPY --from=maven /usr/src/app/infrastructure/target/challenge.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms512M", "-Xmx1024M", "-jar", "app.jar"]