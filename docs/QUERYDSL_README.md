# Querydsl & Lombok 적용 가이드

## 🚀 적용 완료 사항

### 1. **의존성 추가**
- **Lombok**: 코드 간소화를 위한 어노테이션 기반 라이브러리
- **Querydsl**: 타입 안전한 쿼리 작성을 위한 라이브러리

### 2. **Entity 클래스 개선**

#### Employee 엔티티
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity {
    // 필드들...
    
    // 팩토리 메서드
    public static Employee createKakaoEmployee(String name, String email, String kakaoId) {
        return Employee.builder()
                .name(name)
                .email(email)
                .kakaoId(kakaoId)
                .build();
    }
}
```

#### BusinessEmployee 엔티티
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BusinessEmployee extends BaseEntity {
    // 필드들...
    
    // 팩토리 메서드
    public static BusinessEmployee createBusinessConnection(Long businessId, Long employeeId) {
        return BusinessEmployee.builder()
                .businessId(businessId)
                .employeeId(employeeId)
                .status(BusinessEmployeeStatus.PENDING)
                .build();
    }
}
```

### 3. **Repository 구조 개선**

#### Custom Repository 패턴 적용
```java
// 인터페이스
public interface EmployeeRepositoryCustom {
    List<Employee> findActiveEmployeesByBusinessId(Long businessId);
    Page<Employee> searchEmployeesWithConditions(...);
}

// 구현체 (Querydsl 사용)
@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<Employee> findActiveEmployeesByBusinessId(Long businessId) {
        return queryFactory
                .selectFrom(employee)
                .join(employee.businessEmployees, businessEmployee)
                .where(
                    businessEmployee.businessId.eq(businessId),
                    businessEmployee.status.eq(BusinessEmployeeStatus.APPROVED)
                )
                .fetch();
    }
}
```

### 4. **주요 개선점**

#### Lombok 적용 효과
- **@Getter**: 모든 필드의 Getter 메서드 자동 생성
- **@Builder**: 빌더 패턴 자동 생성
- **@NoArgsConstructor**: 기본 생성자 자동 생성
- **@AllArgsConstructor**: 모든 필드를 포함한 생성자 자동 생성

#### Querydsl 적용 효과
- **타입 안전성**: 컴파일 타임에 쿼리 오류 검출
- **가독성**: SQL과 유사한 직관적인 쿼리 작성
- **동적 쿼리**: 조건에 따른 동적 쿼리 작성 용이
- **성능**: 복잡한 조인과 페이징 최적화

## 🔧 Q클래스 생성 방법

### 1. Gradle 빌드
```bash
./gradlew compileQuerydsl
# 또는
./gradlew build
```

### 2. Q클래스 생성 확인
```
build/
├── generated/
    └── querydsl/
        └── com/fitness/domain/employee/entity/
            ├── QEmployee.java
            └── QBusinessEmployee.java
```

### 3. IDE 설정 (IntelliJ)
1. **Settings** → **Build, Execution, Deployment** → **Gradle**
2. **Build and run using**: IntelliJ IDEA
3. **Run tests using**: IntelliJ IDEA

## 📝 사용 예시

### 1. **Entity 생성**
```java
// 기존 방식
Employee employee = new Employee();
employee.setName("홍길동");
employee.setEmail("hong@example.com");
employee.setKakaoId("kakao123");

// 개선된 방식 (Lombok Builder)
Employee employee = Employee.createKakaoEmployee("홍길동", "hong@example.com", "kakao123");
```

### 2. **Repository 사용**
```java
// 기존 JPQL 방식
@Query("SELECT e FROM Employee e JOIN e.businessEmployees be WHERE be.businessId = :businessId")
List<Employee> findByBusinessId(@Param("businessId") Long businessId);

// 개선된 Querydsl 방식
public List<Employee> findActiveEmployeesByBusinessId(Long businessId) {
    return queryFactory
            .selectFrom(employee)
            .join(employee.businessEmployees, businessEmployee)
            .where(
                businessEmployee.businessId.eq(businessId),
                businessEmployee.status.eq(BusinessEmployeeStatus.APPROVED)
            )
            .fetch();
}
```

### 3. **동적 쿼리 작성**
```java
public Page<Employee> searchEmployees(String name, String phone, Boolean isSocial, Pageable pageable) {
    BooleanBuilder builder = new BooleanBuilder();
    
    if (name != null) {
        builder.and(employee.name.containsIgnoreCase(name));
    }
    
    if (phone != null) {
        builder.and(employee.phoneNumber.containsIgnoreCase(phone));
    }
    
    if (isSocial != null && isSocial) {
        builder.and(employee.kakaoId.isNotNull().or(employee.appleId.isNotNull()));
    }
    
    // 쿼리 실행...
}
```

## ⚠️ 주의사항

### 1. **Q클래스 관리**
- Q클래스는 Git에 커밋하지 않음 (.gitignore 추가)
- 엔티티 변경 시 반드시 Q클래스 재생성 필요

### 2. **Lombok 사용 시 주의점**
- **@Data** 어노테이션은 Entity에서 사용 금지 (equals/hashCode 문제)
- **@Setter** 사용 최소화 (불변성 보장)
- **@Builder.Default** 활용하여 기본값 설정

### 3. **성능 고려사항**
- **fetchJoin()** 사용하여 N+1 문제 해결
- 페이징 쿼리에서 **count 쿼리 최적화**
- 복잡한 조건절은 **인덱스 활용** 고려

## 🎯 다음 단계

1. **Service 레이어에서 새로운 Repository 메서드 활용**
2. **Controller에서 복잡한 검색 조건 처리**
3. **성능 테스트 및 쿼리 최적화**
4. **추가 Entity들에도 동일한 패턴 적용**

---

이제 타입 안전하고 유지보수하기 쉬운 코드로 개발할 수 있습니다! 🚀