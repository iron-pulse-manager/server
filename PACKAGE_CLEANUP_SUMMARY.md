# 패키지 구조 정리 완료

## 🧹 정리된 패키지 구조

### ✅ 완료된 작업

#### 1. 중복 엔티티 제거
- **Member.java** 삭제 → User 엔티티로 통합
- **Employee.java** 삭제 → User 엔티티로 통합
- 기존 소셜 로그인 정보는 Auth 엔티티로 분리

#### 2. 패키지 재구성
```
❌ 삭제된 패키지:
- domain/owner/ (사장님 관련)
- domain/employee/ (직원 관련)  
- domain/member/ (회원 관련)

✅ 새로운 구조:
- domain/auth/ (인증 정보)
- domain/user/ (통합 사용자 관리)
- domain/business/ (사업장 및 관계 관리)
```

#### 3. 엔티티 이동 및 통합
```
✅ business/ 패키지로 이동:
- BusinessEmployee.java
- BusinessEmployeeStatus.java  
- BusinessMember.java
- BusinessMemberStatus.java
- Business.java

✅ user/ 패키지로 통합:
- User.java (기존 Member + Employee 통합)
- UserRepository.java (기존 MemberRepository)
```

#### 4. Repository 정리
```
✅ business/ 패키지로 이동:
- BusinessMemberRepository.java
- BusinessMemberRepositoryCustom.java
- BusinessMemberRepositoryImpl.java

✅ user/ 패키지로 이동:
- UserRepository.java
```

### 🏗️ 최종 패키지 구조

```
src/main/java/com/fitness/domain/
├── auth/
│   └── entity/
│       └── Auth.java
├── user/
│   ├── entity/
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── service/
│   ├── controller/
│   └── dto/
├── business/
│   ├── entity/
│   │   ├── Business.java
│   │   ├── BusinessEmployee.java
│   │   ├── BusinessEmployeeStatus.java
│   │   ├── BusinessMember.java
│   │   └── BusinessMemberStatus.java
│   ├── repository/
│   │   ├── BusinessMemberRepository.java
│   │   ├── BusinessMemberRepositoryCustom.java
│   │   └── BusinessMemberRepositoryImpl.java
│   ├── service/
│   ├── controller/
│   └── dto/
├── payment/
│   └── entity/
│       ├── Payment.java
│       └── OutstandingPayment.java
├── membership/
│   └── entity/
│       └── Membership.java
└── ... (기타 도메인들)
```

### 🎯 정리 효과

1. **단순화**: 3개 패키지(owner, employee, member) → 2개 패키지(user, business)로 통합
2. **명확성**: 사용자는 user, 관계는 business로 역할 분리
3. **확장성**: 새로운 사용자 유형 추가 시 User 엔티티만 확장하면 됨
4. **일관성**: 모든 사업장 관련 엔티티가 business 패키지에 위치

### 🚀 다음 단계

이제 정리된 패키지 구조를 바탕으로:
1. Repository 인터페이스 구현 (UserRepository, BusinessRepository 등)
2. Service 레이어 구현
3. Controller 및 DTO 구현
4. 테스트 코드 작성

패키지 구조 정리가 완료되어 더욱 깔끔하고 확장 가능한 구조를 갖추게 되었습니다!