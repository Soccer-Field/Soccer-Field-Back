# ===============================
# Build stage
# ===============================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Gradle Wrapper (권한 포함해서 복사)
COPY --chmod=755 gradlew gradlew
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# 의존성 캐시
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사
COPY src ./src

# 빌드
RUN ./gradlew clean build -x test --no-daemon


# ===============================
# Run stage
# ===============================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
