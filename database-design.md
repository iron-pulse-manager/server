# 헬스장 관리 SaaS DB 설계

## 1. 프로젝트 개요

### 1.1 시스템 목적
- 헬스장, 필라테스 등 피트니스 사업장의 종합 관리 시스템
- 다중 사업장 관리 지원
- 회원 관리, 매출 관리, 시설 관리, 직원 관리 등 종합 기능 제공

### 1.2 주요 기능
- 사용자/사업장 관리
- 회원 관리 (이용권, 출석, 결제)
- 상품 관리
- 락커룸 관리
- 일일권 관리
- 문자 발송
- 일정 관리
- 직원 관리
- 커뮤니티 (자유소통/중고거래 게시판)
- 통계/지출 관리

### 1.3 기술 스택
- **Backend**: Java + Spring Boot + JPA
- **Database**: MySQL 8.0+
- **Architecture**: Multi-tenant (Business 단위)

---

## 2. 도메인 분석

### 2.1 주요 도메인
1. **사용자/계정 관리** - 로그인, 권한 관리, 마이페이지
2. **사업장 관리** - 다중 사업장 관리, 사업장 연결
3. **회원 관리** - 회원 정보, 이용권, 출석, 결제
4. **상품 관리** - 회원권, 개인레슨, 락커, 기타 상품
5. **일일권 관리** - 일회성 이용권
6. **락커룸 관리** - 락커 현황, 배정
7. **문자 발송** - 단건/단체 문자
8. **일정 관리** - 캘린더, 스케줄
9. **직원 관리** - 직원 정보, 가입 요청
10. **커뮤니티** - 자유소통/중고거래 게시판
11. **통계** - 매출, 회원, 직원 실적
12. **지출 관리** - 사업장 지출 관리

---

## 3. 엔터티 설계

### 3.1 핵심 엔터티

#### 3.1.1 User (사용자)
```sql
-- 사업장 사장님 계정
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50) UNIQUE NOT NULL COMMENT '로그인 ID',
    password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    name VARCHAR(100) NOT NULL COMMENT '이름',
    email VARCHAR(255) UNIQUE COMMENT '이메일',
    phone VARCHAR(20) COMMENT '핸드폰 번호',
    profile_image_url VARCHAR(500) COMMENT '프로필 사진 URL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3.1.2 Business (사업장)
```sql
-- 헬스장, 필라테스 등 사업장 정보
CREATE TABLE businesses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL COMMENT '사업장명',
    address VARCHAR(500) COMMENT '주소',
    business_number VARCHAR(20) COMMENT '사업자번호',
    phone VARCHAR(20) COMMENT '사업장 전화번호',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3.1.3 UserBusinessMapping (사용자-사업장 매핑)
```sql
-- 사용자가 여러 사업장을 관리할 수 있는 관계 테이블
CREATE TABLE user_business_mappings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT 'users.id FK',
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    approval_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_business (user_id, business_id)
);
```

#### 3.1.4 Product (상품)
```sql
-- 회원권, 개인레슨, 락커, 기타 상품
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    name VARCHAR(200) NOT NULL COMMENT '상품명',
    type ENUM('MEMBERSHIP', 'PERSONAL_TRAINING', 'LOCKER', 'OTHER') NOT NULL,
    price DECIMAL(10,2) NOT NULL COMMENT '가격',
    duration_days INT COMMENT '기간(일) - 개인레슨은 NULL',
    session_count INT COMMENT '횟수 - 개인레슨용',
    description TEXT COMMENT '상품 설명',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
);
```

#### 3.1.5 Member (회원)
```sql
-- 헬스장 회원 정보
CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    member_number VARCHAR(50) COMMENT '회원번호',
    name VARCHAR(100) NOT NULL COMMENT '이름',
    phone VARCHAR(20) UNIQUE COMMENT '핸드폰 번호',
    email VARCHAR(255) COMMENT '이메일',
    birth_date DATE COMMENT '생년월일',
    gender ENUM('MALE', 'FEMALE', 'OTHER') COMMENT '성별',
    address VARCHAR(500) COMMENT '주소',
    profile_image_url VARCHAR(500) COMMENT '프로필 사진',
    memo TEXT COMMENT '특이사항 메모',
    sms_consent BOOLEAN DEFAULT FALSE COMMENT '문자 수신동의',
    app_usage BOOLEAN DEFAULT FALSE COMMENT '앱 이용 여부',
    status ENUM('ACTIVE', 'EXPIRED', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_business_member_number (business_id, member_number)
);
```

