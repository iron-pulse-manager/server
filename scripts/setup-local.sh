#!/bin/bash

# 로컬 개발 환경 셋업 스크립트 (로컬 MySQL 서버 사용)

echo "🚀 Fitness Management 로컬 환경 셋업을 시작합니다..."

# MySQL 설치 확인
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL이 설치되지 않았습니다."
    echo "   macOS: brew install mysql"
    echo "   Ubuntu: sudo apt-get install mysql-server"
    echo "   Windows: MySQL 공식 사이트에서 다운로드"
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

# MySQL 서버 상태 확인
echo "🔍 MySQL 서버 상태 확인 중..."

# MySQL 서버 실행 확인
if ! mysql -u root -e "SELECT 1;" &> /dev/null; then
    echo "❌ MySQL 서버가 실행되지 않았거나 접근할 수 없습니다."
    echo "   MySQL 서버를 시작해주세요:"
    echo "   macOS: brew services start mysql"
    echo "   Ubuntu: sudo systemctl start mysql"
    echo "   또는 root 비밀번호를 확인해주세요."
    exit 1
fi

# 데이터베이스 생성
echo "🗄️ 데이터베이스 생성 중..."
mysql -u root -e "CREATE DATABASE IF NOT EXISTS fitness_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 

echo "✅ MySQL 준비 완료"


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
echo "  1. 애플리케이션 실행: ./gradlew bootRun --args='--spring.profiles.active=local'"
echo "  2. 테스트 실행: ./gradlew test"
echo "  3. 빌드: ./gradlew build"
echo ""
echo "🌐 접속 정보:"
echo "  - 애플리케이션: http://localhost:8080"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "  - MySQL: localhost:3306 (database: fitness_db, user: root)"