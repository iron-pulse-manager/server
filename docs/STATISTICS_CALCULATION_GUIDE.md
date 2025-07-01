# 📊 피트니스 관리 시스템 - 통계 산출 방식 가이드

## 🎯 개요

본 문서는 피트니스 관리 시스템의 각종 통계가 어떤 방식으로 계산되는지 상세히 설명합니다. 모든 통계는 실시간으로 계산되며, 데이터의 정확성과 일관성을 보장합니다.

---

## 📋 **1. 대시보드 통계**

### 1.1 **마스터 권한 대시보드**

#### 🔹 **일 매출액**
```sql
-- 오늘 매출 (payments 테이블 기준)
SELECT SUM(actual_price) 
FROM payments 
WHERE business_id = :businessId 
  AND DATE(payment_date) = CURDATE()
  AND payment_status = 'COMPLETED';

-- 전날 대비 증감 퍼센테이지
SELECT 
  (today_sales - yesterday_sales) / yesterday_sales * 100 as growth_rate
FROM (
  SELECT 
    (SELECT SUM(actual_price) FROM payments WHERE business_id = :businessId AND DATE(payment_date) = CURDATE()) as today_sales,
    (SELECT SUM(actual_price) FROM payments WHERE business_id = :businessId AND DATE(payment_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)) as yesterday_sales
) as sales_comparison;
```

#### 🔹 **월 매출액**
```sql
-- 이번달 매출
SELECT SUM(actual_price) 
FROM payments 
WHERE business_id = :businessId 
  AND YEAR(payment_date) = YEAR(CURDATE())
  AND MONTH(payment_date) = MONTH(CURDATE())
  AND payment_status = 'COMPLETED';
```

#### 🔹 **오늘 방문자 수**
```sql
-- 오늘 출석한 회원 수 (중복 제거)
SELECT COUNT(DISTINCT bm.member_id)
FROM attendances a
JOIN business_members bm ON a.member_id = bm.member_id
WHERE bm.business_id = :businessId 
  AND DATE(a.attendance_date) = CURDATE()
  AND a.status = 'PRESENT';

-- 어제 대비 증감
SELECT 
  (today_visitors - yesterday_visitors) / yesterday_visitors * 100 as growth_rate
FROM (
  SELECT 
    (SELECT COUNT(DISTINCT bm.member_id) FROM attendances a JOIN business_members bm ON a.member_id = bm.member_id WHERE bm.business_id = :businessId AND DATE(a.attendance_date) = CURDATE()) as today_visitors,
    (SELECT COUNT(DISTINCT bm.member_id) FROM attendances a JOIN business_members bm ON a.member_id = bm.member_id WHERE bm.business_id = :businessId AND DATE(a.attendance_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)) as yesterday_visitors
) as visitor_comparison;
```

#### 🔹 **이번주 방문자 수**
```sql
-- 이번주 출석한 회원 수 (월요일~일요일)
SELECT COUNT(DISTINCT bm.member_id)
FROM attendances a
JOIN business_members bm ON a.member_id = bm.member_id
WHERE bm.business_id = :businessId 
  AND YEARWEEK(a.attendance_date, 1) = YEARWEEK(CURDATE(), 1)
  AND a.status = 'PRESENT';
```

#### 🔹 **매출 구성 비율**
```sql
-- 상품 유형별 매출 비율
SELECT 
  p.product_type,
  SUM(pay.actual_price) as revenue,
  SUM(pay.actual_price) / (SELECT SUM(actual_price) FROM payments WHERE business_id = :businessId AND MONTH(payment_date) = MONTH(CURDATE())) * 100 as percentage
FROM payments pay
JOIN products p ON pay.product_id = p.product_id
WHERE pay.business_id = :businessId 
  AND MONTH(pay.payment_date) = MONTH(CURDATE())
  AND pay.payment_status = 'COMPLETED'
GROUP BY p.product_type;
```

### 1.2 **일반 권한 대시보드**

#### 🔹 **총 회원수**
```sql
-- 현재 활성 회원 수
SELECT COUNT(*) 
FROM business_members 
WHERE business_id = :businessId 
  AND status = 'ACTIVE';
```

#### 🔹 **만료임박 회원권 수**
```sql
-- 30일 이내 만료 예정 회원권 (payments 기준)
SELECT COUNT(DISTINCT pay.member_id)
FROM payments pay
JOIN business_members bm ON pay.member_id = bm.member_id
WHERE bm.business_id = :businessId 
  AND pay.service_end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
  AND pay.payment_status = 'COMPLETED'
  AND bm.status = 'ACTIVE';
```