### 3.2 회원 관리 엔터티

#### 3.2.1 Staff (직원)
```sql
-- 헬스장 직원 (개인 트레이너 등)
CREATE TABLE staffs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    staff_number VARCHAR(50) COMMENT '직원번호',
    name VARCHAR(100) NOT NULL COMMENT '이름',
    phone VARCHAR(20) COMMENT '핸드폰 번호',
    email VARCHAR(255) COMMENT '이메일',
    birth_date DATE COMMENT '생년월일',
    gender ENUM('MALE', 'FEMALE', 'OTHER') COMMENT '성별',
    address VARCHAR(500) COMMENT '주소',
    profile_image_url VARCHAR(500) COMMENT '프로필 사진',
    position VARCHAR(100) COMMENT '직책 (헬스트레이너, 요가강사 등)',
    work_hours VARCHAR(100) COMMENT '근무시간대',
    bank_name VARCHAR(50) COMMENT '은행명',
    account_number VARCHAR(50) COMMENT '계좌번호',
    hire_date DATE COMMENT '입사일',
    memo TEXT COMMENT '특이사항 메모',
    status ENUM('ACTIVE', 'ON_LEAVE', 'RESIGNED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_business_staff_number (business_id, staff_number)
);
```

#### 3.2.2 MemberSubscription (회원 이용권)
```sql
-- 회원의 이용권 정보 (헬스장, 개인레슨, 락커, 기타)
CREATE TABLE member_subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL COMMENT 'members.id FK',
    product_id BIGINT NOT NULL COMMENT 'products.id FK',
    staff_id BIGINT COMMENT 'staffs.id FK (개인레슨 담당자, 상담직원)',
    trainer_staff_id BIGINT COMMENT 'staffs.id FK (개인레슨 전용 트레이너)',
    
    -- 기본 정보
    start_date DATE NOT NULL COMMENT '서비스 시작일',
    end_date DATE NOT NULL COMMENT '서비스 종료일',
    remaining_sessions INT COMMENT '남은 횟수 (개인레슨용)',
    
    -- 결제 정보
    original_price DECIMAL(10,2) NOT NULL COMMENT '상품 금액',
    actual_price DECIMAL(10,2) NOT NULL COMMENT '실제 결제 금액',
    staff_commission DECIMAL(10,2) COMMENT '담당자 매출 실적',
    unpaid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '미수금',
    payment_date DATETIME NOT NULL COMMENT '결제 일시',
    payment_method ENUM('CARD', 'CASH', 'TRANSFER') NOT NULL COMMENT '결제 수단',
    
    -- 메타 정보
    purpose VARCHAR(200) COMMENT '구매 목적 (체력증진, 다이어트 등)',
    memo TEXT COMMENT '특이사항 메모',
    status ENUM('ACTIVE', 'EXPIRED', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (staff_id) REFERENCES staffs(id),
    FOREIGN KEY (trainer_staff_id) REFERENCES staffs(id)
);
```

#### 3.2.3 Attendance (출석)
```sql
-- 회원 출석 기록
CREATE TABLE attendances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL COMMENT 'members.id FK',
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    attendance_date DATE NOT NULL COMMENT '출석 날짜',
    check_in_time TIME COMMENT '입장 시간',
    attendance_type ENUM('MEMBERSHIP', 'DAY_PASS') DEFAULT 'MEMBERSHIP',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_member_date (member_id, attendance_date)
);
```

#### 3.2.4 Payment (결제 내역)
```sql
-- 결제 내역 (이용권별 상세 결제 정보)
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_subscription_id BIGINT NOT NULL COMMENT 'member_subscriptions.id FK',
    member_id BIGINT NOT NULL COMMENT 'members.id FK',
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    
    amount DECIMAL(10,2) NOT NULL COMMENT '결제 금액',
    payment_method ENUM('CARD', 'CASH', 'TRANSFER') NOT NULL,
    payment_date DATETIME NOT NULL COMMENT '결제 일시',
    status ENUM('COMPLETED', 'PENDING', 'CANCELLED') DEFAULT 'COMPLETED',
    
    -- 영수증/계약서 첨부
    receipt_images JSON COMMENT '영수증 이미지 URL 배열',
    contract_images JSON COMMENT '계약서 이미지 URL 배열',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_subscription_id) REFERENCES member_subscriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
);
```

