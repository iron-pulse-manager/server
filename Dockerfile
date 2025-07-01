# 멀티 스테이지 빌드를 사용하여 이미지 크기 최적화
FROM gradle:8.5-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼와 빌드 스크립트 복사
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# 모든 모듈의 빌드 스크립트 복사
COPY */build.gradle ./modules/
COPY common/ common/
COPY user/ user/
COPY member/ member/
COPY business/ business/
COPY employee/ employee/
COPY product/ product/
COPY payment/ payment/
COPY schedule/ schedule/
COPY statistics/ statistics/
COPY community/ community/

# 소스 코드 복사
COPY src/ src/

# Gradle 캐시를 위한 의존성 다운로드
RUN ./gradlew dependencies --no-daemon

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

# 런타임 이미지
FROM openjdk:17-jre-slim

# 애플리케이션 실행을 위한 사용자 생성
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 사용자로 변경
RUN chown appuser:appuser /app/app.jar
USER appuser

# 포트 노출
EXPOSE 8080

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# JVM 옵션 설정 (환경변수로 오버라이드 가능)
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication"
CMD ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]