-- ==========================================
-- 회원 테이블 구조 개선 설계
-- ==========================================
-- 목적: 회원 개인정보와 사업장별 소속정보 분리
-- ==========================================

-- 1. 새로운 MEMBERS 테이블 (개인 정보만)
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

-- 2. 사업장-회원 관계 테이블 (사업장별 소속 정보)
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

-- 3. 회원권/이용권 테이블 (기존 payments 테이블 확장)
-- payments 테이블이 이미 사업장별 이용권 정보를 관리하므로 그대로 사용
-- 단, member_id 참조를 새로운 members_new 테이블로 변경해야 함

-- 4. 출석 테이블 수정 필요
-- attendances 테이블의 member_id도 새로운 members_new 테이블을 참조하도록 변경

-- ==========================================
-- 주요 개선사항
-- ==========================================

/*
기존 문제점:
1. sms_consent - 사업장별로 다를 수 있는 수신동의가 회원 테이블에 있음
2. memo - 사업장별 특이사항이 회원 개인정보에 포함됨  
3. trainer_id - 담당 직원이 회원 개인정보에 포함됨
4. status - 회원 상태가 사업장별로 다를 수 있음

개선된 구조:
1. members_new - 순수한 회원 개인정보만 (이름, 연락처, 소셜로그인 정보 등)
2. business_members_new - 사업장별 회원 관계정보 (담당직원, 상태, 메모, 수신동의 등)
3. 회원이 여러 사업장에 등록 가능
4. 각 사업장별로 다른 담당직원, 상태, 메모 관리 가능
*/