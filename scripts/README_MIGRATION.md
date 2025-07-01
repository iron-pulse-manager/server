# 직원 테이블 구조 개선 마이그레이션 가이드 (소셜 로그인 지원)

## 🎯 마이그레이션 목적

기존 EMPLOYEE 테이블의 구조적 문제를 해결하여 아래 요구사항을 충족하도록 개선합니다:

1. **직원 개인정보와 사업장별 소속정보 분리**
2. **소셜 로그인 지원 (카카오, 애플)**
3. **직원의 사업장 이동 히스토리 완전 관리**
4. **사업장 연결 신청 상태 체계적 관리**
5. **퇴사한 직원 정보 보존**
6. **사업장 연결을 위한 필수정보 검증**

## 📋 변경사항 요약

### 기존 구조
```
employees
├── id (직원 고유 ID)
├── business_id (소속 사업장 - 문제점)
├── working_hours (사업장별 정보 - 문제점)
├── join_date (사업장별 정보 - 문제점)
├── memo (사업장별 정보 - 문제점)
└── status (사업장별 정보 - 문제점)
```

### 개선된 구조
```
employees (개인정보만 + 소셜로그인)
├── employee_id
├── name
├── phone_number (선택사항)
├── email
├── kakao_id (소셜로그인)
├── apple_id (소셜로그인)
├── birth_date
├── gender
├── address
├── profile_image_url
├── bank_name
└── account_number

business_employees (사업장별 소속정보)
├── business_employee_id
├── business_id
├── employee_id
├── status (PENDING/APPROVED/REJECTED/RESIGNED)
├── position
├── working_hours
├── join_date
├── resign_date
├── memo
└── approved_at
```

## 🚀 마이그레이션 실행 순서

### 1. 사전 준비
```sql
-- 데이터베이스 백업
mysqldump -u [username] -p [database_name] > backup_before_migration.sql
```

### 2. 마이그레이션 스크립트 실행
```bash
mysql -u [username] -p [database_name] < scripts/employee_table_migration.sql
```

### 3. 마이그레이션 검증
스크립트 실행 후 자동으로 실행되는 검증 쿼리 결과를 확인하세요:
- 기존 직원 수와 새 직원 수 일치 확인
- 사업장별 직원 수 일치 확인

### 4. 애플리케이션 코드 적용
새로 생성된 Entity 클래스들을 사용하도록 코드를 업데이트하세요:
- `Employee.java` - 직원 개인정보 엔티티
- `BusinessEmployee.java` - 사업장-직원 소속관계 엔티티
- `BusinessEmployeeStatus.java` - 소속 상태 열거형

### 5. Repository 인터페이스 활용
새로운 Repository 인터페이스들을 통해 데이터에 접근하세요:
- `EmployeeRepository.java` - 직원 개인정보 관리
- `BusinessEmployeeRepository.java` - 소속관계 관리

## 📊 주요 비즈니스 로직 변화

### 직원 로그인 (기존)
```java
// 기존: 일반 로그인만 지원
Employee employee = employeeRepository.findByUsername(username);
```

### 직원 로그인 (개선)
```java
// 개선: 소셜 로그인 지원
Employee kakaoEmployee = employeeRepository.findByKakaoId(kakaoId);
Employee appleEmployee = employeeRepository.findByAppleId(appleId);

// 소셜 로그인 회원가입
Employee newEmployee = new Employee(name, email, kakaoId); // 카카오
Employee newEmployee = new Employee(name, email, appleId, true); // 애플
```

### 사업장 연결 신청 (개선)
```java
// 개선: 필수정보 검증 후 사업장 연결 신청
BusinessEmployee.validateEmployeeForBusinessConnection(employee);

BusinessEmployee businessEmployee = new BusinessEmployee(businessId, employeeId);
// 필수정보가 완성되지 않은 경우 승인 불가
if (businessEmployee.canBeApproved()) {
    businessEmployee.approve();
}
```

