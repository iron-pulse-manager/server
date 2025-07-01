-- ==========================================
-- 회원 테이블 마이그레이션 스크립트
-- ==========================================
-- 목적: 기존 members 테이블을 새로운 구조로 안전하게 마이그레이션
-- 실행 순서: 1) 백업 -> 2) 새 테이블 생성 -> 3) 데이터 마이그레이션 -> 4) 검증
-- ==========================================

-- 0. 마이그레이션 시작 로그
INSERT INTO migration_log (script_name, status, started_at) 
VALUES ('member_table_migration', 'STARTED', NOW());

-- ==========================================
-- 1단계: 기존 테이블 백업
-- ==========================================
-- 1-1. 기존 members 테이블 백업 생성
CREATE TABLE members_backup_20241225 AS SELECT * FROM members;
-- 백업된 레코드 수 확인
SELECT COUNT(*) as backup_count FROM members_backup_20241225;

-- 1-2. 기존 테이블 인덱스 정보 백업 (필요시 복구용)
SHOW INDEX FROM members;

-- ==========================================
-- 2단계: 새로운 테이블 구조 생성
-- ==========================================

-- 2-1. 새로운 members 테이블 생성 (개인정보만)
CREATE TABLE members_new (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '이름',
    email VARCHAR(100) COMMENT '이메일',
    phone VARCHAR(20) COMMENT '전화번호',
    profile_image_url VARCHAR(500) COMMENT '프로필 이미지 URL',
    birth_date DATE COMMENT '생년월일',
    gender ENUM('MALE','FEMALE') COMMENT '성별',
    address VARCHAR(500) COMMENT '주소',
    kakao_id VARCHAR(100) UNIQUE COMMENT '카카오 로그인 ID',
    apple_id VARCHAR(100) UNIQUE COMMENT '애플 로그인 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    INDEX idx_members_kakao (kakao_id),
    INDEX idx_members_apple (apple_id),
    INDEX idx_members_phone (phone),
    INDEX idx_members_email (email),
    INDEX idx_members_name (name)
) COMMENT='회원 기본정보 테이블 (소셜로그인 지원)';

