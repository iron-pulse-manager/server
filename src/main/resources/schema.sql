-- ===================================================
-- 🏋️ 피트니스 관리 시스템 - 데이터베이스 스키마 DDL
-- ===================================================

-- 기존 테이블 삭제 (역순으로)
DROP TABLE IF EXISTS community_comments;
DROP TABLE IF EXISTS community_posts;
DROP TABLE IF EXISTS statistics;
DROP TABLE IF EXISTS weights;
DROP TABLE IF EXISTS diets;
DROP TABLE IF EXISTS workouts;
DROP TABLE IF EXISTS attendances;
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS message_recipients;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS lockers;
DROP TABLE IF EXISTS day_passes;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS business_members;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS businesses;
DROP TABLE IF EXISTS owners;

-- ===================================================
-- 1. 사장님 (owners) 테이블
-- ===================================================
CREATE TABLE owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사장님 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '로그인 ID',
    password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    name VARCHAR(50) NOT NULL COMMENT '이름',
    email VARCHAR(100) UNIQUE COMMENT '이메일',
    phone VARCHAR(20) COMMENT '전화번호',
    profile_image_url VARCHAR(500) COMMENT '프로필 이미지 URL',
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE' COMMENT '상태',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사장님';

-- ===================================================
-- 2. 사업장 (businesses) 테이블
-- ===================================================
CREATE TABLE businesses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사업장 ID',
    owner_id BIGINT NOT NULL COMMENT '사장님 ID',
    business_name VARCHAR(100) NOT NULL COMMENT '사업장명',
    business_number VARCHAR(20) UNIQUE COMMENT '사업자번호',
    address VARCHAR(500) COMMENT '주소',
    phone VARCHAR(20) COMMENT '전화번호',
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'SUSPENDED') NOT NULL DEFAULT 'PENDING' COMMENT '승인상태',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_businesses_owner FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사업장';

