FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine AS run
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
COPY --from=build --chown=app:app /app/build/libs/*.jar app.jar

USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
