# 프로젝트 재설계 요약

## 변경된 데이터베이스 스키마 기반 프로젝트 재설계

### 📋 주요 변경사항

#### 1. 엔티티 구조 개선
- **Auth-User 분리**: 인증 정보와 사용자 정보를 분리하여 다양한 로그인 방식 지원
- **통합 User 엔티티**: 사장님, 직원, 회원을 하나의 User 엔티티로 통합
- **Business 중심 설계**: 사업장을 중심으로 한 관계 설정

#### 2. 새로 생성된 핵심 엔티티

##### 2.1 인증 및 사용자 관리
```java
// Auth 엔티티 - 인증 정보 관리
- 일반 로그인 (username/password)
- 카카오 로그인 (kakao_id)  
- 애플 로그인 (apple_id)

// User 엔티티 - 사용자 기본 정보
- 이름, 생년월일, 성별, 연락처 등
- Auth와 1:1 관계
```

##### 2.2 사업장 관리
```java
// Business 엔티티 - 사업장 정보
- 사업장명, 사업자번호, 주소 등
- User(사장님)와 N:1 관계

// BusinessEmployee 엔티티 - 사업장-직원 관계
- 직원의 사업장 소속 정보
- 근무시간, 직책, 계좌정보 등

// BusinessMember 엔티티 - 사업장-회원 관계  
- 회원의 사업장 등록 정보
- 담당 직원, SMS 수신동의 등
```

##### 2.3 결제 및 이용권 관리
```java
// Payment 엔티티 - 결제 정보
- 상품 결제 내역
- 미수금 관리 기능 포함

// Membership 엔티티 - 이용권 정보
- Payment와 1:1 관계
- 서비스 기간, 락커 배정 등
```

#### 3. BaseEntity 개선
```java
// 모든 엔티티의 공통 컬럼
- created_at, created_by (생성 정보)
- updated_at, updated_by (수정 정보)  
- deleted_at, deleted_by (소프트 삭제)
```

### 🏗️ 패키지 구조

```
src/main/java/com/fitness/
├── common/
│   ├── BaseEntity.java (공통 엔티티)
│   ├── config/ (설정 클래스들)
│   ├── exception/ (예외 처리)
│   └── util/ (유틸리티)
├── domain/
│   ├── auth/ (인증 관리)
│   ├── user/ (사용자 관리)  
│   ├── business/ (사업장 관리)
│   ├── employee/ (직원 관리)
│   ├── member/ (회원 관리)
│   ├── payment/ (결제 관리)
│   ├── membership/ (이용권 관리)
│   ├── product/ (상품 관리)
│   ├── locker/ (락커 관리)
│   ├── schedule/ (일정 관리)
│   ├── workout/ (운동일지)
│   ├── notification/ (알림)
│   └── statistics/ (통계)
└── FitnessManagementApplication.java
```

### 🔗 주요 관계 매핑

#### 1. 인증 및 사용자 관련
- Auth ↔ User (1:1)
- User ↔ Business (1:N) - 사업주로서
- User ↔ BusinessEmployee (1:N) - 직원으로서  
- User ↔ BusinessMember (1:N) - 회원으로서

#### 2. 사업장 관련
- Business ↔ BusinessEmployee (1:N)
- Business ↔ BusinessMember (1:N)
- Business ↔ Product (1:N)
- Business ↔ Locker (1:N)

#### 3. 결제 및 이용권 관련
- Payment ↔ Membership (1:1)
- Membership ↔ Locker (1:1) - 락커 이용권인 경우
- Product ↔ Payment (1:N)

### 📊 새로운 스키마의 장점

1. **확장성**: 다양한 로그인 방식 지원 가능
2. **유연성**: 사용자가 여러 역할을 동시에 가질 수 있음
3. **명확성**: 사업장 중심의 명확한 관계 설정
4. **추적성**: 모든 데이터의 생성/수정/삭제 이력 관리
5. **데이터 무결성**: 소프트 삭제로 데이터 무결성 보장

### 🚀 다음 단계

1. ✅ 기본 엔티티 구조 완성
2. 🔄 Repository 인터페이스 구현
3. 🔄 Service 레이어 구현
4. 🔄 Controller 및 API 구현
5. 🔄 DTO 클래스 구현
6. 🔄 예외 처리 및 검증 로직
7. 🔄 보안 설정 및 JWT 인증
8. 🔄 테스트 코드 작성

### 🎯 핵심 비즈니스 로직

#### 회원 관리 플로우
1. 사용자 회원가입 (카카오/애플 로그인)
2. QR코드를 통한 사업장 연결
3. 상품 구매 및 이용권 생성
4. 출석 체크 및 서비스 이용

#### 직원 관리 플로우
1. 직원 회원가입 (일반 로그인)
2. 사업장 연결 신청
3. 사장님 승인/거절
4. 승인 시 직원으로 활동 시작

#### 사장님 관리 플로우
1. 개발자가 직접 계정 생성
2. 사업장 정보 등록
3. 직원 가입 승인/거절
4. 회원 관리 및 매출 관리

이 재설계를 통해 더욱 확장 가능하고 유지보수가 용이한 시스템 구조를 구축했습니다.