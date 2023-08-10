FROM openjdk:11
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENTRYPOINT ["java","-jar","/app.jar"]