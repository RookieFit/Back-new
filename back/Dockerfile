FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app

COPY ./back/build/libs/*.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080