#### 🔹 **만료임박 PT회원 수**
```sql
-- 개인레슨 이용권 중 30일 이내 만료 예정
SELECT COUNT(DISTINCT pay.member_id)
FROM payments pay
JOIN products p ON pay.product_id = p.product_id
JOIN business_members bm ON pay.member_id = bm.member_id
WHERE bm.business_id = :businessId 
  AND p.product_type = 'PERSONAL_TRAINING'
  AND pay.service_end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
  AND pay.payment_status = 'COMPLETED'
  AND bm.status = 'ACTIVE';
```

---

## 👥 **2. 회원 통계**

### 2.1 **회원 상태별 요약**

#### 🔹 **총 회원 수**
```sql
SELECT COUNT(*) 
FROM business_members 
WHERE business_id = :businessId;
```

#### 🔹 **활성 회원 수**
```sql
SELECT COUNT(*) 
FROM business_members 
WHERE business_id = :businessId 
  AND status = 'ACTIVE';
```

#### 🔹 **만료 임박 회원 수**
```sql
-- 이용권 만료일이 30일 이내인 회원
SELECT COUNT(DISTINCT bm.member_id)
FROM business_members bm
JOIN payments pay ON bm.member_id = pay.member_id
WHERE bm.business_id = :businessId 
  AND bm.status = 'ACTIVE'
  AND pay.service_end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
  AND pay.payment_status = 'COMPLETED';
```

#### 🔹 **신규 회원 수 (이번달)**
```sql
SELECT COUNT(*) 
FROM business_members 
WHERE business_id = :businessId 
  AND YEAR(join_date) = YEAR(CURDATE())
  AND MONTH(join_date) = MONTH(CURDATE());
```

### 2.2 **월별 회원 현황**

#### 🔹 **월별 회원 수 추이**
```sql
-- 최근 12개월 회원 수 변화
SELECT 
  DATE_FORMAT(cal.month_date, '%Y-%m') as month,
  COUNT(CASE WHEN bm.join_date <= cal.month_date AND bm.status = 'ACTIVE' THEN 1 END) as total_members,
  COUNT(CASE WHEN p.product_type = 'MEMBERSHIP' AND pay.service_end_date >= cal.month_date THEN 1 END) as membership_members,
  COUNT(CASE WHEN p.product_type = 'PERSONAL_TRAINING' AND pay.service_end_date >= cal.month_date THEN 1 END) as pt_members
FROM (
  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL seq.seq MONTH), '%Y-%m-01') as month_date
  FROM (SELECT 0 as seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 
        UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11) seq
) cal
LEFT JOIN business_members bm ON bm.business_id = :businessId
LEFT JOIN payments pay ON bm.member_id = pay.member_id AND pay.payment_status = 'COMPLETED'
LEFT JOIN products p ON pay.product_id = p.product_id
GROUP BY cal.month_date
ORDER BY cal.month_date;
```

### 2.3 **회원권 유형분포**

#### 🔹 **상품별 회원 분포**
```sql
-- 현재 활성 회원권의 상품별 분포
SELECT 
  p.product_name,
  COUNT(*) as member_count,
  COUNT(*) * 100.0 / (SELECT COUNT(*) FROM business_members bm2 WHERE bm2.business_id = :businessId AND bm2.status = 'ACTIVE') as percentage
FROM business_members bm
JOIN payments pay ON bm.member_id = pay.member_id
JOIN products p ON pay.product_id = p.product_id
WHERE bm.business_id = :businessId 
  AND bm.status = 'ACTIVE'
  AND pay.service_end_date >= CURDATE()
  AND pay.payment_status = 'COMPLETED'
GROUP BY p.product_id, p.product_name
ORDER BY member_count DESC;
```

---

## 💰 **3. 매출 통계**

### 3.1 **월별 매출 현황**

#### 🔹 **월별 매출 추이 (최근 12개월)**
```sql
SELECT 
  DATE_FORMAT(payment_date, '%Y-%m') as month,
  SUM(actual_price) as total_revenue,
  SUM(CASE WHEN p.product_type = 'MEMBERSHIP' THEN actual_price ELSE 0 END) as membership_revenue,
  SUM(CASE WHEN p.product_type = 'PERSONAL_TRAINING' THEN actual_price ELSE 0 END) as pt_revenue,
  SUM(CASE WHEN p.product_type = 'LOCKER' THEN actual_price ELSE 0 END) as locker_revenue,
  SUM(CASE WHEN p.product_type = 'OTHERS' THEN actual_price ELSE 0 END) as others_revenue
FROM payments pay
JOIN products p ON pay.product_id = p.product_id
WHERE pay.business_id = :businessId 
  AND pay.payment_status = 'COMPLETED'
  AND payment_date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
GROUP BY DATE_FORMAT(payment_date, '%Y-%m')
ORDER BY month;

-- 일일권 매출 (별도 계산)
SELECT 
  DATE_FORMAT(visit_date, '%Y-%m') as month,
  SUM(amount) as day_pass_revenue
FROM day_passes 
WHERE business_id = :businessId 
  AND visit_date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
GROUP BY DATE_FORMAT(visit_date, '%Y-%m');
```