#### 3.2.5 SuspensionHistory (정지 기록)
```sql
-- 회원권 정지 기록
CREATE TABLE suspension_histories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_subscription_id BIGINT NOT NULL COMMENT 'member_subscriptions.id FK',
    member_id BIGINT NOT NULL COMMENT 'members.id FK',
    staff_id BIGINT NOT NULL COMMENT 'staffs.id FK (승인 담당자)',
    
    start_date DATE NOT NULL COMMENT '정지 시작일',
    end_date DATE NOT NULL COMMENT '정지 종료일',
    reason TEXT NOT NULL COMMENT '정지 사유',
    status ENUM('ACTIVE', 'COMPLETED') DEFAULT 'ACTIVE',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_subscription_id) REFERENCES member_subscriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staffs(id)
);
```

#### 3.2.6 ModificationHistory (수정 기록)
```sql
-- 이용권 수정 기록
CREATE TABLE modification_histories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_subscription_id BIGINT NOT NULL COMMENT 'member_subscriptions.id FK',
    staff_id BIGINT NOT NULL COMMENT 'staffs.id FK (수정자)',
    
    modification_type ENUM('UPDATE', 'DELETE') NOT NULL,
    modification_reason TEXT NOT NULL COMMENT '수정 사유',
    modification_content TEXT NOT NULL COMMENT '수정 내용',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_subscription_id) REFERENCES member_subscriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staffs(id)
);
```

### 3.3 시설 관리 엔터티

#### 3.3.1 Locker (락커)
```sql
-- 락커 정보 (1번~200번 락커)
CREATE TABLE lockers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    locker_number INT NOT NULL COMMENT '락커 번호 (1~200)',
    status ENUM('AVAILABLE', 'OCCUPIED', 'OUT_OF_ORDER') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_business_locker (business_id, locker_number)
);
```

#### 3.3.2 LockerAssignment (락커 배정)
```sql
-- 락커 배정 정보
CREATE TABLE locker_assignments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    locker_id BIGINT NOT NULL COMMENT 'lockers.id FK',
    member_subscription_id BIGINT NOT NULL COMMENT 'member_subscriptions.id FK (락커 이용권)',
    member_id BIGINT NOT NULL COMMENT 'members.id FK',
    
    start_date DATE NOT NULL COMMENT '배정 시작일',
    end_date DATE NOT NULL COMMENT '배정 종료일',
    memo TEXT COMMENT '특이사항 메모',
    status ENUM('ACTIVE', 'EXPIRED') DEFAULT 'ACTIVE',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (locker_id) REFERENCES lockers(id) ON DELETE CASCADE,
    FOREIGN KEY (member_subscription_id) REFERENCES member_subscriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);
```

#### 3.3.3 DayPass (일일권)
```sql
-- 일일권 구매 기록
CREATE TABLE day_passes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    
    -- 구매자 정보
    customer_name VARCHAR(100) NOT NULL COMMENT '구매자 이름',
    customer_phone VARCHAR(20) NOT NULL COMMENT '구매자 핸드폰',
    
    -- 이용 정보
    visit_date DATE NOT NULL COMMENT '방문일자',
    visit_time TIME NOT NULL COMMENT '방문시간',
    
    -- 결제 정보
    amount DECIMAL(10,2) NOT NULL COMMENT '결제 금액',
    payment_method ENUM('CARD', 'CASH', 'TRANSFER') NOT NULL COMMENT '결제 방식',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
);
```

#### 3.3.4 Schedule (일정)
```sql
-- 캘린더 일정 관리
CREATE TABLE schedules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    
    -- 일정 정보
    title VARCHAR(200) NOT NULL COMMENT '일정 제목',
    type ENUM('PERSONAL_TRAINING', 'MAINTENANCE', 'HOLIDAY', 'OTHER') NOT NULL COMMENT '일정 유형',
    description TEXT COMMENT '메모',
    
    -- 날짜/시간 정보
    schedule_date DATE NOT NULL COMMENT '일정 날짜',
    is_all_day BOOLEAN DEFAULT TRUE COMMENT '하루종일 여부',
    start_time TIME COMMENT '시작 시간 (시간 지정시)',
    end_time TIME COMMENT '종료 시간 (시간 지정시)',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
);
```

#### 3.3.5 ScheduleStaff (일정 담당자)
```sql
-- 일정 담당자 매핑
CREATE TABLE schedule_staffs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    schedule_id BIGINT NOT NULL COMMENT 'schedules.id FK',
    staff_id BIGINT COMMENT 'staffs.id FK (NULL이면 전체)',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staffs(id) ON DELETE CASCADE
);
```

