FROM openjdk:11
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

EXPOSE 8080

ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENTRYPOINT ["java", "-jar", "/app.jar"]