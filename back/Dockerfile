# 1. 빌드 이미지를 설정
FROM eclipse-temurin:21-jdk-alpine as build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 또는 Maven 의존성 복사 및 빌드
COPY ./build.gradle ./settings.gradle ./gradle/wrapper/gradle-wrapper.jar ./gradlew /app/
RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

# 4. 실제 애플리케이션을 실행할 이미지를 설정 (JRE)
FROM eclipse-temurin:21-jre-alpine

# 5. 빌드한 JAR 파일을 컨테이너로 복사
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 6. 포트 설정 (Spring Boot의 기본 포트 8080)
EXPOSE 8080

# 7. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]