#### 3.3.6 StaffJoinRequest (직원 가입 요청)
```sql
-- 직원 가입 요청 관리
CREATE TABLE staff_join_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    
    -- 요청자 정보
    name VARCHAR(100) NOT NULL COMMENT '이름',
    phone VARCHAR(20) NOT NULL COMMENT '핸드폰 번호',
    email VARCHAR(255) COMMENT '이메일',
    address VARCHAR(500) COMMENT '주소',
    profile_image_url VARCHAR(500) COMMENT '프로필 사진',
    bank_name VARCHAR(50) COMMENT '은행명',
    account_number VARCHAR(50) COMMENT '계좌번호',
    
    -- 상태 정보
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP NULL COMMENT '승인/거절 처리 시간',
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE
);
```

### 3.4 커뮤니티 엔터티

#### 3.4.1 Post (게시글)
```sql
-- 게시글 (자유소통 게시판 + 중고거래 게시판)
CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    user_id BIGINT NOT NULL COMMENT 'users.id FK (작성자)',
    
    -- 게시판 구분
    board_type ENUM('FREE', 'MARKETPLACE') NOT NULL COMMENT '자유소통/중고거래',
    
    -- 기본 정보
    title VARCHAR(200) NOT NULL COMMENT '제목',
    content TEXT NOT NULL COMMENT '본문',
    
    -- 자유소통 게시판 전용
    post_type ENUM('GENERAL', 'INFO', 'QUESTION') COMMENT '일반/정보공유/질문',
    
    -- 중고거래 게시판 전용
    product_category ENUM('CARDIO', 'WEIGHT', 'PILATES', 'OTHER') COMMENT '상품 유형',
    product_condition ENUM('NEW', 'LIKE_NEW', 'GOOD', 'FAIR', 'POOR') COMMENT '상품 상태',
    price DECIMAL(10,2) COMMENT '판매 가격',
    trade_location VARCHAR(200) COMMENT '거래 희망 지역',
    sale_status ENUM('SELLING', 'RESERVED', 'SOLD') DEFAULT 'SELLING' COMMENT '판매 상태',
    
    -- 통계 정보
    view_count INT DEFAULT 0 COMMENT '조회수',
    like_count INT DEFAULT 0 COMMENT '좋아요 수',
    comment_count INT DEFAULT 0 COMMENT '댓글 수',
    
    -- 메타 정보
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    INDEX idx_board_type_created (board_type, created_at DESC),
    INDEX idx_business_board_created (business_id, board_type, created_at DESC)
);
```

#### 3.4.2 PostImage (게시글 이미지)
```sql
-- 게시글 첨부 이미지
CREATE TABLE post_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT 'posts.id FK',
    image_url VARCHAR(500) NOT NULL COMMENT '이미지 URL',
    image_order INT NOT NULL COMMENT '이미지 순서',
    is_thumbnail BOOLEAN DEFAULT FALSE COMMENT '대표 이미지 여부',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    INDEX idx_post_order (post_id, image_order)
);
```

#### 3.4.3 Comment (댓글)
```sql
-- 댓글 (2depth만 지원)
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT 'posts.id FK',
    user_id BIGINT NOT NULL COMMENT 'users.id FK (작성자)',
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    parent_comment_id BIGINT COMMENT 'comments.id FK (대댓글인 경우)',
    
    content TEXT NOT NULL COMMENT '댓글 내용',
    like_count INT DEFAULT 0 COMMENT '좋아요 수',
    
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    
    INDEX idx_post_created (post_id, created_at),
    INDEX idx_parent_created (parent_comment_id, created_at)
);
```

#### 3.4.4 PostLike (게시글 좋아요)
```sql
-- 게시글 좋아요
CREATE TABLE post_likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT 'posts.id FK',
    user_id BIGINT NOT NULL COMMENT 'users.id FK',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_post_user (post_id, user_id)
);
```

#### 3.4.5 CommentLike (댓글 좋아요)
```sql
-- 댓글 좋아요
CREATE TABLE comment_likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment_id BIGINT NOT NULL COMMENT 'comments.id FK',
    user_id BIGINT NOT NULL COMMENT 'users.id FK',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_comment_user (comment_id, user_id)
);
```

