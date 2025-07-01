-- ==========================================
-- 직원 테이블 구조 개선 마이그레이션 스크립트
-- ==========================================
-- 작성일: 2025-06-25
-- 목적: 직원 개인정보와 사업장별 소속정보 분리
-- ==========================================

-- 1. 기존 EMPLOYEE 테이블 백업
CREATE TABLE employees_backup AS SELECT * FROM employees;

-- 2. 새로운 EMPLOYEE 테이블 생성 (개인 정보만, 소셜 로그인 지원)
CREATE TABLE employees_new (
    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '이름',
    phone_number VARCHAR(20) COMMENT '전화번호',
    email VARCHAR(100) COMMENT '이메일',
    kakao_id VARCHAR(100) UNIQUE COMMENT '카카오 로그인 ID',
    apple_id VARCHAR(100) UNIQUE COMMENT '애플 로그인 ID',
    birth_date DATE COMMENT '생년월일',
    gender ENUM('M', 'F') COMMENT '성별',
    address VARCHAR(255) COMMENT '주소',
    profile_image_url VARCHAR(500) COMMENT '프로필 이미지 URL',
    bank_name VARCHAR(50) COMMENT '은행명',
    account_number VARCHAR(50) COMMENT '계좌번호',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    INDEX idx_employees_phone (phone_number),
    INDEX idx_employees_email (email),
    INDEX idx_employees_kakao (kakao_id),
    INDEX idx_employees_apple (apple_id)
) COMMENT='직원 기본정보 테이블 (소셜로그인 지원)';

-- 3. 사업장-직원 연결 테이블 생성 (사업장별 소속 정보)
CREATE TABLE business_employees (
    business_employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_id BIGINT NOT NULL COMMENT '사업장 ID',
    employee_id BIGINT NOT NULL COMMENT '직원 ID',
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'RESIGNED') NOT NULL DEFAULT 'PENDING' COMMENT '상태',
    position VARCHAR(50) COMMENT '직책 (헬스트레이너, 요가강사, 팀장 등)',
    working_hours VARCHAR(100) COMMENT '근무시간대',
    join_date DATE COMMENT '입사일',
    resign_date DATE COMMENT '퇴사일',
    memo TEXT COMMENT '특이사항 메모',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '신청일',
    approved_at TIMESTAMP NULL COMMENT '승인일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees_new(employee_id) ON DELETE CASCADE,
    
    -- 같은 사업장에서 동시에 활성 상태인 직원은 1명만 허용
    UNIQUE KEY unique_active_employee (business_id, employee_id),
    
    INDEX idx_business_employees_business (business_id),
    INDEX idx_business_employees_employee (employee_id),
    INDEX idx_business_employees_status (status),
    INDEX idx_business_employees_business_status (business_id, status)
) COMMENT='사업장-직원 소속관계 테이블';

-- 4. 기존 데이터 마이그레이션
-- 4-1. 직원 개인정보 데이터 이전 (소셜 로그인 필드 포함)
INSERT INTO employees_new (
    employee_id,
    name, 
    phone_number, 
    email, 
    kakao_id,
    apple_id,
    profile_image_url,
    created_at, 
    updated_at
)
SELECT 
    id,
    name,
    phone, -- 소셜 로그인으로 변경되므로 phone_number는 선택사항
    email,
    NULL as kakao_id, -- 기존 직원은 소셜 로그인 정보 없음
    NULL as apple_id,  -- 기존 직원은 소셜 로그인 정보 없음
    profile_image_url,
    created_at,
    updated_at
FROM employees;

-- 4-2. 사업장-직원 소속관계 데이터 이전
INSERT INTO business_employees (
    business_id,
    employee_id,
    status,
    position,
    working_hours,
    join_date,
    memo,
    created_at,
    approved_at,
    updated_at
)
SELECT 
    business_id,
    id as employee_id,
    CASE 
        WHEN status = 'NORMAL' THEN 'APPROVED'
        WHEN status = 'PENDING' THEN 'PENDING'
        WHEN status = 'REJECTED' THEN 'REJECTED'
        WHEN status = 'RESIGNED' THEN 'RESIGNED'
        WHEN status = 'LEAVE' THEN 'RESIGNED'
        ELSE 'PENDING'
    END as status,
    position,
    working_hours,
    join_date,
    memo,
    created_at,
    CASE WHEN status = 'NORMAL' THEN updated_at ELSE NULL END as approved_at,
    updated_at
FROM employees
WHERE business_id IS NOT NULL;

-- 5. 기존 테이블 이름 변경 및 새 테이블 적용
RENAME TABLE employees TO employees_old;
RENAME TABLE employees_new TO employees;

-- 6. 관련 테이블들의 외래키 업데이트 (필요시)
-- payments 테이블의 consultant_id, trainer_id 컬럼 업데이트는 별도 스크립트에서 처리
-- (business_employees 테이블을 통해 조인하여 처리해야 함)

-- 7. 인덱스 생성 (성능 최적화)
CREATE INDEX idx_business_employees_approved ON business_employees(business_id, status) 
WHERE status = 'APPROVED';

CREATE INDEX idx_business_employees_join_date ON business_employees(join_date);

-- 8. 새로운 테이블 구조 검증 쿼리
-- 검증 1: 직원 수 일치 확인
SELECT 
    '기존 직원 수' as description,
    COUNT(*) as count
FROM employees_old
UNION ALL
SELECT 
    '새 직원 수' as description,
    COUNT(*) as count
FROM employees;

-- 검증 2: 사업장별 직원 수 확인  
SELECT 
    '기존 사업장별 직원 수' as description,
    business_id,
    COUNT(*) as count
FROM employees_old
WHERE business_id IS NOT NULL
GROUP BY business_id
UNION ALL
SELECT 
    '새 사업장별 직원 수' as description,
    business_id,
    COUNT(*) as count
FROM business_employees
WHERE status = 'APPROVED'
GROUP BY business_id;

-- 9. 마이그레이션 완료 후 정리 작업 (수동 실행 권장)
-- DROP TABLE employees_backup; -- 백업 테이블 삭제 (문제없음 확인 후)
-- DROP TABLE employees_old;     -- 기존 테이블 삭제 (문제없음 확인 후)

-- ==========================================
-- 마이그레이션 완료
-- ==========================================

COMMIT;