### 직원 조회 (기존)
```java
// 기존: 사업장별 직원 조회
List<Employee> employees = employeeRepository.findByBusinessId(businessId);
```

### 직원 조회 (개선)
```java
// 개선: 사업장별 활성 직원 조회
List<Employee> employees = employeeRepository.findActiveEmployeesByBusinessId(businessId);

// 승인 가능한 직원 조회 (필수정보 완성된 PENDING 상태)
List<BusinessEmployee> approvableEmployees = 
    businessEmployeeRepository.findApprovableEmployeesByBusinessId(businessId);

// 소셜 로그인 직원 조회
List<BusinessEmployee> socialEmployees = 
    businessEmployeeRepository.findSocialLoginEmployeesByBusinessIdAndStatus(
        businessId, BusinessEmployeeStatus.APPROVED);
```

### 직원 상태 관리 (기존)
```java
// 기존: 직원 상태 변경
employee.setStatus(EmployeeStatus.RESIGNED);
```

### 직원 상태 관리 (개선)
```java
// 개선: 사업장별 소속상태 관리
BusinessEmployee businessEmployee = 
    businessEmployeeRepository.findByBusinessIdAndEmployeeId(businessId, employeeId);
businessEmployee.resign(LocalDate.now());
```

## 🔍 마이그레이션 이후 확인사항

### 1. 데이터 무결성 검증
```sql
-- 모든 직원이 정상적으로 마이그레이션되었는지 확인
SELECT 
    '기존 테이블' as source,
    COUNT(*) as count
FROM employees_old
UNION ALL
SELECT 
    '새 테이블' as source,
    COUNT(*) as count
FROM employees;

-- 사업장별 소속관계가 정상적으로 생성되었는지 확인
SELECT 
    business_id,
    COUNT(*) as employee_count,
    COUNT(CASE WHEN status = 'APPROVED' THEN 1 END) as active_count
FROM business_employees
GROUP BY business_id;
```

### 2. 애플리케이션 기능 테스트
- [ ] 소셜 로그인 (카카오, 애플)
- [ ] 소셜 로그인 회원가입
- [ ] 마이페이지 정보 완성 (이름, 전화번호)
- [ ] 사업장 연결 신청
- [ ] 필수정보 미완성 시 승인 불가 처리
- [ ] 직원 목록 조회
- [ ] 직원 상세 정보 조회
- [ ] 직원 가입 신청 처리
- [ ] 직원 승인/거절
- [ ] 직원 퇴사 처리
- [ ] 직원 검색 기능
- [ ] 통계 기능 (직원 수, 신규 입사자 등)

### 3. 성능 테스트
새로운 테이블 구조에서 주요 쿼리들의 성능을 확인하세요:
- 사업장별 직원 목록 조회
- 직원 검색 쿼리
- 통계 쿼리

## ⚠️ 주의사항

1. **마이그레이션 실행 전 반드시 데이터베이스 백업 수행**
2. **운영 환경 적용 전 개발/테스트 환경에서 충분한 검증**
3. **기존 백업 테이블은 검증 완료 후 삭제 권장**
4. **관련 애플리케이션 코드 동시 배포 필요**

## 🔄 롤백 계획

마이그레이션에 문제가 발생할 경우:

1. **즉시 롤백**
```sql
-- 새 테이블 삭제
DROP TABLE business_employees;
DROP TABLE employees;

-- 기존 테이블 복원
RENAME TABLE employees_old TO employees;
```

2. **백업에서 복원**
```bash
mysql -u [username] -p [database_name] < backup_before_migration.sql
```

## 📞 지원

마이그레이션 관련 문의사항이 있으시면 개발팀에 연락해주세요.

---
**마이그레이션 완료일**: `실행 후 기록`  
**검증 완료일**: `검증 후 기록`  
**운영 적용일**: `적용 후 기록`