#### 3.4.6 PostView (게시글 조회 기록)
```sql
-- 게시글 조회 기록 (계정당 1일 1회 조회수 증가)
CREATE TABLE post_views (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT 'posts.id FK',
    user_id BIGINT NOT NULL COMMENT 'users.id FK',
    view_date DATE NOT NULL COMMENT '조회 날짜',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_post_user_date (post_id, user_id, view_date)
);
```

### 3.5 통계/관리 엔터티

#### 3.5.1 Expense (지출)
```sql
-- 사업장 지출 관리
CREATE TABLE expenses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    
    -- 지출 정보
    title VARCHAR(200) NOT NULL COMMENT '지출 제목',
    type ENUM('LABOR', 'RENT', 'UTILITIES', 'OTHER') NOT NULL COMMENT '지출 유형',
    amount DECIMAL(10,2) NOT NULL COMMENT '지출 금액',
    description TEXT COMMENT '지출 상세 내용',
    expense_date DATE NOT NULL COMMENT '지출 날짜',
    
    -- 메타 정보
    created_by BIGINT NOT NULL COMMENT 'users.id FK (등록자)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id),
    
    INDEX idx_business_date (business_id, expense_date DESC),
    INDEX idx_business_type_date (business_id, type, expense_date DESC)
);
```

#### 3.5.2 SMSLog (문자 발송 기록)
```sql
-- 문자 발송 기록
CREATE TABLE sms_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL COMMENT 'businesses.id FK',
    sender_id BIGINT NOT NULL COMMENT 'users.id FK (발송자)',
    
    -- 발송 정보
    message_type ENUM('SINGLE', 'BULK') NOT NULL COMMENT '단건/단체',
    content TEXT NOT NULL COMMENT '문자 내용',
    recipient_count INT NOT NULL COMMENT '수신자 수',
    
    -- 수신자 그룹 (단체 발송시)
    recipient_group ENUM('ALL', 'ACTIVE', 'EXPIRING', 'EXPIRED', 'PERSONAL_TRAINING') COMMENT '수신자 그룹',
    
    -- 발송 결과
    sent_count INT DEFAULT 0 COMMENT '발송 성공 수',
    failed_count INT DEFAULT 0 COMMENT '발송 실패 수',
    status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    
    INDEX idx_business_sent (business_id, sent_at DESC)
);
```

#### 3.5.3 SMSRecipient (문자 수신자 상세)
```sql
-- 문자 수신자 상세 기록
CREATE TABLE sms_recipients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sms_log_id BIGINT NOT NULL COMMENT 'sms_logs.id FK',
    
    -- 수신자 정보
    recipient_type ENUM('MEMBER', 'MANUAL') NOT NULL COMMENT '회원/수동입력',
    member_id BIGINT COMMENT 'members.id FK (회원인 경우)',
    recipient_name VARCHAR(100) NOT NULL COMMENT '수신자 이름',
    recipient_phone VARCHAR(20) NOT NULL COMMENT '수신자 핸드폰',
    
    -- 발송 결과
    status ENUM('SENT', 'FAILED') NOT NULL COMMENT '발송 상태',
    sent_at TIMESTAMP,
    error_message TEXT COMMENT '실패 사유',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (sms_log_id) REFERENCES sms_logs(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE SET NULL,
    
    INDEX idx_sms_log (sms_log_id)
);
```

---

## 4. 엔터티 관계 설계

### 4.1 주요 관계 정의

#### 4.1.1 핵심 엔터티 관계
```
User (1) ←→ (N) UserBusinessMapping (N) ←→ (1) Business
- 사용자는 여러 사업장을 관리할 수 있음
- 사업장은 여러 사용자가 관리할 수 있음

Business (1) ←→ (N) Member
- 사업장은 여러 회원을 가짐

Business (1) ←→ (N) Product  
- 사업장은 여러 상품을 가짐

Business (1) ←→ (N) Staff
- 사업장은 여러 직원을 가짐

Member (1) ←→ (N) MemberSubscription
- 회원은 여러 이용권을 가질 수 있음 (헬스+개인레슨+락커 등)

Product (1) ←→ (N) MemberSubscription
- 상품은 여러 이용권에서 참조됨

Staff (1) ←→ (N) MemberSubscription (상담직원)
Staff (1) ←→ (N) MemberSubscription (트레이너)
- 직원은 여러 이용권의 담당자가 될 수 있음
```

