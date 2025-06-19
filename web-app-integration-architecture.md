# 헬스장 관리 SaaS 웹-앱 통합 아키텍처

## 1. 시스템 아키텍처 개요

### 1.1 클라이언트 구성 (REST API 서버 분리형)
```
┌─────────────────────────────────────────────────────────────┐
│                Spring Boot REST API Server                 │
│              (Stateless Backend Only)                      │
│        - JWT 인증                                           │
│        - REST API 엔드포인트                                 │
│        - Multi-tenant 지원                                  │
│        - CORS 설정                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP REST API 통신
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌───▼───┐ ┌───────▼──────┐
│ React 웹서버   │ │ 직원 앱 │ │   회원 앱    │
│ (별도 서버)    │ │(Flutter)│ │  (Flutter)   │
│ - React.js    │ │         │ │             │
│ - Node.js     │ │ 모바일앱 │ │   모바일앱    │
│ - 관리자 UI   │ │         │ │             │
│ - 대시보드     │ │         │ │             │
└─────────────────┘ └───────┘ └─────────────┘
```

### 1.2 데이터 흐름 예시
```
1. 사장님이 웹에서 사업장 추가
   ↓
2. API 서버에 Business 생성
   ↓  
3. 직원 앱에서 Business 목록 API 호출
   ↓
4. 직원이 해당 사업장에 가입 신청
   ↓
5. 웹에서 가입 승인/거절
   ↓
6. 승인 시 직원 앱에서 해당 사업장 기능 사용 가능
```

## 2. 인증 및 권한 체계

### 2.1 클라이언트 타입별 인증 방식
```java
public enum ClientType {
    WEB_ADMIN,      // 웹 사장님 (관리자)
    STAFF_APP,      // 직원 앱
    MEMBER_APP      // 회원 앱
}

public enum UserRole {
    BUSINESS_OWNER, // 사업장 사장님
    STAFF,          // 직원 (트레이너, 직원)
    MEMBER          // 일반 회원
}
```

### 2.2 JWT 토큰 구조
```json
{
  "sub": "user-id",
  "clientType": "STAFF_APP",
  "userRole": "STAFF", 
  "businessId": 123,
  "permissions": ["READ_MEMBERS", "UPDATE_SALES"],
  "iat": 1234567890,
  "exp": 1234567890
}
```

## 3. API 설계 전략

### 3.1 API 버전 관리
```
/api/v1/web/     - 웹 전용 API
/api/v1/staff/   - 직원 앱 전용 API  
/api/v1/member/  - 회원 앱 전용 API
/api/v1/common/  - 공통 API (인증, 파일업로드 등)
```

### 3.2 권한 기반 API 접근 제어
```java
@RestController
@RequestMapping("/api/v1/staff")
@PreAuthorize("hasRole('STAFF') and hasPermission('STAFF_APP')")
public class StaffController {
    
    @GetMapping("/sales/my")
    @PreAuthorize("hasPermission('READ_OWN_SALES')")
    public ResponseEntity<SalesDto> getMySales() {
        // 자신의 매출만 조회
    }
}
```

## 4. 실시간 데이터 동기화

### 4.1 WebSocket 연결 구조
```
클라이언트별 WebSocket 채널:
- /ws/web/{businessId}     - 웹 클라이언트
- /ws/staff/{businessId}   - 직원 앱
- /ws/member/{businessId}  - 회원 앱
```

### 4.2 이벤트 기반 알림
```java
@EventListener
public void handleBusinessCreated(BusinessCreatedEvent event) {
    // 새 사업장 생성 시 모든 직원 앱에 알림
    webSocketService.broadcastToStaffApps(
        "NEW_BUSINESS_AVAILABLE", 
        event.getBusiness()
    );
}
```

## 5. 보안 고려사항

### 5.1 클라이언트별 보안 정책
- **웹**: Session + CSRF 토큰
- **앱**: JWT + Refresh Token
- **API**: Rate Limiting, IP 화이트리스트

### 5.2 데이터 접근 제어
```java
// 사업장별 데이터 격리
@TenantFilter
public class MemberService {
    // 자동으로 현재 사업장 데이터만 조회
}

// 역할별 데이터 접근 제어  
@RoleBasedFilter
public class SalesService {
    // 직원은 자신의 매출만, 사장님은 전체 매출 조회
}
```

## 6. 성능 최적화

### 6.1 캐싱 전략
```java
// 클라이언트별 캐시 키
@Cacheable("business:staff-list:#{businessId}")
public List<Staff> getBusinessStaffs(Long businessId);

// 앱 전용 경량화된 데이터
@Cacheable("member:app-summary:#{memberId}")  
public MemberAppSummary getMemberSummary(Long memberId);
```

### 6.2 API 응답 최적화
```java
// 앱용 경량화된 DTO
public class MemberAppDto {
    private Long id;
    private String name;
    private String membershipStatus;
    // 앱에 필요한 최소한의 정보만 포함
}
```

## 7. 배포 및 운영

### 7.1 환경별 설정
```yaml
# application-mobile.yml
mobile:
  push:
    firebase:
      server-key: ${FIREBASE_SERVER_KEY}
  deep-link:
    scheme: gymmanager
```

### 7.2 모니터링
- 클라이언트별 API 사용량 추적
- 실시간 연결 상태 모니터링
- 앱 버전별 호환성 체크