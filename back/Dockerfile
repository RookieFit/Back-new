FROM eclipse-temurin:21-jdk-alpine AS runtime

WORKDIR /app

COPY ./back/build/libs/*.jar /app/myapp.jar

RUN chmod +x /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080