#### 4.1.2 회원 관리 관계
```
Member (1) ←→ (N) Attendance
- 회원은 여러 출석 기록을 가짐

MemberSubscription (1) ←→ (N) Payment  
- 이용권은 여러 결제 기록을 가짐

MemberSubscription (1) ←→ (N) SuspensionHistory
- 이용권은 여러 정지 기록을 가짐

MemberSubscription (1) ←→ (N) ModificationHistory
- 이용권은 여러 수정 기록을 가짐
```

#### 4.1.3 시설 관리 관계
```
Business (1) ←→ (N) Locker
- 사업장은 여러 락커를 가짐

Locker (1) ←→ (N) LockerAssignment  
- 락커는 여러 배정 기록을 가짐 (이력 관리)

MemberSubscription (1) ←→ (1) LockerAssignment
- 락커 이용권은 하나의 락커 배정을 가짐

Business (1) ←→ (N) Schedule
- 사업장은 여러 일정을 가짐

Schedule (1) ←→ (N) ScheduleStaff
- 일정은 여러 담당자를 가질 수 있음
```

#### 4.1.4 커뮤니티 관계
```
User (1) ←→ (N) Post
- 사용자는 여러 게시글을 작성

Post (1) ←→ (N) PostImage
- 게시글은 여러 이미지를 가짐

Post (1) ←→ (N) Comment
- 게시글은 여러 댓글을 가짐

Comment (1) ←→ (N) Comment (대댓글)
- 댓글은 여러 대댓글을 가짐

Post (1) ←→ (N) PostLike
User (1) ←→ (N) PostLike
- 게시글 좋아요는 사용자와 게시글의 다대다 관계

Comment (1) ←→ (N) CommentLike  
User (1) ←→ (N) CommentLike
- 댓글 좋아요는 사용자와 댓글의 다대다 관계
```

---

## 5. 인덱스 전략

### 5.1 핵심 검색 인덱스
```sql
-- 회원 검색용 인덱스
ALTER TABLE members ADD INDEX idx_business_phone (business_id, phone);
ALTER TABLE members ADD INDEX idx_business_name (business_id, name);
ALTER TABLE members ADD INDEX idx_business_member_number (business_id, member_number);
ALTER TABLE members ADD INDEX idx_business_status (business_id, status);

-- 이용권 조회 인덱스  
ALTER TABLE member_subscriptions ADD INDEX idx_member_status (member_id, status);
ALTER TABLE member_subscriptions ADD INDEX idx_business_dates (business_id, start_date, end_date);
ALTER TABLE member_subscriptions ADD INDEX idx_business_product_status (business_id, product_id, status);

-- 출석 조회 인덱스
ALTER TABLE attendances ADD INDEX idx_member_date (member_id, attendance_date DESC);
ALTER TABLE attendances ADD INDEX idx_business_date (business_id, attendance_date DESC);
ALTER TABLE attendances ADD INDEX idx_business_type_date (business_id, attendance_type, attendance_date DESC);

-- 결제 조회 인덱스
ALTER TABLE payments ADD INDEX idx_member_date (member_id, payment_date DESC);
ALTER TABLE payments ADD INDEX idx_business_date (business_id, payment_date DESC);
ALTER TABLE payments ADD INDEX idx_business_status_date (business_id, status, payment_date DESC);
```

### 5.2 커뮤니티 인덱스
```sql
-- 게시글 조회 인덱스
ALTER TABLE posts ADD INDEX idx_business_board_created (business_id, board_type, created_at DESC);
ALTER TABLE posts ADD INDEX idx_business_board_type_created (business_id, board_type, post_type, created_at DESC);
ALTER TABLE posts ADD INDEX idx_business_board_category_created (business_id, board_type, product_category, created_at DESC);

-- 댓글 조회 인덱스
ALTER TABLE comments ADD INDEX idx_post_parent_created (post_id, parent_comment_id, created_at);
ALTER TABLE comments ADD INDEX idx_user_created (user_id, created_at DESC);

-- 좋아요 조회 인덱스
ALTER TABLE post_likes ADD INDEX idx_user_created (user_id, created_at DESC);
ALTER TABLE comment_likes ADD INDEX idx_user_created (user_id, created_at DESC);
```

