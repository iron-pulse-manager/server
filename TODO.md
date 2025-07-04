# 피트니스 관리 시스템 개발 TODO

## 현재 완료된 기능
✅ Spring Security + JWT 인증 시스템 완료
✅ 소셜 로그인 (카카오/애플) 구현 완료  
✅ 권한별 API 접근 제어 완료 (OWNER/EMPLOYEE/MEMBER)
✅ 토큰 갱신 기능 완료

---

## 🚀 실제 API 개발 전 필수 작업사항

### 1. 인증/회원가입 시스템 완성
- [ ] **일반 회원가입 API 구현** (OWNER용 - username/password)
- [ ] **소셜 회원가입 API 분리** (현재는 로그인과 회원가입이 혼재)
- [ ] **회원가입 시 토큰 발급 및 응답** 처리
- [ ] **OWNER 초기 계정 생성** (테스트 및 운영용)

### 2. 데이터베이스 스키마 최종 검증
- [ ] **엔티티 관계 검증** (User ↔ Auth 1:N 관계 확인)
- [ ] **누락된 엔티티 구현** (Business, BusinessEmployee, BusinessMember 등)
- [ ] **Auditing 설정 완료** (created_by, updated_by 자동 설정)
- [ ] **인덱스 최적화** (검색 성능 향상)

### 3. 공통 인프라 구축
- [ ] **BaseEntity 최종 정리** (공통 필드 표준화)
- [ ] **공통 응답 DTO 설계** (ApiResponse, ErrorResponse)
- [ ] **예외 처리 체계 구축** (GlobalExceptionHandler)
- [ ] **Validation 어노테이션 적용** (Bean Validation)

### 4. 설정 및 환경 구성
- [ ] **프로파일별 설정 분리** (local, dev, prod)
- [ ] **로깅 설정 최적화** (Logback 설정)
- [ ] **API 문서화 설정** (Swagger/OpenAPI 완성)
- [ ] **테스트 환경 구축** (TestContainers, 테스트 데이터)

### 5. 코드 품질 및 규칙
- [ ] **코딩 컨벤션 정리** (네이밍, 패키지 구조)
- [ ] **DTO 변환 전략 수립** (MapStruct vs ModelMapper)
- [ ] **서비스 레이어 설계 패턴 정의**
- [ ] **트랜잭션 관리 전략 수립**

---

## 🎯 API 개발 우선순위 (인프라 완성 후)

### Phase 1: 핵심 도메인
1. **사업장 관리 API**
2. **사용자 관리 API** (회원/직원/사장)
3. **권한 및 역할 관리 API**

### Phase 2: 비즈니스 로직
1. **회원권 관리 API**
2. **결제 관리 API**
3. **출석 관리 API**

### Phase 3: 부가 기능
1. **통계 및 대시보드 API**
2. **알림 시스템 API**
3. **커뮤니티 기능 API**

---

## ⚠️ 중요 고려사항

### 보안
- JWT 토큰 블랙리스트 구현 (Redis)
- 비밀번호 암호화 정책 수립
- API Rate Limiting 적용

### 성능
- 데이터베이스 커넥션 풀 최적화
- 캐싱 전략 수립 (Redis)
- 페이징 처리 표준화

### 운영
- 헬스체크 엔드포인트 구현
- 모니터링 및 메트릭 설정
- 로그 수집 및 분석 체계

---

## 📝 참고사항
- 모든 API는 RESTful 설계 원칙 준수
- 에러 응답은 일관된 형식으로 표준화
- 민감한 정보는 환경변수로 관리
- 테스트 코드는 각 기능과 함께 작성