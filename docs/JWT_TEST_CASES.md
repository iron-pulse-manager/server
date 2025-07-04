# JWT 인증 시스템 테스트 케이스

## 테스트 환경
- 서버: http://localhost:8080
- API 문서: http://localhost:8080/swagger-ui/index.html

## 1. 기본 동작 확인

### ✅ 1.1 애플리케이션 실행 확인
```bash
curl -X GET http://localhost:8080/actuator/health
```
**예상 결과**: `{"status":"UP"}` 응답

### ✅ 1.2 Swagger UI 접근 확인
브라우저에서 http://localhost:8080/swagger-ui/index.html 접근
**예상 결과**: Swagger UI 화면이 표시되어야 함

## 2. JWT 인증 테스트

### ✅ 2.1 인증 없이 보호된 엔드포인트 접근
```bash
curl -X GET http://localhost:8080/api/auth/test
```
**예상 결과**: 401 Unauthorized 에러 또는 인증 오류

### 2.2 로그인 API 테스트 (데이터베이스에 사용자가 있는 경우)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```
**예상 결과**: Access Token과 Refresh Token 발급

### 2.3 유효한 토큰으로 보호된 엔드포인트 접근
```bash
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer [ACCESS_TOKEN]"
```
**예상 결과**: 성공 응답

### 2.4 잘못된 토큰으로 접근
```bash
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer invalid-token"
```
**예상 결과**: 401 Unauthorized 에러

## 3. 권한별 접근 제어 테스트

### 3.1 ADMIN 권한 필요 API
```bash
curl -X GET http://localhost:8080/api/admin/test \
  -H "Authorization: Bearer [ADMIN_TOKEN]"
```

### 3.2 STAFF 권한 필요 API
```bash
curl -X GET http://localhost:8080/api/staff/test \
  -H "Authorization: Bearer [STAFF_TOKEN]"
```

### 3.3 MEMBER 권한 필요 API
```bash
curl -X GET http://localhost:8080/api/member/test \
  -H "Authorization: Bearer [MEMBER_TOKEN]"
```

## 4. 토큰 만료 테스트

### 4.1 만료된 Access Token 사용
- 토큰 만료시간: 15분
- 만료 후 API 호출 시 401 에러 발생 확인

### 4.2 Refresh Token으로 토큰 갱신
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Authorization: Bearer [REFRESH_TOKEN]"
```

## 5. CORS 테스트

### 5.1 다른 도메인에서의 API 호출
```bash
curl -X OPTIONS http://localhost:8080/api/auth/login \
  -H "Origin: http://example.com" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type,Authorization"
```
**예상 결과**: CORS 헤더가 포함된 응답

## 6. 에러 응답 형식 확인

### 6.1 401 Unauthorized 응답 형식
```json
{
  "error": "Unauthorized",
  "message": "인증이 필요합니다. 로그인 후 다시 시도해주세요.",
  "status": 401,
  "timestamp": 1640995200000,
  "path": "/api/protected-endpoint"
}
```

### 6.2 403 Forbidden 응답 형식
```json
{
  "error": "Forbidden",
  "message": "접근 권한이 없습니다. 관리자에게 문의하세요.",
  "status": 403,
  "timestamp": 1640995200000,
  "path": "/api/admin/users"
}
```

## 7. 로그 확인 사항

### 7.1 인증 성공 로그
```
INFO  c.f.c.s.CustomUserDetailsService - 사용자 정보 로드 성공: admin (사용자 ID: 1)
INFO  c.f.a.a.service.AuthService - 로그인 성공 - 사용자: admin, 타입: OWNER
```

### 7.2 인증 실패 로그
```
ERROR c.f.c.s.CustomUserDetailsService - 사용자를 찾을 수 없습니다: invalid-user
ERROR c.f.a.a.service.AuthService - 로그인 실패 - 사용자: invalid-user, 원인: Bad credentials
```

### 7.3 JWT 토큰 검증 로그
```
DEBUG c.f.c.jwt.JwtAuthenticationFilter - JWT 토큰 인증 성공 - 사용자: admin
ERROR c.f.c.jwt.JwtUtil - JWT 토큰이 만료되었습니다: JWT expired at...
```

## 테스트 결과

### ✅ 통과한 테스트
- [x] 애플리케이션 실행
- [x] Swagger UI 접근
- [x] 인증 없이 보호된 엔드포인트 접근 시 오류 발생

### ⏳ 추가 테스트 필요
- [ ] 실제 사용자 데이터로 로그인 테스트
- [ ] 토큰 발급 및 검증 테스트
- [ ] 권한별 접근 제어 테스트
- [ ] 토큰 만료 테스트

## 주의사항

1. **데이터베이스 설정**: 실제 로그인 테스트를 위해서는 Auth 테이블에 사용자 데이터가 필요합니다.
2. **비밀키 보안**: 운영환경에서는 환경변수로 JWT_SECRET 설정 필요
3. **HTTPS 사용**: 운영환경에서는 반드시 HTTPS 사용
4. **토큰 저장**: 클라이언트에서 토큰을 안전하게 저장해야 함

## 다음 단계

1. 테스트용 사용자 데이터 생성
2. 실제 로그인/로그아웃 플로우 테스트
3. 프론트엔드와의 연동 테스트
4. 성능 및 보안 테스트