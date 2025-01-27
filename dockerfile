FROM gradle:8-jdk21 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x ./gradlew

COPY src src

RUN ./gradlew bootJar

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]