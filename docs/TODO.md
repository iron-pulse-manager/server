# 🏋️ 피트니스 관리 시스템 - TODO 리스트

## 📋 프로젝트 현황 (2024년 6월 22일 기준)

### ✅ **완료된 작업 (Phase 0: 설계 단계 - 100% 완료)**

#### **1. 도메인 설계 및 아키텍처**
- [x] 현재 프로젝트 구조 파악 및 기존 도메인 분석
- [x] 비즈니스 도메인 식별 및 분류 (16개 도메인)
- [x] DDD 기반 핵심 도메인 엔티티 설계 (Owner, Employee, Member, Business, Product, Payment)
- [x] 나머지 도메인 엔티티 설계 (일일권, 락커, 지출, 메시지, 운동일지, 식단, 체중, 출석, 일정, 통계, 커뮤니티)
- [x] 도메인 간 관계 설계 완료 (도메인 경계, 집계 루트, 순환 참조 방지, 트랜잭션 경계)
- [x] 6단계 계층형 패키지 구조 설계

#### **2. 프로젝트 재구축**
- [x] 기존 프로젝트 완전 삭제 및 새로운 도메인 기반 프로젝트 생성
- [x] 16개 모듈 생성 및 의존성 설정 (settings.gradle)
- [x] 핵심 엔티티 생성 (Owner, Employee, Member, Business, Product, Payment)
- [x] 도메인 서비스 및 리포지토리 설계
- [x] 애플리케이션 서비스 및 DTO 구현

#### **3. 데이터베이스 설계**
- [x] ERD 작성 및 테이블 관계 설계
- [x] DDL 스크립트 생성 (schema.sql)
- [x] 인덱스 설계 (성능 최적화용)
- [x] 초기 데이터 준비 (data.sql)
- [x] 데이터베이스 스키마 문서화 (database-schema.md)

#### **4. 시스템 문서화**
- [x] 시스템 아키텍처 설계 문서 작성 (SYSTEM_ARCHITECTURE.md)
- [x] 20주 개발 로드맵 작성 (DEVELOPMENT_ROADMAP.md)
- [x] API 설계 가이드라인 및 테스트 전략 수립
- [x] 배포 전략 및 운영 계획 수립

#### **5. 핵심 설계 결정사항 확정**
- [x] **사용자 분리**: User 통합 테이블 → Owner/Employee/Member 완전 분리
- [x] **통합 결제**: 모든 상품 타입을 Payment 테이블에서 통합 관리
- [x] **OneToMany 컬렉션 제거**: 성능을 위해 양방향 관계 최소화
- [x] **순환 의존성 방지**: 6단계 계층형 모듈 구조로 해결

---

## 🚀 **다음 단계: 개발 시작 (Phase 1-4)**

### **🎯 즉시 시작 가능한 작업 (우선순위: 최고)**

#### **1. 개발 환경 구축**
- [ ] Docker 개발 환경 설정
  - [ ] MySQL 8.0 컨테이너 설정
  - [ ] Redis 컨테이너 설정  
  - [ ] Docker Compose 파일 작성
- [ ] application.yml 환경별 설정 (local, dev, prod)
- [ ] GitHub Actions CI/CD 기본 파이프라인 설정
- [ ] 프로젝트 빌드 검증 (`./gradlew build`)

#### **2. Phase 1 Week 1: Owner 도메인 구현 (1주차)**
- [ ] **Owner 엔티티 비즈니스 로직 완성**
  ```java
  // 완료해야 할 메서드들
  - Owner.changePassword()
  - Owner.isActive()
  - Owner.updateProfile()
  ```
- [ ] **OwnerRepository 구현**
  - [ ] 기본 CRUD 메서드
  - [ ] `findByUsername()` - 로그인용
  - [ ] `findActiveOwnerById()` - 활성 사장님 조회
- [ ] **OwnerApplicationService 구현**
  - [ ] 로그인 처리 로직
  - [ ] 프로필 수정 로직
  - [ ] 비밀번호 변경 로직
- [ ] **JWT 토큰 기반 인증 시스템**
  - [ ] JwtTokenProvider 구현
  - [ ] OwnerAuthenticationService 구현
  - [ ] Spring Security 기본 설정
- [ ] **Owner REST API 구현**
  ```
  POST /api/v1/auth/owner/login
  GET  /api/v1/owners/profile  
  PUT  /api/v1/owners/profile
  POST /api/v1/auth/owner/change-password
  ```
