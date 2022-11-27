FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8081
RUN mkdir /app
COPY build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar","/app/spring-boot-application.jar"]