### 5.3 통계 조회 인덱스
```sql
-- 매출 통계 인덱스
ALTER TABLE member_subscriptions ADD INDEX idx_business_payment_date (business_id, payment_date);
ALTER TABLE day_passes ADD INDEX idx_business_visit_date (business_id, visit_date);

-- 직원 실적 인덱스
ALTER TABLE member_subscriptions ADD INDEX idx_staff_payment_date (staff_id, payment_date);
ALTER TABLE member_subscriptions ADD INDEX idx_trainer_payment_date (trainer_staff_id, payment_date);

-- 지출 조회 인덱스
ALTER TABLE expenses ADD INDEX idx_business_date (business_id, expense_date DESC);
ALTER TABLE expenses ADD INDEX idx_business_type_date (business_id, type, expense_date DESC);
```

---

## 6. 비즈니스 로직 고려사항

### 6.1 회원권 만료 로직
```sql
-- 회원권 상태 자동 업데이트를 위한 배치 작업용 뷰
CREATE VIEW active_subscriptions AS
SELECT 
    ms.*,
    CASE 
        WHEN ms.end_date < CURDATE() THEN 'EXPIRED'
        WHEN ms.end_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) THEN 'EXPIRING_SOON'
        ELSE 'ACTIVE'
    END as calculated_status
FROM member_subscriptions ms
WHERE ms.status IN ('ACTIVE', 'SUSPENDED');
```

### 6.2 출석률 계산
```sql
-- 회원별 출석률 계산을 위한 뷰
CREATE VIEW member_attendance_stats AS
SELECT 
    m.id as member_id,
    m.business_id,
    COUNT(a.id) as total_attendances,
    COUNT(DISTINCT a.attendance_date) as unique_attendance_days,
    DATEDIFF(CURDATE(), MIN(ms.start_date)) as total_membership_days,
    ROUND(COUNT(DISTINCT a.attendance_date) / DATEDIFF(CURDATE(), MIN(ms.start_date)) * 100, 2) as attendance_rate
FROM members m
LEFT JOIN member_subscriptions ms ON m.id = ms.member_id AND ms.status = 'ACTIVE'
LEFT JOIN attendances a ON m.id = a.member_id
WHERE ms.id IS NOT NULL
GROUP BY m.id, m.business_id;
```

### 6.3 매출 통계
```sql
-- 월별 매출 통계를 위한 뷰
CREATE VIEW monthly_revenue_stats AS
SELECT 
    business_id,
    YEAR(payment_date) as year,
    MONTH(payment_date) as month,
    p.type as product_type,
    SUM(ms.actual_price) as total_revenue,
    COUNT(*) as subscription_count
FROM member_subscriptions ms
JOIN products p ON ms.product_id = p.id
WHERE ms.status != 'CANCELLED'
GROUP BY business_id, YEAR(payment_date), MONTH(payment_date), p.type;
```

---

## 7. 데이터 무결성 제약조건

### 7.1 체크 제약조건
```sql
-- 이용권 날짜 유효성 검사
ALTER TABLE member_subscriptions 
ADD CONSTRAINT chk_subscription_dates 
CHECK (end_date >= start_date);

-- 가격 양수 검사
ALTER TABLE products 
ADD CONSTRAINT chk_product_price 
CHECK (price > 0);

ALTER TABLE member_subscriptions 
ADD CONSTRAINT chk_subscription_prices 
CHECK (original_price > 0 AND actual_price >= 0 AND unpaid_amount >= 0);

-- 락커 번호 범위 검사
ALTER TABLE lockers 
ADD CONSTRAINT chk_locker_number 
CHECK (locker_number BETWEEN 1 AND 200);

-- 개인레슨 횟수 유효성 검사
ALTER TABLE member_subscriptions 
ADD CONSTRAINT chk_personal_training_sessions 
CHECK (
    (SELECT type FROM products WHERE id = product_id) != 'PERSONAL_TRAINING' 
    OR remaining_sessions >= 0
);
```

### 7.2 트리거
```sql
-- 이용권 등록시 회원 상태 자동 업데이트
DELIMITER //
CREATE TRIGGER update_member_status_on_subscription
AFTER INSERT ON member_subscriptions
FOR EACH ROW
BEGIN
    UPDATE members 
    SET status = 'ACTIVE'
    WHERE id = NEW.member_id;
END//

-- 게시글 댓글 수 자동 업데이트
CREATE TRIGGER increment_comment_count
AFTER INSERT ON comments
FOR EACH ROW
BEGIN
    UPDATE posts 
    SET comment_count = comment_count + 1
    WHERE id = NEW.post_id;
END//

CREATE TRIGGER decrement_comment_count
AFTER DELETE ON comments
FOR EACH ROW
BEGIN
    UPDATE posts 
    SET comment_count = comment_count - 1
    WHERE id = OLD.post_id;
END//
DELIMITER ;
```