- [ ] **Owner 도메인 단위 테스트 작성** (커버리지 80% 이상)

#### **3. Phase 1 Week 2: Employee & Member 도메인 구현 (2주차)**
- [ ] **Employee 도메인 구현**
  - [ ] Employee 가입 승인 프로세스 구현
  - [ ] EmployeeApplicationService 구현
  - [ ] EmployeeApprovalService 구현 (사장님 승인 로직)
  - [ ] Employee REST API 구현
- [ ] **Member 도메인 구현**
  - [ ] Member OAuth 연동 구현 (카카오/애플)
  - [ ] MemberApplicationService 구현
  - [ ] BusinessMember N:M 관계 엔티티 구현
  - [ ] Member REST API 구현
- [ ] **OAuth 로그인 연동**
  - [ ] 카카오 OAuth 설정
  - [ ] 애플 OAuth 설정
  - [ ] OAuth 인증 컨트롤러 구현

---

### **📅 단계별 개발 계획 (Phase 1-4)**

#### **Phase 1: 핵심 도메인 구축 (8주) - 우선순위: 최고**
- **Week 1-2**: 사용자 관리 (Owner, Employee, Member) ← **현재 여기**
- **Week 3-4**: 사업장 및 상품 관리 (Business, Product)
- **Week 5-6**: 통합 결제 시스템 (Payment)
- **Week 7-8**: 회원 관리 통합 (Member 상세 기능)

#### **Phase 2: 운영 기능 확장 (6주) - 우선순위: 높음**
- **Week 9-10**: 일정 및 출석 관리 (Schedule, Attendance)
- **Week 11-12**: 락커 및 일일권 관리 (Locker, DayPass)
- **Week 13-14**: 메시지 발송 및 기본 통계 (Message, Statistics)

#### **Phase 3: 앱 특화 기능 (4주) - 우선순위: 중간**
- **Week 15-16**: 운동일지 및 식단 관리 (Workout, Diet)
- **Week 17-18**: 체중 관리 및 커뮤니티 (Weight, Community)

#### **Phase 4: 최적화 및 배포 (2주) - 우선순위: 중간**
- **Week 19-20**: 성능 최적화 및 프로덕션 배포

---

## 🛠️ **지속적으로 진행할 작업**

### **테스트 작성 (각 도메인 구현과 동시에)**
- [ ] 단위 테스트 작성 (목표: 80% 커버리지)
- [ ] 통합 테스트 작성 (Repository, Service 레이어)
- [ ] E2E 테스트 작성 (TestContainers 사용)
- [ ] 성능 테스트 작성 (부하 테스트)

### **문서화 (주간 단위로)**
- [ ] API 문서 작성 (Swagger/OpenAPI)
- [ ] 도메인 모델 문서 업데이트
- [ ] 개발자 가이드 작성
- [ ] 사용자 매뉴얼 작성

### **보안 및 품질 관리**
- [ ] SonarQube 코드 품질 검사 설정
- [ ] 보안 취약점 검사 설정
- [ ] 코드 리뷰 프로세스 구축
- [ ] 데이터 암호화 적용

---

## 🎯 **마일스톤 및 릴리즈 계획**

### **Alpha Release (8주 후 - Phase 1 완료)**
**목표**: 기본적인 사업장 운영이 가능한 수준
- ✅ 사용자 관리 (사장님, 직원, 회원)
- ✅ 사업장 및 상품 관리  
- ✅ 기본 결제 시스템
- ✅ 회원 관리 기본 기능

### **Beta Release (14주 후 - Phase 2 완료)**
**목표**: 완전한 사업장 운영 시스템
- ✅ Alpha 기능 + 운영 관리 기능
- ✅ 일정 및 출석 관리
- ✅ 락커 및 일일권 관리
- ✅ 메시지 발송 및 통계

### **RC Release (18주 후 - Phase 3 완료)**
**목표**: 앱 특화 기능 포함 완전체
- ✅ Beta 기능 + 앱 특화 기능
- ✅ 운동일지 및 식단 관리
- ✅ 체중 관리
- ✅ 커뮤니티 기능

### **Production Release (20주 후 - Phase 4 완료)**
**목표**: 프로덕션 배포 가능한 완성품
- ✅ RC 기능 + 성능 최적화
- ✅ 완전한 모니터링 시스템
- ✅ 프로덕션 배포 환경

---

## 🔧 **현재 프로젝트 상태**

