FROM gradle:7.6-jdk21-alpine AS build

WORKDIR /app

COPY ./back/build/libs/*.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080