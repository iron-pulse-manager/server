# Spring Security + JWT 인증 시스템 구현 TODO

## 현재 진행 상황
- **진행 중**: JWT 토큰 관련 유틸리티 클래스들 구현
- **완료된 작업**: application.yml에 JWT 설정 추가 (access-token-expiration: 15분, refresh-token-expiration: 7일)

## 다음 작업 리스트

### 🔥 High Priority (먼저 완료해야 함)
1. **[진행 중] JWT 토큰 관련 유틸리티 클래스들 구현**
   - [ ] JwtProperties 설정 클래스 생성
   - [ ] JwtTokenProvider 클래스 구현 (토큰 생성, 검증, 파싱)
   - [ ] JwtTokenFilter 클래스 구현 (HTTP 요청 토큰 추출 및 인증)

2. **Spring Security 설정 클래스 구현**
   - [ ] SecurityConfig 클래스 구현
   - [ ] JWT 필터 등록 및 설정

3. **UserDetailsService 구현**
   - [ ] CustomUserDetailsService 클래스 구현
   - [ ] Auth 엔티티 기반 사용자 정보 로드

4. **토큰 갱신 기능 구현**
   - [ ] Refresh Token으로 Access Token 갱신 로직
   - [ ] 기존 Refresh Token 무효화 처리

5. **리프레시 토큰 만료 시 로그아웃 처리**
   - [ ] 만료 감지 및 자동 로그아웃 로직
   - [ ] 모든 토큰 무효화 처리

### 🔶 Medium Priority
6. **인증 관련 DTO 클래스들 구현**
   - [ ] 로그인 요청/응답 DTO
   - [ ] 회원가입 요청/응답 DTO
   - [ ] 토큰 갱신 요청/응답 DTO

7. **인증 관련 서비스 클래스 구현**
   - [ ] AuthService 클래스 구현
   - [ ] 일반 로그인/소셜 로그인 처리 로직

8. **인증 관련 컨트롤러 구현**
   - [ ] AuthController 클래스 구현
   - [ ] 로그인/회원가입/토큰갱신 API 엔드포인트

### 🔹 Low Priority
9. **예외 처리 클래스들 구현**
   - [ ] CustomAuthenticationException 등 커스텀 예외
   - [ ] 글로벌 예외 처리기

10. **통합 테스트 작성 및 검증**
    - [ ] 인증 시스템 전체 테스트
    - [ ] 토큰 갱신 시나리오 테스트

11. **인증 시스템 가이드 문서 작성**
    - [ ] 일반 로그인/소셜 로그인 방식 설명
    - [ ] API 사용법 가이드

## 중요한 설계 결정사항
- **일반 로그인 (사장님)**: username + password 인증
- **소셜 로그인 (직원/회원)**: JWT 토큰 기반 인증만 사용
- **토큰 만료 시간**: Access Token 15분, Refresh Token 7일
- **토큰 갱신**: Refresh Token으로 새로운 토큰 쌍 발급
- **보안**: 토큰 갱신 시 기존 Refresh Token 무효화

## 다음 작업 시작점
1. JwtProperties 설정 클래스 생성부터 시작
2. 위치: `src/main/java/com/fitness/config/JwtProperties.java`