### 3.2 **이번달 수입 요약**

#### 🔹 **상품 유형별 매출 비율**
```sql
-- 이번달 상품별 매출 및 비율
SELECT 
  CASE p.product_type
    WHEN 'MEMBERSHIP' THEN '회원권'
    WHEN 'PERSONAL_TRAINING' THEN '개인레슨'
    WHEN 'LOCKER' THEN '락커'
    WHEN 'OTHERS' THEN '기타'
  END as product_category,
  SUM(pay.actual_price) as revenue,
  SUM(pay.actual_price) * 100.0 / (
    SELECT SUM(actual_price) 
    FROM payments 
    WHERE business_id = :businessId 
      AND YEAR(payment_date) = YEAR(CURDATE()) 
      AND MONTH(payment_date) = MONTH(CURDATE())
      AND payment_status = 'COMPLETED'
  ) as percentage
FROM payments pay
JOIN products p ON pay.product_id = p.product_id
WHERE pay.business_id = :businessId 
  AND YEAR(pay.payment_date) = YEAR(CURDATE())
  AND MONTH(pay.payment_date) = MONTH(CURDATE())
  AND pay.payment_status = 'COMPLETED'
GROUP BY p.product_type;

-- 일일권 매출 추가
SELECT 
  '일일권' as product_category,
  SUM(amount) as revenue,
  SUM(amount) * 100.0 / (
    SELECT total_revenue FROM (
      SELECT SUM(actual_price) as total_revenue FROM payments WHERE business_id = :businessId AND YEAR(payment_date) = YEAR(CURDATE()) AND MONTH(payment_date) = MONTH(CURDATE())
      UNION ALL
      SELECT SUM(amount) FROM day_passes WHERE business_id = :businessId AND YEAR(visit_date) = YEAR(CURDATE()) AND MONTH(visit_date) = MONTH(CURDATE())
    ) combined
  ) as percentage
FROM day_passes 
WHERE business_id = :businessId 
  AND YEAR(visit_date) = YEAR(CURDATE())
  AND MONTH(visit_date) = MONTH(CURDATE());
```

### 3.3 **이번달 지출 요약**

#### 🔹 **지출 유형별 금액 및 비율**
```sql
-- 지출 관리에서 등록된 지출 기준
SELECT 
  expense_type,
  SUM(amount) as expense_amount,
  SUM(amount) * 100.0 / (
    SELECT SUM(amount) 
    FROM expenses 
    WHERE business_id = :businessId 
      AND YEAR(expense_date) = YEAR(CURDATE()) 
      AND MONTH(expense_date) = MONTH(CURDATE())
  ) as percentage
FROM expenses 
WHERE business_id = :businessId 
  AND YEAR(expense_date) = YEAR(CURDATE())
  AND MONTH(expense_date) = MONTH(CURDATE())
GROUP BY expense_type;
```

---

## 👨‍💼 **4. 직원 실적 통계**

### 4.1 **직원별 매출 실적**

#### 🔹 **이번달 총 매출액 (직원별)**
```sql
-- 상품 담당자 매출 실적 기준 (payments.instructor_commission 활용)
SELECT 
  be.business_employee_id,
  e.name as employee_name,
  SUM(pay.instructor_commission) as total_sales,
  COUNT(DISTINCT pay.member_id) as member_count,
  COUNT(pay.id) as contract_count
FROM business_employees be
JOIN employees e ON be.employee_id = e.employee_id
LEFT JOIN payments pay ON pay.trainer_id = be.business_employee_id
WHERE be.business_id = :businessId 
  AND be.status = 'APPROVED'
  AND (pay.payment_date IS NULL OR (
    YEAR(pay.payment_date) = YEAR(CURDATE()) 
    AND MONTH(pay.payment_date) = MONTH(CURDATE())
    AND pay.payment_status = 'COMPLETED'
  ))
GROUP BY be.business_employee_id, e.name
ORDER BY total_sales DESC;
```