### **생성된 모듈 (16개)**
```
Level 1: common/
Level 2: owner/, employee/, member/
Level 3: business/, product/, payment/
Level 4: schedule/, attendance/, locker/, daypass/, expense/
Level 5: workout/, diet/, weight/, message/
Level 6: statistics/, community/
```

### **완성된 핵심 엔티티**
- [x] Owner (사장님) - 로그인, 프로필 관리
- [x] Employee (직원) - 가입 승인, 상태 관리  
- [x] Member (회원) - OAuth 로그인, 트레이너 배정
- [x] Business (사업장) - 승인 프로세스
- [x] Product (상품) - 4가지 타입 지원
- [x] Payment (결제) - 통합 결제 시스템

### **데이터베이스**
- [x] schema.sql - 전체 DDL 스크립트
- [x] data.sql - 샘플 데이터
- [x] database-schema.md - ERD 및 설계 문서

### **설계 문서**
- [x] SYSTEM_ARCHITECTURE.md - 시스템 아키텍처
- [x] DEVELOPMENT_ROADMAP.md - 20주 개발 계획
- [x] CLAUDE.md - 비즈니스 요구사항

---

## 💡 **다음 액션 아이템 (이번 주)**

### **1. 환경 구축 (1-2일)**
1. Docker 환경 설정
2. MySQL 연결 확인
3. 프로젝트 빌드 검증
4. GitHub Actions 기본 설정

### **2. Owner 도메인 구현 시작 (3-5일)**
1. Owner 엔티티 비즈니스 로직 완성
2. OwnerRepository 구현
3. JWT 인증 시스템 구축
4. Owner REST API 구현
5. 단위 테스트 작성

### **3. 다음 주 준비**
1. Employee 도메인 구현 계획 수립
2. OAuth 연동 준비 (카카오/애플 개발자 계정)
3. 프론트엔드 개발 시작 준비

---

## ⚠️ **주의사항 및 리스크**

### **기술적 리스크**
- **OAuth 연동 복잡성**: 카카오/애플 OAuth 정책 변경 가능성
- **성능 이슈**: 복잡한 통계 쿼리 최적화 필요
- **데이터 무결성**: 복잡한 결제 로직 검증 필요

### **일정 리스크**  
- **요구사항 변경**: 비즈니스 요구사항 추가/변경 가능성
- **기술적 난이도**: 예상보다 복잡한 구현 부분 발생 가능
- **테스트 작성 시간**: 높은 커버리지 목표로 인한 개발 시간 증가

### **운영 리스크**
- **확장성**: 사용자 증가 시 성능 이슈
- **보안**: 개인정보보호법 준수 필요
- **백업**: 중요 데이터 손실 방지 전략 필요

---

## 📊 **성공 지표 (KPI)**

### **개발 지표**
- **코드 커버리지**: 80% 이상 유지
- **API 응답시간**: 95% 요청 < 500ms  
- **배포 성공률**: 95% 이상
- **버그 발생률**: 주요 기능 0건, 부가 기능 월 5건 이하

### **비즈니스 지표**
- **사용자 만족도**: 4.0/5.0 이상
- **기능 활용률**: 핵심 기능 80% 이상 사용
- **시스템 가용성**: 99.9% 이상
- **신규 사용자 증가율**: 월 10% 이상

---

## 📞 **참고사항**

### **프로젝트 정보**
- **프로젝트 루트**: `/Users/jonghne/personal/server`
- **주요 문서 위치**:
  - 비즈니스 요구사항: `CLAUDE.md`
  - 시스템 아키텍처: `SYSTEM_ARCHITECTURE.md`  
  - 개발 로드맵: `DEVELOPMENT_ROADMAP.md`
  - DB 스키마: `database-schema.md`
  - 할 일 목록: `TODO.md` (현재 문서)

### **개발 환경**
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.x
- **데이터베이스**: MySQL 8.0
- **빌드 도구**: Gradle
- **컨테이너**: Docker, Kubernetes

### **팀 협업**
- **버전 관리**: Git + GitHub
- **이슈 관리**: GitHub Issues  
- **문서화**: Markdown + GitHub Wiki
- **API 문서**: Swagger/OpenAPI

---

> **💡 중요**  
> 이 TODO 리스트는 매주 업데이트되며, 진행상황에 따라 일정과 우선순위가 조정될 수 있습니다.
> 
> **마지막 업데이트**: 2024년 6월 22일  
> **다음 리뷰 예정일**: Phase 1 Week 1 완료 후 (1주 후)