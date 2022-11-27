# build
FROM gradle:7.5.1-jdk17 as builder
WORKDIR /build
COPY --chown=gradle:gradle src /build/src
COPY --chown=gradle:gradle build.gradle settings.gradle /build/
RUN gradle --no-daemon build

# runtime
FROM eclipse-temurin:17-jdk-jammy as runtime
EXPOSE 8081
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT java -jar app.jar