#### 🔹 **신규 회원 계약 수**
```sql
-- 이번달 신규 회원 계약 (최초 가입)
SELECT 
  be.business_employee_id,
  e.name as employee_name,
  COUNT(DISTINCT bm.member_id) as new_members
FROM business_employees be
JOIN employees e ON be.employee_id = e.employee_id
LEFT JOIN payments pay ON pay.consultant_id = be.business_employee_id
LEFT JOIN business_members bm ON pay.member_id = bm.member_id
WHERE be.business_id = :businessId 
  AND be.status = 'APPROVED'
  AND bm.join_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01')  -- 이번달 가입
  AND pay.payment_status = 'COMPLETED'
GROUP BY be.business_employee_id, e.name;
```

#### 🔹 **재등록 계약 수**
```sql
-- 이번달 재등록 회원 계약 (기존 회원의 추가 결제)
SELECT 
  be.business_employee_id,
  e.name as employee_name,
  COUNT(pay.id) as renewal_contracts
FROM business_employees be
JOIN employees e ON be.employee_id = e.employee_id
LEFT JOIN payments pay ON pay.consultant_id = be.business_employee_id
LEFT JOIN business_members bm ON pay.member_id = bm.member_id
WHERE be.business_id = :businessId 
  AND be.status = 'APPROVED'
  AND YEAR(pay.payment_date) = YEAR(CURDATE())
  AND MONTH(pay.payment_date) = MONTH(CURDATE())
  AND pay.payment_status = 'COMPLETED'
  AND bm.join_date < DATE_FORMAT(CURDATE(), '%Y-%m-01')  -- 이번달 이전 가입
GROUP BY be.business_employee_id, e.name;
```

### 4.2 **담당 회원 수**

#### 🔹 **현재 활성 담당 회원 수**
```sql
-- 현재 담당하고 있는 활성 회원 수
SELECT 
  be.business_employee_id,
  e.name as employee_name,
  COUNT(bm.member_id) as active_members
FROM business_employees be
JOIN employees e ON be.employee_id = e.employee_id
LEFT JOIN business_members bm ON bm.trainer_id = be.business_employee_id
WHERE be.business_id = :businessId 
  AND be.status = 'APPROVED'
  AND (bm.status IS NULL OR bm.status = 'ACTIVE')
GROUP BY be.business_employee_id, e.name;
```

---

## 📈 **5. 출석률 계산**

### 5.1 **개별 회원 출석률**

#### 🔹 **전체 출석률**
```sql
-- 가입일부터 현재까지의 출석률
SELECT 
  member_id,
  total_days,
  attended_days,
  (attended_days * 100.0 / total_days) as attendance_rate,
  CASE 
    WHEN (attended_days * 100.0 / total_days) >= 80 THEN '우수'
    WHEN (attended_days * 100.0 / total_days) >= 40 THEN '보통'
    ELSE '미흡'
  END as rating
FROM (
  SELECT 
    bm.member_id,
    DATEDIFF(CURDATE(), bm.join_date) + 1 as total_days,
    COUNT(a.attendance_date) as attended_days
  FROM business_members bm
  LEFT JOIN attendances a ON bm.member_id = a.member_id 
    AND a.status = 'PRESENT'
    AND a.attendance_date >= bm.join_date
  WHERE bm.business_id = :businessId 
    AND bm.member_id = :memberId
  GROUP BY bm.member_id, bm.join_date
) attendance_stats;
```

#### 🔹 **최근 30일/90일 방문횟수**
```sql
-- 최근 30일 방문횟수
SELECT COUNT(*)
FROM attendances a
JOIN business_members bm ON a.member_id = bm.member_id
WHERE bm.business_id = :businessId 
  AND bm.member_id = :memberId
  AND a.attendance_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
  AND a.status = 'PRESENT';

-- 최근 90일 방문횟수
SELECT COUNT(*)
FROM attendances a
JOIN business_members bm ON a.member_id = bm.member_id
WHERE bm.business_id = :businessId 
  AND bm.member_id = :memberId
  AND a.attendance_date >= DATE_SUB(CURDATE(), INTERVAL 90 DAY)
  AND a.status = 'PRESENT';
```

---

## 🏆 **6. 락커룸 통계**

### 6.1 **락커 현황 요약**