---

## 8. 성능 최적화 방안

### 8.1 파티셔닝
```sql
-- 출석 테이블 월별 파티셔닝
ALTER TABLE attendances
PARTITION BY RANGE (YEAR(attendance_date) * 100 + MONTH(attendance_date)) (
    PARTITION p202401 VALUES LESS THAN (202402),
    PARTITION p202402 VALUES LESS THAN (202403),
    -- ... 계속 추가
);

-- 결제 테이블 월별 파티셔닝  
ALTER TABLE payments
PARTITION BY RANGE (YEAR(payment_date) * 100 + MONTH(payment_date)) (
    PARTITION p202401 VALUES LESS THAN (202402),
    PARTITION p202402 VALUES LESS THAN (202403),
    -- ... 계속 추가
);
```

### 8.2 캐싱 전략
```java
// 자주 조회되는 데이터에 대한 캐싱 전략 (Spring Boot + Redis)

// 사업장별 활성 회원 수 캐싱 (1시간)
@Cacheable(value = "business:active-members", key = "#businessId")
public Long getActiveMememberCount(Long businessId);

// 상품 목록 캐싱 (30분)
@Cacheable(value = "business:products", key = "#businessId")
public List<Product> getBusinessProducts(Long businessId);

// 락커 현황 캐싱 (5분)
@Cacheable(value = "business:locker-status", key = "#businessId")
public LockerStatusDto getLockerStatus(Long businessId);
```

---

## 9. 보안 고려사항

### 9.1 데이터 암호화
```sql
-- 개인정보 암호화 저장
-- 실제 구현시 애플리케이션 레벨에서 AES 암호화 적용
-- phone, email 등 민감정보는 암호화하여 저장
```

### 9.2 접근 권한 관리
```sql
-- 사업장별 데이터 접근 제한을 위한 Row Level Security
-- MySQL 8.0에서는 애플리케이션 레벨에서 구현 필요

-- 예시: 모든 쿼리에 business_id 조건 자동 추가
SELECT * FROM members WHERE business_id = :currentUserBusinessId;
```

---

## 10. 마이그레이션 및 배포 전략

### 10.1 초기 데이터 설정
```sql
-- 기본 락커 생성 (1~200번)
INSERT INTO lockers (business_id, locker_number, status)
SELECT b.id, numbers.n, 'AVAILABLE'
FROM businesses b
CROSS JOIN (
    SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION ... UNION SELECT 200
) numbers;

-- 기본 상품 템플릿 생성
INSERT INTO products (business_id, name, type, price, duration_days, is_active)
VALUES 
    (?, '헬스 1개월', 'MEMBERSHIP', 60000, 30, true),
    (?, '헬스 3개월', 'MEMBERSHIP', 150000, 90, true),
    (?, '개인레슨 10회', 'PERSONAL_TRAINING', 500000, 90, true),
    (?, '락커 1개월', 'LOCKER', 30000, 30, true);
```

### 10.2 배포 순서
1. **Phase 1**: 핵심 엔터티 (User, Business, Member, Product)
2. **Phase 2**: 회원 관리 (MemberSubscription, Attendance, Payment)
3. **Phase 3**: 시설 관리 (Locker, DayPass, Schedule, Staff)
4. **Phase 4**: 커뮤니티 (Post, Comment, Like)
5. **Phase 5**: 통계/관리 (Expense, SMS)

---

## 11. 모니터링 및 로깅

### 11.1 슬로우 쿼리 모니터링
```sql
-- MySQL 슬로우 쿼리 로그 설정
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
SET GLOBAL log_queries_not_using_indexes = 'ON';
```

### 11.2 감사 로그
```sql
-- 중요 테이블에 대한 감사 로그 테이블
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    table_name VARCHAR(100) NOT NULL,
    operation_type ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    record_id BIGINT NOT NULL,
    old_values JSON,
    new_values JSON,
    user_id BIGINT,
    business_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_table_record (table_name, record_id),
    INDEX idx_business_created (business_id, created_at DESC)
);
```

---

이 DB 설계는 헬스장 관리 SaaS의 모든 비즈니스 요구사항을 충족하도록 설계되었으며, 확장성과 성능, 보안을 모두 고려한 종합적인 설계입니다. Spring Boot + JPA 환경에서 효율적으로 활용할 수 있도록 구성되어 있습니다.