# ===============================
# Build stage
# ===============================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# 1️⃣ Gradle Wrapper 관련 파일 먼저 복사 (캐시 최적화)
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# 2️⃣ 실행 권한 부여
RUN chmod +x gradlew

# 3️⃣ 의존성 캐시
RUN ./gradlew dependencies --no-daemon || true

# 4️⃣ 소스 코드 복사
COPY src ./src

# 5️⃣ 빌드
RUN ./gradlew clean build -x test --no-daemon


# ===============================
# Run stage
# ===============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# 빌드된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]