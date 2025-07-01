-- ===================================================
-- 🏋️ 피트니스 관리 시스템 - 초기 데이터 (개발용)
-- ===================================================

-- 개발용 초기 데이터 (로컬 환경에서만 사용)
-- 패스워드는 모두 'password'로 설정됨

-- ===================================================
-- 1. 사장님 계정 데이터
-- ===================================================
INSERT IGNORE INTO owners (id, username, password, name, email, phone, status) VALUES 
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '김사장', 'admin@fitness.com', '010-1234-5678', 'ACTIVE'),
(2, 'owner2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '이사장', 'owner2@fitness.com', '010-1234-5679', 'ACTIVE');

-- ===================================================
-- 2. 사업장 데이터
-- ===================================================
INSERT IGNORE INTO businesses (id, owner_id, business_name, business_number, address, phone, status) VALUES 
(1, 1, '헬스킹 강남점', '123-45-67890', '서울시 강남구 테헤란로 123', '02-1234-5678', 'APPROVED'),
(2, 1, '헬스킹 홍대점', '234-56-78901', '서울시 마포구 홍익로 456', '02-2345-6789', 'APPROVED'),
(3, 2, '필라테스 스튜디오 서초점', '345-67-89012', '서울시 서초구 강남대로 789', '02-3456-7890', 'APPROVED');

-- ===================================================
-- 3. 직원 데이터 (패스워드: password)
-- ===================================================
INSERT IGNORE INTO employees (id, business_id, username, password, name, email, phone, position, working_hours, join_date, status) VALUES 
(1, 1, 'trainer01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '김트레이너', 'trainer01@fitness.com', '010-2345-6789', '헬스트레이너', '09:00-18:00', '2024-01-01', 'NORMAL'),
(2, 1, 'trainer02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '박강사', 'trainer02@fitness.com', '010-3456-7890', '필라테스강사', '10:00-19:00', '2024-01-15', 'NORMAL'),
(3, 2, 'trainer03', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '이코치', 'trainer03@fitness.com', '010-4567-8901', '헬스트레이너', '08:00-17:00', '2024-02-01', 'NORMAL'),
(4, 3, 'trainer04', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyE8ZTBi5Im', '최강사', 'trainer04@fitness.com', '010-5678-9012', '필라테스강사', '09:00-18:00', '2024-02-15', 'NORMAL');

-- ===================================================
-- 4. 회원 데이터
-- ===================================================
INSERT IGNORE INTO members (id, name, phone, email, birth_date, gender, kakao_id, apple_id, sms_consent, trainer_id, status) VALUES 
(1, '홍길동', '010-1111-1111', 'hong@example.com', '1990-01-01', 'MALE', 'kakao_hong', NULL, TRUE, 1, 'ACTIVE'),
(2, '김영희', '010-2222-2222', 'kim@example.com', '1992-05-15', 'FEMALE', 'kakao_kim', NULL, TRUE, 1, 'ACTIVE'),
(3, '박철수', '010-3333-3333', 'park@example.com', '1988-12-20', 'MALE', NULL, 'apple_park', FALSE, 2, 'ACTIVE'),
(4, '이미영', '010-4444-4444', 'lee@example.com', '1995-08-10', 'FEMALE', 'kakao_lee', NULL, TRUE, 3, 'ACTIVE'),
(5, '정우성', '010-5555-5555', 'jung@example.com', '1985-03-25', 'MALE', NULL, 'apple_jung', TRUE, 4, 'ACTIVE');

-- ===================================================
-- 5. 사업장-회원 관계 데이터
-- ===================================================
INSERT IGNORE INTO business_members (id, business_id, member_id, status, join_date) VALUES 
(1, 1, 1, 'ACTIVE', '2024-01-01 09:00:00'),
(2, 1, 2, 'ACTIVE', '2024-01-15 10:30:00'),
(3, 1, 3, 'ACTIVE', '2024-02-01 14:20:00'),
(4, 2, 4, 'ACTIVE', '2024-02-15 11:45:00'),
(5, 3, 5, 'ACTIVE', '2024-03-01 16:15:00'),
-- 다중 사업장 등록 예시
(6, 2, 1, 'ACTIVE', '2024-03-15 13:30:00');

-- ===================================================
-- 6. 상품 데이터
-- ===================================================
INSERT IGNORE INTO products (id, business_id, product_name, product_type, price, duration, session_count, description, is_active) VALUES 
-- 헬스킹 강남점 상품
(1, 1, '헬스 1개월권', 'MEMBERSHIP', 100000, 30, NULL, '헬스장 이용권 1개월', TRUE),
(2, 1, '헬스 3개월권', 'MEMBERSHIP', 270000, 90, NULL, '헬스장 이용권 3개월 (10% 할인)', TRUE),
(3, 1, '헬스 6개월권', 'MEMBERSHIP', 480000, 180, NULL, '헬스장 이용권 6개월 (20% 할인)', TRUE),
(4, 1, '개인레슨 10회', 'PERSONAL_TRAINING', 500000, 60, 10, '개인 트레이닝 10회 패키지', TRUE),
(5, 1, '개인레슨 20회', 'PERSONAL_TRAINING', 900000, 90, 20, '개인 트레이닝 20회 패키지 (10% 할인)', TRUE),
(6, 1, '락커 1개월', 'LOCKER', 30000, 30, NULL, '개인 락커 1개월 이용권', TRUE),
(7, 1, '락커 3개월', 'LOCKER', 80000, 90, NULL, '개인 락커 3개월 이용권', TRUE),
(8, 1, '운동복 세트', 'OTHERS', 50000, NULL, NULL, '헬스킹 브랜드 운동복 세트', TRUE),

-- 헬스킹 홍대점 상품
(9, 2, '헬스 1개월권', 'MEMBERSHIP', 90000, 30, NULL, '헬스장 이용권 1개월', TRUE),
(10, 2, '헬스 3개월권', 'MEMBERSHIP', 250000, 90, NULL, '헬스장 이용권 3개월', TRUE),
(11, 2, '개인레슨 10회', 'PERSONAL_TRAINING', 450000, 60, 10, '개인 트레이닝 10회 패키지', TRUE),
(12, 2, '락커 1개월', 'LOCKER', 25000, 30, NULL, '개인 락커 1개월 이용권', TRUE),

-- 필라테스 스튜디오 서초점 상품
(13, 3, '필라테스 1개월권', 'MEMBERSHIP', 150000, 30, NULL, '필라테스 무제한 이용권 1개월', TRUE),
(14, 3, '필라테스 3개월권', 'MEMBERSHIP', 400000, 90, NULL, '필라테스 무제한 이용권 3개월', TRUE),
(15, 3, '개인레슨 5회', 'PERSONAL_TRAINING', 350000, 30, 5, '개인 필라테스 5회 패키지', TRUE),
(16, 3, '개인레슨 10회', 'PERSONAL_TRAINING', 650000, 60, 10, '개인 필라테스 10회 패키지', TRUE);

-- ===================================================
-- 7. 결제 데이터
-- ===================================================
INSERT IGNORE INTO payments (id, business_id, member_id, product_id, consultant_id, trainer_id, product_price, actual_price, instructor_commission, outstanding_amount, payment_method, payment_status, purchase_purpose, payment_date, service_start_date, service_end_date) VALUES 
-- 홍길동의 헬스킹 강남점 결제 내역
(1, 1, 1, 1, 1, 1, 100000, 100000, 40000, 0, 'CARD', 'COMPLETED', '체력증진', '2024-01-01 09:00:00', '2024-01-01', '2024-01-31'),
(2, 1, 1, 4, 1, 1, 500000, 500000, 200000, 0, 'CARD', 'COMPLETED', '개인맞춤운동', '2024-01-15 14:30:00', '2024-01-15', '2024-03-15'),

-- 김영희의 헬스킹 강남점 결제 내역
(3, 1, 2, 2, 1, 1, 270000, 250000, 100000, 20000, 'TRANSFER', 'COMPLETED', '다이어트', '2024-01-15 10:30:00', '2024-01-15', '2024-04-15'),
(4, 1, 2, 6, 1, 1, 30000, 30000, 0, 0, 'CASH', 'COMPLETED', '편의성', '2024-01-15 10:45:00', '2024-01-15', '2024-02-15'),

-- 박철수의 헬스킹 강남점 결제 내역
(5, 1, 3, 3, 2, 2, 480000, 480000, 0, 0, 'CARD', 'COMPLETED', '건강관리', '2024-02-01 14:20:00', '2024-02-01', '2024-08-01'),

-- 이미영의 헬스킹 홍대점 결제 내역
(6, 2, 4, 9, 3, 3, 90000, 90000, 36000, 0, 'CARD', 'COMPLETED', '체형관리', '2024-02-15 11:45:00', '2024-02-15', '2024-03-17'),
(7, 2, 4, 11, 3, 3, 450000, 450000, 180000, 0, 'CARD', 'COMPLETED', '전문지도', '2024-03-01 16:20:00', '2024-03-01', '2024-04-30'),

-- 정우성의 필라테스 스튜디오 서초점 결제 내역
(8, 3, 5, 13, 4, 4, 150000, 150000, 60000, 0, 'TRANSFER', 'COMPLETED', '자세교정', '2024-03-01 16:15:00', '2024-03-01', '2024-03-31'),
(9, 3, 5, 15, 4, 4, 350000, 300000, 120000, 50000, 'CARD', 'COMPLETED', '재활운동', '2024-03-15 10:30:00', '2024-03-15', '2024-04-14'),

-- 홍길동의 헬스킹 홍대점 결제 내역 (다중 사업장 이용)
(10, 2, 1, 10, 3, 3, 250000, 250000, 100000, 0, 'CARD', 'COMPLETED', '지점 이용', '2024-03-15 13:30:00', '2024-03-15', '2024-06-15');