-- 2-2. 사업장-회원 관계 테이블 생성
CREATE TABLE business_members_new (
    business_member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_id BIGINT NOT NULL COMMENT '사업장 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    trainer_id BIGINT COMMENT '담당 트레이너 ID',
    member_number VARCHAR(50) COMMENT '회원번호 (사업장별)',
    status ENUM('ACTIVE','INACTIVE','SUSPENDED','EXPIRED') NOT NULL DEFAULT 'ACTIVE' COMMENT '회원상태',
    sms_consent BOOLEAN NOT NULL DEFAULT FALSE COMMENT '문자 수신동의',
    memo TEXT COMMENT '특이사항 메모 (사업장별)',
    join_date DATE NOT NULL COMMENT '등록일',
    last_visit_date DATE COMMENT '마지막 방문일',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members_new(member_id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES business_employees(business_employee_id),
    
    -- 같은 사업장에서 같은 회원은 하나의 활성 관계만 가능
    UNIQUE KEY unique_business_member (business_id, member_id),
    
    INDEX idx_business_members_business (business_id),
    INDEX idx_business_members_member (member_id),
    INDEX idx_business_members_trainer (trainer_id),
    INDEX idx_business_members_status (status),
    INDEX idx_business_members_business_status (business_id, status),
    INDEX idx_business_members_join_date (join_date)
) COMMENT='사업장-회원 소속관계 테이블';

-- ==========================================
-- 3단계: 데이터 마이그레이션
-- ==========================================

-- 3-1. 기존 members 테이블 데이터를 새로운 members_new 테이블로 이전
INSERT INTO members_new (
    member_id,
    name,
    email,
    phone,
    profile_image_url,
    birth_date,
    gender,
    address,
    created_at,
    updated_at
)
SELECT 
    id,
    name,
    email,
    phone,
    profile_image_url,
    birth_date,
    gender,
    address,
    created_at,
    updated_at
FROM members;

-- 마이그레이션된 회원 수 확인
SELECT COUNT(*) as migrated_members_count FROM members_new;

-- 3-2. payments 테이블에서 사업장-회원 관계 정보 추출하여 business_members_new에 삽입
-- 각 회원의 첫 결제일을 join_date로 사용하고, 기존 members 테이블의 정보를 활용
INSERT INTO business_members_new (
    business_id,
    member_id,
    trainer_id,
    member_number,
    status,
    sms_consent,
    memo,
    join_date,
    created_at,
    updated_at
)
SELECT DISTINCT
    p.business_id,
    p.member_id,
    CASE 
        WHEN m.trainer_id IS NOT NULL THEN 
            (SELECT be.business_employee_id 
             FROM business_employees be 
             WHERE be.employee_id = m.trainer_id 
             AND be.business_id = p.business_id 
             AND be.status = 'APPROVED' 
             LIMIT 1)
        ELSE NULL 
    END as trainer_id,
    m.member_number,
    CASE 
        WHEN m.status = 'ACTIVE' THEN 'ACTIVE'
        WHEN m.status = 'EXPIRED' THEN 'EXPIRED'
        WHEN m.status = 'SUSPENDED' THEN 'SUSPENDED'
        ELSE 'ACTIVE'
    END as status,
    COALESCE(m.sms_consent, FALSE) as sms_consent,
    m.memo,
    COALESCE(
        (SELECT MIN(pay_date) FROM payments WHERE member_id = p.member_id AND business_id = p.business_id),
        m.created_at
    ) as join_date,
    NOW() as created_at,
    NOW() as updated_at
FROM payments p
INNER JOIN members m ON p.member_id = m.id
WHERE p.member_id IS NOT NULL AND p.business_id IS NOT NULL;

-- 마이그레이션된 사업장-회원 관계 수 확인
SELECT COUNT(*) as migrated_business_members_count FROM business_members_new;

-- ==========================================
-- 4단계: 데이터 무결성 검증
-- ==========================================

-- 4-1. 회원 수 일치 확인
SELECT 
    (SELECT COUNT(*) FROM members) as original_count,
    (SELECT COUNT(*) FROM members_new) as new_count,
    CASE 
        WHEN (SELECT COUNT(*) FROM members) = (SELECT COUNT(*) FROM members_new) 
        THEN 'PASS' 
        ELSE 'FAIL' 
    END as validation_result;

-- 4-2. 사업장별 회원 수 검증
SELECT 
    bm.business_id,
    COUNT(*) as member_count,
    COUNT(DISTINCT bm.member_id) as unique_member_count
FROM business_members_new bm
GROUP BY bm.business_id
ORDER BY bm.business_id;

-- 4-3. 이상 데이터 체크
-- 중복된 사업장-회원 관계 확인
SELECT business_id, member_id, COUNT(*) as duplicate_count
FROM business_members_new
GROUP BY business_id, member_id
HAVING COUNT(*) > 1;

-- 존재하지 않는 member_id 확인
SELECT bm.member_id
FROM business_members_new bm
LEFT JOIN members_new m ON bm.member_id = m.member_id
WHERE m.member_id IS NULL;

-- 존재하지 않는 trainer_id 확인
SELECT bm.trainer_id
FROM business_members_new bm
LEFT JOIN business_employees be ON bm.trainer_id = be.business_employee_id
WHERE bm.trainer_id IS NOT NULL AND be.business_employee_id IS NULL;

-- ==========================================
-- 5단계: 관련 테이블 외래키 업데이트
-- ==========================================

-- 5-1. payments 테이블의 member_id 컬럼이 새로운 members_new 테이블을 참조하도록 수정
-- (실제로는 기존 ID가 그대로 유지되므로 별도 작업 불필요)

-- 5-2. attendances 테이블의 member_id도 새로운 members_new를 참조하도록 수정
-- (실제로는 기존 ID가 그대로 유지되므로 별도 작업 불필요)

-- ==========================================
-- 6단계: 기존 테이블 이름 변경 및 새 테이블 활성화
-- ==========================================

-- 주의: 이 단계는 서비스 중단 시간이 발생할 수 있으므로 신중하게 실행
-- 6-1. 기존 테이블 이름 변경 (임시로 _old 접미사 추가)
-- RENAME TABLE members TO members_old;

-- 6-2. 새 테이블을 정식 테이블 이름으로 변경
-- RENAME TABLE members_new TO members;
-- RENAME TABLE business_members_new TO business_members;

-- ==========================================
-- 7단계: 외래키 제약조건 재설정
-- ==========================================

-- 7-1. payments 테이블 외래키 재설정
-- ALTER TABLE payments DROP FOREIGN KEY IF EXISTS fk_payments_member;
-- ALTER TABLE payments ADD CONSTRAINT fk_payments_member 
--     FOREIGN KEY (member_id) REFERENCES members(member_id);

-- 7-2. attendances 테이블 외래키 재설정
-- ALTER TABLE attendances DROP FOREIGN KEY IF EXISTS fk_attendances_member;
-- ALTER TABLE attendances ADD CONSTRAINT fk_attendances_member 
--     FOREIGN KEY (member_id) REFERENCES members(member_id);

-- ==========================================
-- 8단계: 마이그레이션 완료 및 정리
-- ==========================================

-- 8-1. 마이그레이션 완료 로그
UPDATE migration_log 
SET status = 'COMPLETED', completed_at = NOW() 
WHERE script_name = 'member_table_migration' AND status = 'STARTED';

-- 8-2. 최종 검증 쿼리
SELECT 
    '회원 테이블 마이그레이션 완료' as message,
    (SELECT COUNT(*) FROM members_new) as total_members,
    (SELECT COUNT(*) FROM business_members_new) as total_business_member_relations,
    (SELECT COUNT(DISTINCT business_id) FROM business_members_new) as businesses_with_members,
    NOW() as completed_at;

-- ==========================================
-- 마이그레이션 로그 테이블 (없다면 생성)
-- ==========================================
CREATE TABLE IF NOT EXISTS migration_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    script_name VARCHAR(100) NOT NULL,
    status ENUM('STARTED', 'COMPLETED', 'FAILED') NOT NULL,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    error_message TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- 중요 참고사항
-- ==========================================
/*
1. 실행 전 체크리스트:
   - 데이터베이스 전체 백업 필수
   - 서비스 점검 모드 전환
   - 충분한 디스크 공간 확보
   - 트랜잭션 로그 공간 확보

2. 롤백 방안:
   - members_backup_20241225 테이블을 이용한 원복
   - 기존 테이블 구조 복원 스크립트 준비

3. 성능 고려사항:
   - 대용량 데이터의 경우 배치 처리 고려
   - 인덱스 생성은 데이터 입력 후 수행
   - 피크 시간 외 실행 권장

4. 애플리케이션 코드 변경사항:
   - Member 엔티티 수정사항 적용
   - BusinessMember 엔티티 추가
   - Repository 인터페이스 업데이트
   - Service 로직 수정

5. 테스트 계획:
   - 개발/스테이징 환경에서 선행 테스트
   - 데이터 무결성 검증
   - 애플리케이션 기능 테스트
   - 성능 테스트
*/