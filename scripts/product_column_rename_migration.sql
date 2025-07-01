-- ==========================================
-- PRODUCT 테이블 컬럼명 변경 마이그레이션
-- ==========================================
-- 목적: DURATION → VALID_DAYS, SESSION_COUNT → USAGE_COUNT로 의미 명확화
-- 실행 일시: 2024-12-25
-- ==========================================

-- 마이그레이션 시작 로그
INSERT INTO migration_log (script_name, status, started_at, notes) 
VALUES ('product_column_rename_migration', 'STARTED', NOW(), 'DURATION → VALID_DAYS, SESSION_COUNT → USAGE_COUNT 컬럼명 변경');

-- ==========================================
-- 1단계: 현재 상태 백업 및 확인
-- ==========================================

-- 1-1. 기존 products 테이블 구조 확인
DESCRIBE products;

-- 1-2. 기존 데이터 백업 (혹시 모를 상황에 대비)
CREATE TABLE products_backup_20241225 AS SELECT * FROM products;

-- 1-3. 현재 데이터 상태 확인
SELECT 
    product_type,
    COUNT(*) as count,
    COUNT(CASE WHEN duration IS NOT NULL THEN 1 END) as duration_used,
    COUNT(CASE WHEN session_count IS NOT NULL THEN 1 END) as session_count_used,
    AVG(duration) as avg_duration,
    AVG(session_count) as avg_session_count
FROM products 
GROUP BY product_type;

-- ==========================================
-- 2단계: 새로운 컬럼 추가
-- ==========================================

-- 2-1. valid_days 컬럼 추가 (기존 duration을 대체)
ALTER TABLE products 
ADD COLUMN valid_days INT COMMENT '이용 가능 일수 (Ex. 헬스권 1개월 = 30일)';

-- 2-2. usage_count 컬럼 추가 (기존 session_count를 대체)
ALTER TABLE products 
ADD COLUMN usage_count INT COMMENT '사용 가능 횟수 (Ex. 프로틴음료 10회, 개인레슨 20회)';

-- ==========================================
-- 3단계: 데이터 마이그레이션
-- ==========================================

-- 3-1. duration 데이터를 valid_days로 복사
UPDATE products 
SET valid_days = duration 
WHERE duration IS NOT NULL;

-- 3-2. session_count 데이터를 usage_count로 복사
UPDATE products 
SET usage_count = session_count 
WHERE session_count IS NOT NULL;

-- ==========================================
-- 4단계: 데이터 검증
-- ==========================================

-- 4-1. 마이그레이션 결과 확인
SELECT 
    product_type,
    COUNT(*) as total_products,
    COUNT(CASE WHEN duration IS NOT NULL THEN 1 END) as old_duration_count,
    COUNT(CASE WHEN valid_days IS NOT NULL THEN 1 END) as new_valid_days_count,
    COUNT(CASE WHEN session_count IS NOT NULL THEN 1 END) as old_session_count_count,
    COUNT(CASE WHEN usage_count IS NOT NULL THEN 1 END) as new_usage_count_count
FROM products 
GROUP BY product_type;

-- 4-2. 데이터 일치성 검증
SELECT 
    '데이터 검증' as check_type,
    CASE 
        WHEN (SELECT COUNT(*) FROM products WHERE duration != valid_days AND duration IS NOT NULL AND valid_days IS NOT NULL) = 0 
        THEN 'PASS' 
        ELSE 'FAIL' 
    END as duration_migration_result,
    CASE 
        WHEN (SELECT COUNT(*) FROM products WHERE session_count != usage_count AND session_count IS NOT NULL AND usage_count IS NOT NULL) = 0 
        THEN 'PASS' 
        ELSE 'FAIL' 
    END as session_count_migration_result;

-- 4-3. 불일치 데이터가 있다면 조회 (디버깅용)
SELECT product_id, product_name, product_type, duration, valid_days, session_count, usage_count
FROM products 
WHERE (duration != valid_days AND duration IS NOT NULL AND valid_days IS NOT NULL)
   OR (session_count != usage_count AND session_count IS NOT NULL AND usage_count IS NOT NULL);

-- ==========================================
-- 5단계: 기존 컬럼 제거 (검증 완료 후)
-- ==========================================

-- 주의: 이 단계는 검증 완료 후에만 실행하세요!
-- 5-1. 기존 duration 컬럼 삭제
-- ALTER TABLE products DROP COLUMN duration;

-- 5-2. 기존 session_count 컬럼 삭제  
-- ALTER TABLE products DROP COLUMN session_count;

-- ==========================================
-- 6단계: 인덱스 추가 (성능 최적화)
-- ==========================================

