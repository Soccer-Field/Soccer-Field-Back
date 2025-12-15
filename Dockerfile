# ===== 1) Build stage =====
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# 의존성 캐시 최적화
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
RUN ./gradlew --no-daemon dependencies || true

# 소스 복사 후 빌드
COPY . /app
RUN ./gradlew --no-daemon clean bootJar

# ===== 2) Run stage =====
FROM eclipse-temurin:17-jre
WORKDIR /app

# (선택) 보안/권한: non-root 유저
RUN useradd -m appuser
USER appuser

# 빌드 산출물 복사
COPY --from=build /app/build/libs/*.jar app.jar

# (선택) JVM 옵션은 환경변수로 주입 가능
ENV JAVA_OPTS=""

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]