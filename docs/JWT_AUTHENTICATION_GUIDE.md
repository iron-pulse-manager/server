# JWT 인증 시스템 가이드

## 개요
본 프로젝트는 JWT(JSON Web Token) 기반의 인증 시스템을 사용합니다. 이 문서는 JWT 인증 시스템의 구조와 사용법을 설명합니다.

## 시스템 구조

### 1. 핵심 컴포넌트

#### JwtProperties
- JWT 설정을 관리하는 프로퍼티 클래스
- `application.yml`에서 JWT 관련 설정을 바인딩
- 비밀키, Access Token 만료시간(15분), Refresh Token 만료시간(7일) 설정

#### JwtUtil
- JWT 토큰의 생성, 검증, 파싱을 담당하는 유틸리티 클래스
- **주요 메서드:**
  - `generateAccessToken()`: Access Token 생성
  - `generateRefreshToken()`: Refresh Token 생성
  - `validateToken()`: 토큰 유효성 검증
  - `getUsernameFromToken()`: 토큰에서 사용자명 추출

#### JwtAuthenticationFilter
- 모든 HTTP 요청을 가로채서 JWT 토큰을 검증하는 필터
- Authorization 헤더에서 Bearer 토큰을 추출하여 검증
- 유효한 토큰인 경우 SecurityContext에 인증 정보 설정

#### 예외 처리
- `JwtAuthenticationEntryPoint`: 인증 실패 시 401 Unauthorized 처리
- `JwtAccessDeniedHandler`: 권한 부족 시 403 Forbidden 처리

### 2. 보안 설정 (SecurityConfig)

#### 인증이 필요 없는 경로
```
- /api/auth/**        (로그인, 회원가입)
- /api/public/**      (공개 API)
- /swagger-ui/**      (API 문서)
- /v3/api-docs/**     (Swagger)
- /actuator/**        (모니터링)
```

#### 권한별 접근 제어
```
- /api/member/**  → MEMBER 권한 필요
- /api/staff/**   → STAFF 권한 필요  
- /api/admin/**   → ADMIN 권한 필요
```

## 사용법

### 1. 로그인 프로세스
```
1. 클라이언트가 /api/auth/login에 ID/PW 전송
2. 서버에서 인증 성공 시 Access Token + Refresh Token 발급
3. 클라이언트는 토큰을 저장하고 이후 요청에 포함
```

### 2. API 요청 시 토큰 사용
```http
Authorization: Bearer {access_token}
```

### 3. 토큰 갱신
```
1. Access Token 만료 시 401 에러 발생
2. 클라이언트는 Refresh Token으로 새로운 Access Token 요청
3. /api/auth/refresh 엔드포인트 사용
```

## 설정 변경

### JWT 설정 (application.yml)
```yaml
jwt:
  secret: ${JWT_SECRET:your-secret-key-here-make-it-at-least-32-characters-long}
  access-token-expiration: 900000    # 15분
  refresh-token-expiration: 604800000 # 7일
```

### 환경별 설정
- **개발환경**: 짧은 만료시간으로 테스트 용이성 확보
- **운영환경**: 보안을 위해 비밀키를 환경변수로 설정 필수

## 보안 고려사항

### 1. 비밀키 관리
- 운영환경에서는 반드시 환경변수 `JWT_SECRET` 사용
- 최소 32자 이상의 강력한 비밀키 설정

### 2. 토큰 저장
- 클라이언트에서 토큰을 안전하게 저장 (HttpOnly 쿠키 권장)
- XSS 공격 방지를 위해 localStorage 사용 지양

### 3. HTTPS 사용
- 토큰 전송 시 반드시 HTTPS 사용
- 중간자 공격 방지

### 4. 토큰 만료시간
- Access Token: 짧게 설정 (15분)
- Refresh Token: 적절한 길이 (7일)

## 에러 응답 형식

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "인증이 필요합니다. 로그인 후 다시 시도해주세요.",
  "status": 401,
  "timestamp": 1640995200000,
  "path": "/api/protected-endpoint"
}
```

### 403 Forbidden
```json
{
  "error": "Forbidden", 
  "message": "접근 권한이 없습니다. 관리자에게 문의하세요.",
  "status": 403,
  "timestamp": 1640995200000,
  "path": "/api/admin/users"
}
```

## 개발 가이드

### 1. 새로운 API 엔드포인트 추가 시
- SecurityConfig에서 해당 경로의 권한 설정 추가
- 컨트롤러에서 `@PreAuthorize` 어노테이션으로 세부 권한 제어

### 2. 커스텀 권한 검증
```java
@PreAuthorize("hasRole('ADMIN') or @userService.isOwner(authentication.name, #userId)")
public ResponseEntity<User> getUser(@PathVariable Long userId) {
    // 구현
}
```

### 3. 로그인한 사용자 정보 가져오기
```java
@GetMapping("/profile")
public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
    String username = authentication.getName();
    // 구현
}
```

## 문제 해결

### 1. 토큰 검증 실패
- 로그에서 JWT 관련 에러 확인
- 토큰 만료시간 확인
- 비밀키 설정 확인

### 2. CORS 에러
- SecurityConfig의 CORS 설정 확인
- 프론트엔드 도메인이 허용 목록에 있는지 확인

### 3. 권한 에러
- 사용자의 역할(ROLE) 확인
- SecurityConfig의 경로별 권한 설정 확인