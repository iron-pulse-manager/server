#!/bin/bash

# 로컬 개발 환경 셋업 스크립트

echo "🚀 Fitness Management 로컬 환경 셋업을 시작합니다..."

# Docker 및 Docker Compose 설치 확인
if ! command -v docker &> /dev/null; then
    echo "❌ Docker가 설치되지 않았습니다. Docker를 먼저 설치해주세요."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose가 설치되지 않았습니다. Docker Compose를 먼저 설치해주세요."
    exit 1
fi

# Java 17 설치 확인
if ! command -v java &> /dev/null; then
    echo "❌ Java가 설치되지 않았습니다. Java 17을 먼저 설치해주세요."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 이상이 필요합니다. 현재 버전: $JAVA_VERSION"
    exit 1
fi

echo "✅ 사전 요구사항 확인 완료"

# Docker 컨테이너 정리
echo "🧹 기존 컨테이너 정리 중..."
docker-compose down -v

# 데이터베이스 및 Redis 시작
echo "🐳 데이터베이스 및 Redis 시작 중..."
docker-compose up -d mysql redis

# 데이터베이스 준비 대기
echo "⏳ MySQL 준비 대기 중..."
sleep 30

# 데이터베이스 연결 테스트
until docker-compose exec mysql mysqladmin ping -h localhost --silent; do
    echo "MySQL 연결 대기 중..."
    sleep 2
done

echo "✅ MySQL 준비 완료"

# Redis 연결 테스트
until docker-compose exec redis redis-cli ping; do
    echo "Redis 연결 대기 중..."
    sleep 2
done

echo "✅ Redis 준비 완료"

# Gradle 빌드
echo "🔨 애플리케이션 빌드 중..."
./gradlew clean build -x test

if [ $? -eq 0 ]; then
    echo "✅ 빌드 성공"
else
    echo "❌ 빌드 실패"
    exit 1
fi

echo "🎉 로컬 환경 셋업 완료!"
echo ""
echo "📋 다음 명령어로 개발을 시작하세요:"
echo "  1. 애플리케이션 실행: ./gradlew bootRun"
echo "  2. 전체 Docker 환경: docker-compose up"
echo "  3. 테스트 실행: ./gradlew test"
echo ""
echo "🌐 접속 정보:"
echo "  - 애플리케이션: http://localhost:8080"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "  - MySQL: localhost:3306 (user: fitness_user, password: fitness_password)"
echo "  - Redis: localhost:6379"