-- ===================================================
-- 3. 직원 (employees) 테이블
-- ===================================================
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '직원 ID',
    business_id BIGINT COMMENT '소속 사업장 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '로그인 ID',
    password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    name VARCHAR(50) NOT NULL COMMENT '이름',
    email VARCHAR(100) COMMENT '이메일',
    phone VARCHAR(20) COMMENT '전화번호',
    profile_image_url VARCHAR(500) COMMENT '프로필 이미지 URL',
    position VARCHAR(50) COMMENT '직책',
    working_hours VARCHAR(100) COMMENT '근무시간대',
    account_info VARCHAR(200) COMMENT '계좌정보',
    join_date DATE COMMENT '입사일',
    status ENUM('PENDING', 'NORMAL', 'LEAVE', 'RESIGNED', 'REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT '직원상태',
    memo TEXT COMMENT '특이사항 메모',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_employees_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='직원';

-- ===================================================
-- 4. 회원 (members) 테이블
-- ===================================================
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 ID',
    name VARCHAR(50) NOT NULL COMMENT '이름',
    email VARCHAR(100) COMMENT '이메일',
    phone VARCHAR(20) COMMENT '전화번호',
    profile_image_url VARCHAR(500) COMMENT '프로필 이미지 URL',
    birth_date DATE COMMENT '생년월일',
    gender ENUM('MALE', 'FEMALE') COMMENT '성별',
    address VARCHAR(500) COMMENT '주소',
    kakao_id VARCHAR(100) UNIQUE COMMENT '카카오 로그인 ID',
    apple_id VARCHAR(100) UNIQUE COMMENT '애플 로그인 ID',
    sms_consent BOOLEAN NOT NULL DEFAULT FALSE COMMENT '문자 수신동의',
    memo TEXT COMMENT '특이사항 메모',
    trainer_id BIGINT COMMENT '담당 직원 ID',
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE' COMMENT '회원상태',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_members_trainer FOREIGN KEY (trainer_id) REFERENCES employees(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원';

-- ===================================================
-- 5. 사업장-회원 관계 (business_members) 테이블
-- ===================================================
CREATE TABLE business_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '관계 ID',
    business_id BIGINT NOT NULL COMMENT '사업장 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE' COMMENT '관계상태',
    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_business_members_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_business_members_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    UNIQUE KEY uk_business_member_relation (business_id, member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사업장-회원 관계';

-- ===================================================
-- 6. 상품 (products) 테이블
-- ===================================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    product_name VARCHAR(100) NOT NULL COMMENT '상품명',
    product_type ENUM('MEMBERSHIP', 'PERSONAL_TRAINING', 'LOCKER', 'OTHERS') NOT NULL COMMENT '상품 유형',
    price DECIMAL(10,0) NOT NULL COMMENT '가격',
    duration INT COMMENT '기간 (일 단위)',
    session_count INT COMMENT '횟수 (개인레슨용)',
    description TEXT COMMENT '상품 설명',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '활성화 여부',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_products_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY uk_products_name_per_business (business_id, product_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상품';

-- ===================================================
-- 7. 통합 결제 (payments) 테이블
-- ===================================================
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '결제 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    member_id BIGINT NOT NULL COMMENT '결제한 회원 ID',
    product_id BIGINT NOT NULL COMMENT '구매한 상품 ID',
    consultant_id BIGINT COMMENT '상담 직원 ID',
    trainer_id BIGINT COMMENT '담당 강사 ID',
    product_price DECIMAL(10,0) NOT NULL COMMENT '상품 금액',
    actual_price DECIMAL(10,0) NOT NULL COMMENT '실제 결제 금액',
    instructor_commission DECIMAL(10,0) COMMENT '강사 매출 실적',
    outstanding_amount DECIMAL(10,0) DEFAULT 0 COMMENT '미수금',
    payment_method ENUM('CARD', 'CASH', 'TRANSFER') NOT NULL COMMENT '결제수단',
    payment_status ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'COMPLETED' COMMENT '결제상태',
    purchase_purpose VARCHAR(100) COMMENT '구매 목적',
    memo TEXT COMMENT '특이사항 메모',
    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
    service_start_date DATE NOT NULL COMMENT '서비스 시작일',
    service_end_date DATE NOT NULL COMMENT '서비스 종료일',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_payments_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    CONSTRAINT fk_payments_consultant FOREIGN KEY (consultant_id) REFERENCES employees(id) ON DELETE SET NULL,
    CONSTRAINT fk_payments_trainer FOREIGN KEY (trainer_id) REFERENCES employees(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='통합 결제';

-- ===================================================
-- 인덱스 생성
-- ===================================================

-- Unique Indexes
CREATE UNIQUE INDEX idx_owners_username ON owners(username);
CREATE UNIQUE INDEX idx_owners_email ON owners(email);
CREATE UNIQUE INDEX idx_businesses_number ON businesses(business_number);
CREATE UNIQUE INDEX idx_employees_username ON employees(username);
CREATE UNIQUE INDEX idx_members_kakao ON members(kakao_id);
CREATE UNIQUE INDEX idx_members_apple ON members(apple_id);

-- Performance Indexes
-- 사업장 관련
CREATE INDEX idx_businesses_owner ON businesses(owner_id);
CREATE INDEX idx_businesses_status ON businesses(status);

-- 직원 관련
CREATE INDEX idx_employees_business ON employees(business_id);
CREATE INDEX idx_employees_status ON employees(status);
CREATE INDEX idx_employees_business_status ON employees(business_id, status);

-- 회원 관련
CREATE INDEX idx_members_trainer ON members(trainer_id);
CREATE INDEX idx_members_status ON members(status);
CREATE INDEX idx_members_phone ON members(phone);
CREATE INDEX idx_members_email ON members(email);

-- 사업장-회원 관계
CREATE INDEX idx_business_members_business ON business_members(business_id);
CREATE INDEX idx_business_members_member ON business_members(member_id);
CREATE INDEX idx_business_members_status ON business_members(status);

-- 상품 관련
CREATE INDEX idx_products_business ON products(business_id);
CREATE INDEX idx_products_type ON products(product_type);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_products_business_type ON products(business_id, product_type);

-- 결제 관련 (매우 중요 - 통계 쿼리용)
CREATE INDEX idx_payments_business ON payments(business_id);
CREATE INDEX idx_payments_member ON payments(member_id);
CREATE INDEX idx_payments_product ON payments(product_id);
CREATE INDEX idx_payments_consultant ON payments(consultant_id);
CREATE INDEX idx_payments_trainer ON payments(trainer_id);
CREATE INDEX idx_payments_status ON payments(payment_status);
CREATE INDEX idx_payments_date ON payments(payment_date);
CREATE INDEX idx_payments_business_date ON payments(business_id, payment_date);
CREATE INDEX idx_payments_business_status ON payments(business_id, payment_status);
CREATE INDEX idx_payments_trainer_date ON payments(trainer_id, payment_date);

-- 일일권 관련 인덱스
CREATE INDEX idx_day_passes_business ON day_passes(business_id);
CREATE INDEX idx_day_passes_visit_date ON day_passes(visit_date);
CREATE INDEX idx_day_passes_business_date ON day_passes(business_id, visit_date);

-- 락커 관련 인덱스
CREATE INDEX idx_lockers_business ON lockers(business_id);
CREATE INDEX idx_lockers_member ON lockers(member_id);
CREATE INDEX idx_lockers_status ON lockers(status);
CREATE INDEX idx_lockers_business_status ON lockers(business_id, status);

-- 지출 관련 인덱스
CREATE INDEX idx_expenses_business ON expenses(business_id);
CREATE INDEX idx_expenses_date ON expenses(expense_date);
CREATE INDEX idx_expenses_type ON expenses(expense_type);
CREATE INDEX idx_expenses_business_date ON expenses(business_id, expense_date);

-- 메시지 관련 인덱스
CREATE INDEX idx_messages_business ON messages(business_id);
CREATE INDEX idx_messages_sender ON messages(sender_id);
CREATE INDEX idx_messages_status ON messages(status);
CREATE INDEX idx_message_recipients_message ON message_recipients(message_id);
CREATE INDEX idx_message_recipients_member ON message_recipients(member_id);

-- 일정 관련 인덱스
CREATE INDEX idx_schedules_business ON schedules(business_id);
CREATE INDEX idx_schedules_date ON schedules(schedule_date);
CREATE INDEX idx_schedules_type ON schedules(schedule_type);
CREATE INDEX idx_schedules_business_date ON schedules(business_id, schedule_date);

-- 출석 관련 인덱스
CREATE INDEX idx_attendances_business ON attendances(business_id);
CREATE INDEX idx_attendances_member ON attendances(member_id);
CREATE INDEX idx_attendances_date ON attendances(attendance_date);
CREATE INDEX idx_attendances_member_date ON attendances(member_id, attendance_date);

-- 운동일지 관련 인덱스
CREATE INDEX idx_workouts_business ON workouts(business_id);
CREATE INDEX idx_workouts_member ON workouts(member_id);
CREATE INDEX idx_workouts_trainer ON workouts(trainer_id);
CREATE INDEX idx_workouts_date ON workouts(workout_date);
CREATE INDEX idx_workouts_member_date ON workouts(member_id, workout_date);

-- 식단 관련 인덱스
CREATE INDEX idx_diets_business ON diets(business_id);
CREATE INDEX idx_diets_member ON diets(member_id);
CREATE INDEX idx_diets_trainer ON diets(trainer_id);
CREATE INDEX idx_diets_date ON diets(diet_date);
CREATE INDEX idx_diets_member_date ON diets(member_id, diet_date);

-- 체중관리 관련 인덱스
CREATE INDEX idx_weights_member ON weights(member_id);
CREATE INDEX idx_weights_date ON weights(record_date);
CREATE INDEX idx_weights_member_date ON weights(member_id, record_date);

-- 통계 관련 인덱스
CREATE INDEX idx_statistics_business ON statistics(business_id);
CREATE INDEX idx_statistics_type ON statistics(statistics_type);
CREATE INDEX idx_statistics_date ON statistics(target_date);
CREATE INDEX idx_statistics_business_type ON statistics(business_id, statistics_type);

-- 커뮤니티 관련 인덱스
CREATE INDEX idx_community_posts_business ON community_posts(business_id);
CREATE INDEX idx_community_posts_author ON community_posts(author_id);
CREATE INDEX idx_community_posts_type ON community_posts(community_type);
CREATE INDEX idx_community_posts_created ON community_posts(created_at);
CREATE INDEX idx_community_comments_post ON community_comments(post_id);
CREATE INDEX idx_community_comments_author ON community_comments(author_id);
CREATE INDEX idx_community_comments_parent ON community_comments(parent_id);

-- Full-Text Search Indexes (MySQL 5.7+ 지원)
-- CREATE FULLTEXT INDEX idx_members_search ON members(name, phone);
-- CREATE FULLTEXT INDEX idx_employees_search ON employees(name, phone);
-- CREATE FULLTEXT INDEX idx_businesses_search ON businesses(business_name, address);
-- CREATE FULLTEXT INDEX idx_products_search ON products(product_name, description);
-- CREATE FULLTEXT INDEX idx_community_posts_search ON community_posts(title, content);

-- ===================================================
-- 초기 데이터 삽입 (테스트용)
-- ===================================================

-- 샘플 사장님 데이터
INSERT INTO owners (username, password, name, email, phone) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '김사장', 'admin@fitness.com', '010-1234-5678');

-- 샘플 사업장 데이터
INSERT INTO businesses (owner_id, business_name, business_number, address, phone, status) VALUES 
(1, '헬스킹 강남점', '123-45-67890', '서울시 강남구 테헤란로 123', '02-1234-5678', 'APPROVED');

-- 샘플 직원 데이터
INSERT INTO employees (business_id, username, password, name, email, phone, position, status) VALUES 
(1, 'trainer01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '박트레이너', 'trainer@fitness.com', '010-2345-6789', '헬스트레이너', 'NORMAL');

-- 샘플 회원 데이터
INSERT INTO members (name, email, phone, kakao_id, trainer_id) VALUES 
('김회원', 'member@test.com', '010-3456-7890', 'kakao123', 1);

-- 샘플 사업장-회원 관계
INSERT INTO business_members (business_id, member_id) VALUES 
(1, 1);

-- 샘플 상품 데이터
INSERT INTO products (business_id, product_name, product_type, price, duration, description) VALUES 
(1, '헬스 1개월권', 'MEMBERSHIP', 100000, 30, '헬스장 이용권 1개월'),
(1, '개인레슨 10회', 'PERSONAL_TRAINING', 500000, 60, '개인 트레이닝 10회 패키지'),
(1, '락커 1개월', 'LOCKER', 30000, 30, '개인 락커 1개월 이용권');

-- 샘플 결제 데이터
INSERT INTO payments (business_id, member_id, product_id, consultant_id, trainer_id, product_price, actual_price, instructor_commission, payment_method, service_start_date, service_end_date) VALUES 
(1, 1, 1, 1, 1, 100000, 100000, 40000, 'CARD', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY));

-- 샘플 일일권 데이터
INSERT INTO day_passes (business_id, visitor_name, visitor_phone, price, payment_method) VALUES 
(1, '이방문', '010-4567-8901', 15000, 'CASH');

-- 샘플 락커 데이터 (1-200번 락커 생성)
INSERT INTO lockers (business_id, locker_number, status) 
SELECT 1, n, 'AVAILABLE'
FROM (
    SELECT a.N + b.N * 10 + c.N * 100 + 1 as n
    FROM 
        (SELECT 0 as N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
        CROSS JOIN (SELECT 0 as N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
        CROSS JOIN (SELECT 0 as N UNION ALL SELECT 1 UNION ALL SELECT 2) c
) numbers
WHERE n <= 200;

-- 샘플 락커 사용 (1번 락커를 1번 회원이 사용)
UPDATE lockers SET member_id = 1, payment_id = 1, status = 'OCCUPIED', 
                   start_date = CURDATE(), end_date = DATE_ADD(CURDATE(), INTERVAL 30 DAY)
WHERE business_id = 1 AND locker_number = 1;

-- 샘플 지출 데이터
INSERT INTO expenses (business_id, expense_name, expense_type, amount, description, expense_date) VALUES 
(1, '직원 급여', 'LABOR_COST', 3000000, '2024년 6월 직원 급여', CURDATE()),
(1, '임대료', 'RENT', 2000000, '2024년 6월 임대료', CURDATE()),
(1, '전기요금', 'UTILITIES', 150000, '2024년 6월 전기요금', CURDATE());

-- 샘플 메시지 데이터
INSERT INTO messages (business_id, sender_id, content, message_type, status, sent_at) VALUES 
(1, 1, '새로운 운동 프로그램이 오픈되었습니다!', 'GROUP', 'SENT', NOW());

INSERT INTO message_recipients (message_id, member_id, member_name, member_phone, delivery_status, delivered_at) VALUES 
(1, 1, '김회원', '010-3456-7890', 'DELIVERED', NOW());

-- 샘플 일정 데이터
INSERT INTO schedules (business_id, title, schedule_type, schedule_date, start_time, end_time, is_all_day, assigned_to, memo) VALUES 
(1, '개인레슨 - 김회원', 'PERSONAL_LESSON', CURDATE(), '14:00:00', '15:00:00', FALSE, '[1]', '하체 중심 운동'),
(1, '시설 정기점검', 'MAINTENANCE', DATE_ADD(CURDATE(), INTERVAL 1 DAY), NULL, NULL, TRUE, '[1]', '월례 시설 점검');

-- 샘플 출석 데이터
INSERT INTO attendances (business_id, member_id, attendance_date, check_in_time, status) VALUES 
(1, 1, CURDATE(), '09:30:00', 'PRESENT'),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '10:15:00', 'PRESENT');

-- 샘플 운동일지 데이터
INSERT INTO workouts (business_id, member_id, trainer_id, workout_date, exercises, notes, member_question, trainer_answer) VALUES 
(1, 1, 1, CURDATE(), 
 '{"exercises": [{"name": "스쿼트", "sets": 3, "reps": 15, "weight": "50kg"}, {"name": "데드리프트", "sets": 3, "reps": 10, "weight": "60kg"}]}',
 '오늘은 하체 운동에 집중했습니다. 폼이 많이 좋아졌어요!',
 '무릎이 조금 아픈데 괜찮을까요?',
 '무릎 통증이 있으시면 무게를 줄이고 스트레칭을 충분히 해주세요.');

-- 샘플 식단 데이터
INSERT INTO diets (business_id, member_id, trainer_id, diet_date, diet_type, meal_plan, actual_meal, trainer_feedback, image_urls) VALUES 
(1, 1, 1, CURDATE(), 'RECOMMENDED',
 '{"breakfast": "오트밀 + 바나나", "lunch": "닭가슴살 샐러드", "dinner": "연어구이 + 브로콜리"}',
 '{"breakfast": "오트밀 + 바나나", "lunch": "닭가슴살 샐러드", "dinner": "피자"}',
 '저녁에 피자 대신 계획했던 연어구이를 드셨으면 더 좋았을 것 같아요!',
 '["https://example.com/diet1.jpg", "https://example.com/diet2.jpg"]');

-- 샘플 체중관리 데이터
INSERT INTO weights (member_id, current_weight, target_weight, body_image_url, record_date, memo) VALUES 
(1, 75.5, 70.0, 'https://example.com/body1.jpg', CURDATE(), '목표까지 5.5kg 남았습니다!'),
(1, 76.0, 70.0, 'https://example.com/body0.jpg', DATE_SUB(CURDATE(), INTERVAL 7 DAY), '첫 측정');

-- 샘플 통계 데이터
INSERT INTO statistics (business_id, statistics_type, target_date, statistics_data, total_amount, total_count) VALUES 
(1, 'DAILY_REVENUE', CURDATE(), 
 '{"membership": 100000, "personal_training": 0, "locker": 0, "others": 15000}', 
 115000, 2),
(1, 'MEMBER_COUNT', CURDATE(),
 '{"total": 1, "active": 1, "expiring_soon": 0, "expired": 0}',
 NULL, 1);

-- 샘플 커뮤니티 게시글 데이터
INSERT INTO community_posts (business_id, author_id, title, content, community_type, post_category, view_count, like_count, comment_count) VALUES 
(1, 1, '헬스장 이용 안내', '안녕하세요! 헬스킹 강남점입니다. 이용 안내사항을 공지드립니다.', 'FREE_BOARD', 'GENERAL', 15, 3, 1),
(1, 1, '덤벨 세트 판매합니다', '집에서 사용하던 덤벨 세트 판매합니다. 상태 좋습니다.', 'MARKETPLACE', 'WEIGHT', 8, 1, 0);

-- 중고거래 게시글 추가 정보 업데이트
UPDATE community_posts 
SET price = 150000, product_condition = '상태좋음', trade_location = '강남구', sale_status = 'SELLING'
WHERE id = 2;

-- 샘플 커뮤니티 댓글 데이터
INSERT INTO community_comments (post_id, author_id, content, like_count) VALUES 
(1, 1, '유용한 정보 감사합니다!', 2);

-- ===================================================
-- 8. 일일권 (day_passes) 테이블
-- ===================================================
CREATE TABLE day_passes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '일일권 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    visitor_name VARCHAR(50) NOT NULL COMMENT '방문자 이름',
    visitor_phone VARCHAR(20) NOT NULL COMMENT '방문자 연락처',
    price DECIMAL(10,0) NOT NULL COMMENT '일일권 금액',
    payment_method ENUM('CARD', 'CASH', 'TRANSFER') NOT NULL COMMENT '결제수단',
    visit_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '방문일시',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_day_passes_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='일일권';

-- ===================================================
-- 9. 락커 (lockers) 테이블
-- ===================================================
CREATE TABLE lockers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '락커 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    member_id BIGINT COMMENT '사용 회원 ID',
    payment_id BIGINT COMMENT '결제 ID',
    locker_number INT NOT NULL COMMENT '락커 번호',
    status ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE' COMMENT '락커 상태',
    start_date DATE COMMENT '사용 시작일',
    end_date DATE COMMENT '사용 종료일',
    memo TEXT COMMENT '특이사항',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_lockers_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_lockers_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE SET NULL,
    CONSTRAINT fk_lockers_payment FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE SET NULL,
    UNIQUE KEY uk_business_locker_number (business_id, locker_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='락커';

-- ===================================================
-- 10. 지출 (expenses) 테이블
-- ===================================================
CREATE TABLE expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '지출 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    expense_name VARCHAR(100) NOT NULL COMMENT '지출명',
    expense_type ENUM('LABOR_COST', 'RENT', 'UTILITIES', 'OTHERS') NOT NULL COMMENT '지출 유형',
    amount DECIMAL(10,0) NOT NULL COMMENT '지출 금액',
    description TEXT COMMENT '지출 상세 내용',
    expense_date DATE NOT NULL COMMENT '지출 날짜',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_expenses_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='지출';

-- ===================================================
-- 11. 메시지 (messages) 테이블
-- ===================================================
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '메시지 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    sender_id BIGINT NOT NULL COMMENT '발송자 ID (직원)',
    content TEXT NOT NULL COMMENT '메시지 내용',
    message_type ENUM('SINGLE', 'GROUP') NOT NULL COMMENT '메시지 유형',
    status ENUM('PENDING', 'SENT', 'FAILED') NOT NULL DEFAULT 'PENDING' COMMENT '발송 상태',
    sent_at TIMESTAMP COMMENT '발송일시',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_messages_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='메시지';

-- ===================================================
-- 12. 메시지 수신자 (message_recipients) 테이블
-- ===================================================
CREATE TABLE message_recipients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '수신자 ID',
    message_id BIGINT NOT NULL COMMENT '메시지 ID',
    member_id BIGINT NOT NULL COMMENT '수신자 회원 ID',
    member_name VARCHAR(50) NOT NULL COMMENT '수신자 이름',
    member_phone VARCHAR(20) NOT NULL COMMENT '수신자 전화번호',
    delivery_status ENUM('PENDING', 'DELIVERED', 'FAILED') NOT NULL DEFAULT 'PENDING' COMMENT '전송 상태',
    delivered_at TIMESTAMP COMMENT '전송일시',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_message_recipients_message FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_recipients_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='메시지 수신자';

-- ===================================================
-- 13. 일정 (schedules) 테이블
-- ===================================================
CREATE TABLE schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '일정 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    title VARCHAR(100) NOT NULL COMMENT '일정 제목',
    schedule_type ENUM('PERSONAL_LESSON', 'HOLIDAY', 'MAINTENANCE', 'OTHERS') NOT NULL COMMENT '일정 유형',
    schedule_date DATE NOT NULL COMMENT '일정 날짜',
    start_time TIME COMMENT '시작 시간',
    end_time TIME COMMENT '종료 시간',
    is_all_day BOOLEAN NOT NULL DEFAULT FALSE COMMENT '하루종일 여부',
    assigned_to TEXT COMMENT '담당자 (직원 ID들, JSON 형태)',
    memo TEXT COMMENT '메모',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_schedules_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='일정';

-- ===================================================
-- 14. 출석 (attendances) 테이블
-- ===================================================
CREATE TABLE attendances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '출석 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    attendance_date DATE NOT NULL COMMENT '출석 날짜',
    check_in_time TIME COMMENT '입장 시간',
    status ENUM('PRESENT', 'ABSENT') NOT NULL DEFAULT 'PRESENT' COMMENT '출석 상태',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_attendances_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendances_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    UNIQUE KEY uk_attendance_daily (business_id, member_id, attendance_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='출석';

-- ===================================================
-- 15. 운동일지 (workouts) 테이블
-- ===================================================
CREATE TABLE workouts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '운동일지 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    trainer_id BIGINT NOT NULL COMMENT '작성한 트레이너 ID',
    workout_date DATE NOT NULL COMMENT '운동 날짜',
    exercises JSON COMMENT '운동 내용 (JSON)',
    notes TEXT COMMENT '트레이너 메모',
    member_question TEXT COMMENT '회원 질문',
    trainer_answer TEXT COMMENT '트레이너 답변',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_workouts_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_workouts_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_workouts_trainer FOREIGN KEY (trainer_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='운동일지';

-- ===================================================
-- 16. 식단 (diets) 테이블
-- ===================================================
CREATE TABLE diets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '식단 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    trainer_id BIGINT NOT NULL COMMENT '담당 트레이너 ID',
    diet_date DATE NOT NULL COMMENT '식단 날짜',
    diet_type ENUM('RECOMMENDED', 'RECORDED') NOT NULL COMMENT '식단 유형',
    meal_plan JSON COMMENT '식단 계획 (JSON)',
    actual_meal JSON COMMENT '실제 섭취한 식단 (JSON)',
    trainer_feedback TEXT COMMENT '트레이너 피드백',
    image_urls JSON COMMENT '식단 사진 URLs (JSON)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_diets_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_diets_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_diets_trainer FOREIGN KEY (trainer_id) REFERENCES employees(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='식단';

-- ===================================================
-- 17. 체중관리 (weights) 테이블
-- ===================================================
CREATE TABLE weights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '체중기록 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    current_weight DECIMAL(5,2) NOT NULL COMMENT '현재 체중',
    target_weight DECIMAL(5,2) COMMENT '목표 체중',
    body_image_url VARCHAR(500) COMMENT '몸 사진 URL',
    record_date DATE NOT NULL COMMENT '기록 날짜',
    memo TEXT COMMENT '메모',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_weights_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='체중관리';

-- ===================================================
-- 18. 통계 (statistics) 테이블
-- ===================================================
CREATE TABLE statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '통계 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    statistics_type ENUM('DAILY_REVENUE', 'MONTHLY_REVENUE', 'MEMBER_COUNT', 'EMPLOYEE_PERFORMANCE') NOT NULL COMMENT '통계 유형',
    target_date DATE NOT NULL COMMENT '통계 기준일',
    statistics_data JSON COMMENT '통계 데이터 (JSON)',
    total_amount DECIMAL(15,0) COMMENT '총 금액',
    total_count INT COMMENT '총 개수',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_statistics_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY uk_statistics_business_type_date (business_id, statistics_type, target_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='통계';

-- ===================================================
-- 19. 커뮤니티 게시글 (community_posts) 테이블
-- ===================================================
CREATE TABLE community_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID',
    business_id BIGINT NOT NULL COMMENT '소속 사업장 ID',
    author_id BIGINT NOT NULL COMMENT '작성자 ID (사장님)',
    title VARCHAR(200) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    community_type ENUM('FREE_BOARD', 'MARKETPLACE') NOT NULL COMMENT '커뮤니티 유형',
    post_category VARCHAR(50) COMMENT '게시글 카테고리',
    image_urls JSON COMMENT '첨부 이미지 URLs (JSON)',
    view_count INT NOT NULL DEFAULT 0 COMMENT '조회수',
    like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    comment_count INT NOT NULL DEFAULT 0 COMMENT '댓글 수',
    price DECIMAL(10,0) COMMENT '판매가격 (중고거래용)',
    product_condition VARCHAR(50) COMMENT '상품상태 (중고거래용)',
    trade_location VARCHAR(200) COMMENT '거래희망지역 (중고거래용)',
    sale_status ENUM('SELLING', 'RESERVED', 'SOLD') COMMENT '판매상태 (중고거래용)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_community_posts_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_community_posts_author FOREIGN KEY (author_id) REFERENCES owners(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='커뮤니티 게시글';

-- ===================================================
-- 20. 커뮤니티 댓글 (community_comments) 테이블
-- ===================================================
CREATE TABLE community_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID',
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    author_id BIGINT NOT NULL COMMENT '작성자 ID',
    parent_id BIGINT COMMENT '부모 댓글 ID (대댓글용)',
    content TEXT NOT NULL COMMENT '댓글 내용',
    like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    
    CONSTRAINT fk_community_comments_post FOREIGN KEY (post_id) REFERENCES community_posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_community_comments_author FOREIGN KEY (author_id) REFERENCES owners(id) ON DELETE CASCADE,
    CONSTRAINT fk_community_comments_parent FOREIGN KEY (parent_id) REFERENCES community_comments(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='커뮤니티 댓글';