-- 6-1. 새로운 컬럼들에 대한 인덱스 추가
CREATE INDEX idx_products_valid_days ON products(valid_days);
CREATE INDEX idx_products_usage_count ON products(usage_count);
CREATE INDEX idx_products_type_valid_days ON products(product_type, valid_days);
CREATE INDEX idx_products_type_usage_count ON products(product_type, usage_count);

-- ==========================================
-- 7단계: 마이그레이션 완료 처리
-- ==========================================

-- 7-1. 최종 결과 리포트
SELECT 
    'PRODUCT 컬럼명 변경 마이그레이션 완료' as message,
    (SELECT COUNT(*) FROM products) as total_products,
    (SELECT COUNT(*) FROM products WHERE valid_days IS NOT NULL) as products_with_valid_days,
    (SELECT COUNT(*) FROM products WHERE usage_count IS NOT NULL) as products_with_usage_count,
    NOW() as completed_at;

-- 7-2. 상품 유형별 통계
SELECT 
    product_type,
    COUNT(*) as total,
    COUNT(valid_days) as with_valid_days,
    COUNT(usage_count) as with_usage_count,
    AVG(valid_days) as avg_valid_days,
    AVG(usage_count) as avg_usage_count,
    MIN(valid_days) as min_valid_days,
    MAX(valid_days) as max_valid_days,
    MIN(usage_count) as min_usage_count,
    MAX(usage_count) as max_usage_count
FROM products
GROUP BY product_type
ORDER BY product_type;

-- 7-3. 마이그레이션 완료 로그
UPDATE migration_log 
SET status = 'COMPLETED', completed_at = NOW(),
    notes = CONCAT(notes, ' | 완료: ', 
                  (SELECT COUNT(*) FROM products), '개 상품 처리, ',
                  (SELECT COUNT(*) FROM products WHERE valid_days IS NOT NULL), '개 기간제, ',
                  (SELECT COUNT(*) FROM products WHERE usage_count IS NOT NULL), '개 횟수제')
WHERE script_name = 'product_column_rename_migration' AND status = 'STARTED';

-- ==========================================
-- 샘플 데이터 확인 쿼리
-- ==========================================

-- 각 상품 유형별 샘플 데이터 조회
SELECT product_id, product_name, product_type, valid_days, usage_count, price, is_active
FROM products 
WHERE product_type = 'MEMBERSHIP'
LIMIT 5;

SELECT product_id, product_name, product_type, valid_days, usage_count, price, is_active
FROM products 
WHERE product_type = 'PERSONAL_TRAINING'
LIMIT 5;

SELECT product_id, product_name, product_type, valid_days, usage_count, price, is_active
FROM products 
WHERE product_type = 'LOCKER'
LIMIT 5;

SELECT product_id, product_name, product_type, valid_days, usage_count, price, is_active
FROM products 
WHERE product_type = 'OTHERS'
LIMIT 5;

-- ==========================================
-- 롤백 스크립트 (문제 발생 시 사용)
-- ==========================================

/*
-- 롤백이 필요한 경우 아래 스크립트 사용:

-- 1. 기존 컬럼 다시 추가
ALTER TABLE products ADD COLUMN duration INT;
ALTER TABLE products ADD COLUMN session_count INT;

-- 2. 데이터 복원
UPDATE products SET duration = valid_days WHERE valid_days IS NOT NULL;
UPDATE products SET session_count = usage_count WHERE usage_count IS NOT NULL;

-- 3. 새 컬럼 삭제
ALTER TABLE products DROP COLUMN valid_days;
ALTER TABLE products DROP COLUMN usage_count;

-- 4. 백업에서 완전 복원 (최후의 수단)
DROP TABLE products;
RENAME TABLE products_backup_20241225 TO products;
*/

-- ==========================================
-- 참고 정보
-- ==========================================

/*
컬럼명 변경 사유:
1. duration → valid_days
   - 기존: 모호한 의미 (기간이지만 단위 불명확)
   - 변경: 명확한 의미 (이용 가능한 일수)
   - 예시: 헬스권 1개월 = valid_days 30

2. session_count → usage_count  
   - 기존: 개인레슨에만 국한된 의미
   - 변경: 모든 횟수제 상품에 적용 가능한 의미
   - 예시: 개인레슨 10회, 프로틴음료 10회 등

적용 상품 예시:
- MEMBERSHIP: valid_days 사용 (헬스권 30일, 필라테스 90일)
- PERSONAL_TRAINING: usage_count 사용 (PT 10회, 20회)  
- LOCKER: valid_days 사용 (락커 30일, 90일)
- OTHERS: valid_days 또는 usage_count 사용 (프로틴 10회, 운동복 대여 7일)
*/