#### 🔹 **락커 상태별 집계**
```sql
-- 락커 상태별 개수
SELECT 
  status,
  COUNT(*) as count
FROM lockers 
WHERE business_id = :businessId 
GROUP BY status;

-- 상세 상태 계산
SELECT 
  SUM(CASE WHEN status = 'AVAILABLE' THEN 1 ELSE 0 END) as available_count,
  SUM(CASE WHEN status = 'OCCUPIED' THEN 1 ELSE 0 END) as occupied_count,
  SUM(CASE WHEN status = 'MAINTENANCE' THEN 1 ELSE 0 END) as maintenance_count,
  -- 만료 임박 락커 (30일 이내 만료)
  SUM(CASE 
    WHEN la.end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) 
    THEN 1 ELSE 0 
  END) as expiring_soon_count,
  -- 만료된 락커
  SUM(CASE 
    WHEN la.end_date < CURDATE() 
    THEN 1 ELSE 0 
  END) as expired_count
FROM lockers l
LEFT JOIN locker_assignments la ON l.locker_id = la.locker_id AND la.status = 'ACTIVE'
WHERE l.business_id = :businessId;
```

---

## 📊 **7. 실시간 갱신 정책**

### 7.1 **데이터 갱신 시점**

| 통계 종류 | 갱신 시점 | 갱신 방법 |
|-----------|-----------|-----------|
| **대시보드 통계** | 페이지 로드 시마다 | 실시간 쿼리 |
| **회원 통계** | 회원권 등록/수정/삭제 시 | 이벤트 기반 + 실시간 쿼리 |
| **매출 통계** | 결제 완료/취소 시 | 실시간 쿼리 |
| **직원 실적** | 결제 등록/수정 시 | 실시간 쿼리 |
| **출석률** | 출석 체크 시 | 실시간 쿼리 |
| **락커룸 현황** | 락커 배정/해제 시 | 실시간 쿼리 |

### 7.2 **성능 최적화**

#### 🔹 **인덱스 전략**
```sql
-- 주요 통계 쿼리 최적화를 위한 인덱스
CREATE INDEX idx_payments_business_date ON payments(business_id, payment_date, payment_status);
CREATE INDEX idx_payments_business_month ON payments(business_id, YEAR(payment_date), MONTH(payment_date));
CREATE INDEX idx_business_members_business_status ON business_members(business_id, status);
CREATE INDEX idx_attendances_member_date ON attendances(member_id, attendance_date, status);
CREATE INDEX idx_business_employees_business_status ON business_employees(business_id, status);
```

#### 🔹 **캐싱 전략**
- **대시보드 통계**: 5분 캐시 (Redis)
- **월별 매출**: 1시간 캐시
- **회원 상태별 요약**: 30분 캐시
- **직원 실적**: 1시간 캐시

---

## 🎯 **8. 정확성 보장 방안**

### 8.1 **데이터 일관성**

1. **트랜잭션 처리**: 결제/회원권 등록 시 관련 통계 원자적 업데이트
2. **제약조건**: 데이터베이스 레벨에서 무결성 보장
3. **검증 로직**: 통계 계산 전후 데이터 검증

### 8.2 **예외 상황 처리**

1. **데이터 누락**: NULL 값 처리 및 기본값 설정
2. **날짜 경계**: 시간대 고려한 날짜 계산
3. **상태 변경**: 회원/직원 상태 변경 시 통계 재계산

---

## 📝 **9. 사용 예시**

### 9.1 **대시보드 API 응답 예시**
```json
{
  "dashboard": {
    "todaySales": 1500000,
    "todaySalesGrowth": 15.5,
    "monthSales": 45000000,
    "todayVisitors": 85,
    "todayVisitorsGrowth": -2.3,
    "thisWeekVisitors": 520,
    "salesBreakdown": {
      "membership": 60.5,
      "personalTraining": 25.2,
      "locker": 8.1,
      "others": 6.2
    }
  }
}
```

### 9.2 **회원 통계 API 응답 예시**
```json
{
  "memberStats": {
    "summary": {
      "totalMembers": 450,
      "activeMembers": 320,
      "expiringSoonMembers": 35,
      "expiredMembers": 60,
      "suspendedMembers": 15,
      "newMembersThisMonth": 25
    },
    "monthlyTrend": [
      {"month": "2024-01", "total": 300, "membership": 180, "personalTraining": 120},
      {"month": "2024-02", "total": 315, "membership": 190, "personalTraining": 125}
    ],
    "productDistribution": [
      {"productName": "헬스 1개월", "memberCount": 120, "percentage": 37.5},
      {"productName": "PT 10회", "memberCount": 80, "percentage": 25.0}
    ]
  }
}
```

이 문서를 통해 시스템의 모든 통계가 정확하고 일관되게 계산될 수 있도록 보장합니다. 🚀