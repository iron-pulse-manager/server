# 데이터베이스 스키마 설계

## 1. 계정(AUTH)
사용자 인증 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| AUTH_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 계정 ID |
| USERNAME | VARCHAR(50) | UNIQUE | 로그인 ID |
| PASSWORD | VARCHAR(255) | | 로그인 PW (암호화) |
| KAKAO_ID | VARCHAR(255) | | 카카오톡 로그인 ID |
| APPLE_ID | VARCHAR(255) | | 애플 로그인 ID |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 2. 유저(USER)
사용자 기본 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| USER_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 유저 ID |
| NAME | VARCHAR(100) | NOT NULL | 이름 |
| BIRTHDAY | DATE | | 생년월일 |
| GENDER | VARCHAR(10) | | 성별 |
| STATUS | VARCHAR(20) | NOT NULL | 회원 상태 (활성, 비활성) |
| PHONE_NUMBER | VARCHAR(20) | | 연락처 |
| CI | VARCHAR(255) | | CI값 |
| PHOTO_URL | VARCHAR(500) | | 프로필 사진 URL |
| ADDRESS | VARCHAR(500) | | 자택주소 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 3. 사업장(BUSINESS)
체육시설업 사업장 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| BUSINESS_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 사업장 ID |
| USER_ID | BIGINT | FK, NOT NULL | 사업주 유저 ID |
| BUSINESS_NAME | VARCHAR(255) | NOT NULL | 사업장명 |
| BUSINESS_NUMBER | VARCHAR(20) | UNIQUE, NOT NULL | 사업자 번호 |
| STATUS | VARCHAR(20) | NOT NULL | 사업장 상태 (활성, 비활성, 대기, 승인거절) |
| PHONE | VARCHAR(20) | | 사업장 전화번호 |
| ADDRESS | VARCHAR(500) | | 사업장 주소 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 4. 사업장 소속 직원(BUSINESS_EMPLOYEE)
특정 사업장에 소속된 직원 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| BUSINESS_EMPLOYEE_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 사업장 소속 직원 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| USER_ID | BIGINT | FK, NOT NULL | 직원 ID |
| STATUS | VARCHAR(20) | NOT NULL | 상태 (승인대기, 승인완료, 거절, 정상, 휴직, 퇴사) |
| POSITION | VARCHAR(50) | | 직책 |
| WORKING_START_TIME | TIME | | 근무 시작 시간 |
| WORKING_END_TIME | TIME | | 근무 종료 시간 |
| BANK_NAME | VARCHAR(50) | | 주거래 은행명 |
| ACCOUNT_NUMBER | VARCHAR(50) | | 주거래 계좌번호 |
| MEMO | TEXT | | 사장님 메모 |
| JOIN_DATE | DATE | | 입사일 |
| RESIGN_DATE | DATE | | 퇴사일 |
| APPROVED_AT | DATETIME | | 승인일자 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 5. 사업장 소속 회원(BUSINESS_MEMBER)
특정 사업장에 등록된 회원 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| BUSINESS_MEMBER_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 사업장 소속 회원 ID |
| MEMBER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| EMPLOYEE_ID | BIGINT | FK | 담당 직원 ID |
| STATUS | VARCHAR(20) | NOT NULL | 회원 상태 (활성, 만료임박, 만료, 정지) |
| SMS_YN | CHAR(1) | DEFAULT 'Y' | 문자수신동의 여부 |
| MEMO | TEXT | | 메모 |
| JOIN_DATE | DATE | NOT NULL | 가입일자 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 6. 상품(PRODUCT)
사업장에서 판매하는 상품 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| PRODUCT_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 상품 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| PRODUCT_TYPE | VARCHAR(20) | NOT NULL | 상품 유형 (회원권, 개인레슨권, 락커, 기타) |
| NAME | VARCHAR(255) | NOT NULL | 상품명 |
| PRICE | DECIMAL(10,2) | NOT NULL | 상품가격 |
| STATUS | VARCHAR(20) | NOT NULL | 상태 (활성, 비활성) |
| DESCRIPTION | TEXT | | 상품 설명 |
| VALID_DAY | INT | | 이용 가능 일수 |
| USAGE_CNT | INT | | 사용 가능 횟수 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 7. 락커(LOCKER)
사업장의 락커 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| LOCKER_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 락커 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| LOCKER_NUMBER | INT | NOT NULL | 락커 번호 |
| STATUS | VARCHAR(20) | NOT NULL | 상태 (이용가능, 이용중, 만료) |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 8. 결제(PAYMENT)
회원의 상품 결제 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| PAYMENT_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 결제 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| MEMBER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| PRODUCT_ID | BIGINT | FK, NOT NULL | 상품 ID |
| CONSULTANT_ID | BIGINT | FK | 상품 담당자 ID |
| TRAINER_ID | BIGINT | FK | 담당 강사 ID |
| PRODUCT_PRICE | DECIMAL(10,2) | NOT NULL | 상품 금액 |
| ACTUAL_PRICE | DECIMAL(10,2) | NOT NULL | 실제 결제 금액 |
| CONSULTANT_EMPLOYEE_COMMISSION | DECIMAL(10,2) | DEFAULT 0 | 상품 담당자 실적 |
| OUTSTANDING_AMOUNT | DECIMAL(10,2) | DEFAULT 0 | 현재 미수금 |
| INITIAL_OUTSTANDING_AMOUNT | DECIMAL(10,2) | DEFAULT 0 | 최초 결제 당시 미수금 |
| PAYMENT_METHOD | VARCHAR(20) | NOT NULL | 결제 방법 (카드, 현금, 계좌이체) |
| PAYMENT_DATE | DATETIME | NOT NULL | 결제 일시 |
| PURCHASE_PURPOSE | VARCHAR(255) | | 구매 목적 |
| MEMO | TEXT | | 메모 |
| STATUS | VARCHAR(20) | NOT NULL | 결제 상태 (대기, 완료, 취소) |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 9. 미수금 결제 이력(OUTSTANDING_PAYMENT)
미수금 결제 내역을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| OUTSTANDING_PAYMENT_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 미수금 결제 ID |
| PAYMENT_ID | BIGINT | FK, NOT NULL | 원본 결제 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| USER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| PAID_AMOUNT | DECIMAL(10,2) | NOT NULL | 미수금 결제 금액 |
| PAYMENT_METHOD | VARCHAR(20) | NOT NULL | 결제 수단 |
| PAYMENT_DATE | DATETIME | NOT NULL | 결제 일시 |
| MEMO | TEXT | | 메모 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 10. 이용권(MEMBERSHIP)
회원의 이용권 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| MEMBERSHIP_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 이용권 ID |
| PAYMENT_ID | BIGINT | FK, NOT NULL | 결제 ID |
| USER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| TYPE | VARCHAR(20) | NOT NULL | 이용권 유형 (회원권, PT, 필라테스) |
| LOCKER_ID | BIGINT | FK | 락커 번호 |
| PRODUCT_ID | BIGINT | FK, NOT NULL | 상품 ID |
| SERVICE_START_DATE | DATE | NOT NULL | 서비스 시작일 |
| SERVICE_END_DATE | DATE | NOT NULL | 서비스 종료일 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 11. 일일권(DAY_PASS)
일일권 이용 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| DAY_PASS_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 일일권 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| CUSTOMER_NAME | VARCHAR(100) | NOT NULL | 이용자 이름 |
| CUSTOMER_PHONE | VARCHAR(20) | | 이용자 연락처 |
| AMOUNT | DECIMAL(10,2) | NOT NULL | 결제 금액 |
| PAYMENT_METHOD | VARCHAR(20) | NOT NULL | 결제 수단 |
| VISIT_DATE | DATE | NOT NULL | 방문일자 |
| VISIT_TIME | TIME | | 방문시간 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 12. 출석(ATTENDANCE)
회원의 출석 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| ATTENDANCE_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 출석 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| USER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| ATTENDANCE_DATE | DATE | NOT NULL | 출석일자 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 13. 지출(EXPENSE)
사업장의 지출 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| EXPENSE_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 지출 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| EXPENSE_TYPE | VARCHAR(50) | NOT NULL | 지출 유형 (인건비, 임대료, 수도/경비, 기타경비) |
| TITLE | VARCHAR(255) | NOT NULL | 지출 제목 |
| AMOUNT | DECIMAL(10,2) | NOT NULL | 지출금액 |
| DESCRIPTION | TEXT | | 지출 상세내용 |
| EXPENSE_DATE | DATE | NOT NULL | 지출일자 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 14. 레슨 일정(LESSON_SCHEDULE)
레슨 일정 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| LESSON_SCHEDULE_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 레슨 일정 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| USER_ID | BIGINT | FK, NOT NULL | 강사 ID |
| LESSON_TYPE | VARCHAR(50) | NOT NULL | 레슨 유형 |
| STATUS | VARCHAR(20) | NOT NULL | 스케줄 상태 (진행예정, 진행중, 완료, 취소) |
| TITLE | VARCHAR(255) | NOT NULL | 일정 제목 |
| DESCRIPTION | TEXT | | 일정 메모 |
| SCHEDULE_START_DATE | DATETIME | NOT NULL | 레슨 시작일시 |
| SCHEDULE_END_DATE | DATETIME | NOT NULL | 레슨 종료일시 |
| DURATION_MINUTE | INT | NOT NULL | 소요시간 (분) |
| MAX_PARTICIPANT | INT | DEFAULT 1 | 최대 참가자 수 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 15. 레슨 일정 참여 회원(LESSON_PARTICIPANT)
레슨 참여 회원 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| LESSON_PARTICIPANT_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 레슨 참여 회원 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| LESSON_SCHEDULE_ID | BIGINT | FK, NOT NULL | 레슨 일정 ID |
| USER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| PAYMENT_ID | BIGINT | FK, NOT NULL | 레슨권 ID |
| PARTICIPANT_STATUS | VARCHAR(20) | NOT NULL | 참가 상태 (승인대기, 승인완료, 출석, 미출석) |
| REGISTERED_DATE | DATETIME | NOT NULL | 레슨 신청일시 |
| APPROVED_DATE | DATETIME | | 강사 승인시간 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 16. 회원권 정지 이력(MEMBERSHIP_SUSPENSION)
회원권 정지 이력을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| MEMBERSHIP_SUSPENSION_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 회원권 정지 이력 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| MEMBER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| APPROVE_EMPLOYEE_ID | BIGINT | FK, NOT NULL | 승인직원 ID |
| REASON | TEXT | NOT NULL | 정지사유 |
| START_DATE | DATE | NOT NULL | 정지 시작일 |
| END_DATE | DATE | NOT NULL | 정지 종료일 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 17. 커미션 정책(COMMISSION_POLICY)
직원 실적 커미션 정책을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| COMMISSION_POLICY_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 커미션 정책 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| MIN_AMOUNT | DECIMAL(10,2) | NOT NULL | 최소 매출액 |
| MAX_AMOUNT | DECIMAL(10,2) | | 최대 매출액 |
| COMMISSION_RATE | DECIMAL(5,2) | NOT NULL | 커미션 비율 (%) |
| USE_YN | CHAR(1) | DEFAULT 'Y' | 사용 여부 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 18. 직원 월별 실적(EMPLOYEE_MONTHLY_PERFORMANCE)
직원의 월별 실적 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| EMPLOYEE_COMMISSION_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 직원 월별 실적 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| USER_ID | BIGINT | FK, NOT NULL | 직원 ID |
| YEAR_MONTH | VARCHAR(7) | NOT NULL | 년월 (YYYY-MM) |
| TOTAL_AMOUNT | DECIMAL(10,2) | DEFAULT 0 | 총 매출 |
| COMMISSION_AMOUNT | DECIMAL(10,2) | DEFAULT 0 | 실적 금액 |
| NEW_MEMBER_CNT | INT | DEFAULT 0 | 신규회원 유치 수 |
| RENEWAL_MEMBER_CNT | INT | DEFAULT 0 | 재등록 수 |
| MANAGED_MEMBER_CNT | INT | DEFAULT 0 | 담당회원 수 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 19. 직원 실적 계산 내역(COMMISSION_CALCULATION)
직원 실적 계산 세부 내역을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| COMMISSION_CALCULATION_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 계산 내역 ID |
| PERFORMANCE_ID | BIGINT | FK, NOT NULL | 월별 실적 ID |
| PAYMENT_ID | BIGINT | FK, NOT NULL | 결제 ID |
| SALES_AMOUNT | DECIMAL(10,2) | NOT NULL | 해당 결제의 매출액 |
| COMMISSION_RATE | DECIMAL(5,2) | NOT NULL | 커미션 비율 |
| COMMISSION_AMOUNT | DECIMAL(10,2) | NOT NULL | 직원의 커미션 금액 |
| COMMISSION_POLICY_ID | BIGINT | FK, NOT NULL | 커미션 정책 ID |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 20. 문자 발송 이력(SMS_HIST)
문자 발송 이력을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| SMS_HIST_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 문자발송이력 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| MESSAGE_TYPE | VARCHAR(20) | NOT NULL | 메시지 유형 (단건문자, 단체문자) |
| TITLE | VARCHAR(255) | | 문자 제목 |
| CONTENT | TEXT | NOT NULL | 메시지 내용 |
| RECIPIENT_CNT | INT | NOT NULL | 수신자 수 |
| SENT_AT | DATETIME | NOT NULL | 발송시간 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 21. 문자 수신자(SMS_RECIPIENT)
문자 수신자 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| SMS_RECIPIENT_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 문자 수신자 ID |
| SMS_HIST_ID | BIGINT | FK, NOT NULL | 문자 발송 이력 ID |
| USER_ID | BIGINT | FK | 수신 회원 ID |
| PHONE_NUMBER | VARCHAR(20) | NOT NULL | 회원 핸드폰 번호 |
| SEND_STATUS | VARCHAR(20) | NOT NULL | 문자 전송 상태 (발송완료, 실패) |
| SENT_AT | DATETIME | | 발송 시간 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 22. 운동일지(WORKOUT_SESSION)
회원의 운동일지 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| WORKOUT_SESSION_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 운동일지 ID |
| LESSON_SCHEDULE_ID | BIGINT | FK, NOT NULL | 스케줄 ID |
| USER_ID | BIGINT | FK, NOT NULL | 회원 ID |
| MEMO | TEXT | | 세션 메모 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 23. 운동 종목 유형(EXERCISE_TYPE)
운동 종목 유형 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| EXERCISE_TYPE_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 운동 종목 유형 ID |
| EXERCISE_CATEGORY | VARCHAR(50) | NOT NULL | 운동 카테고리 |
| NAME | VARCHAR(255) | NOT NULL | 운동 이름 |
| DESCRIPTION | TEXT | | 운동 설명 |
| USE_YN | CHAR(1) | DEFAULT 'Y' | 사용여부 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 24. 운동 종목(WORKOUT_EXERCISE)
운동일지의 세부 운동 종목을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| WORKOUT_EXERCISE_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 운동 종목 ID |
| WORKOUT_LOG_ID | BIGINT | FK, NOT NULL | 운동일지 ID |
| EXERCISE_TYPE_ID | BIGINT | FK | 운동 종목 유형 ID |
| CUSTOM_EXERCISE_NAME | VARCHAR(255) | | 커스텀 운동 종목명 |
| EXERCISE_ORDER | INT | NOT NULL | 운동 순서 |
| MEMO | TEXT | | 종목 별 트레이너 메모 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 25. 운동 종목 세트 기록(EXERCISE_SETS)
운동 세트별 상세 기록을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| EXERCISE_SETS_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 세트 ID |
| WORKOUT_EXERCISE_ID | BIGINT | FK, NOT NULL | 운동 종목 기록 ID |
| SET_ORDER | INT | NOT NULL | 세트 순서 |
| WEIGHT | DECIMAL(5,2) | | 운동 무게 (KG) |
| REPS | INT | | 수행 횟수 |
| DURATION_SECOND | INT | | 수행 시간 (초) |
| DURATION_MINUTE | INT | | 수행 시간 (분) |
| REST_MINUTE | INT | | 세트 간 쉬는시간 (분) |
| REST_SECOND | INT | | 세트 간 쉬는시간 (초) |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 26. 운동 종목 첨부파일(EXERCISE_ATTACH)
운동 종목의 첨부파일을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| EXERCISE_ATTACH_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 운동 종목 첨부파일 ID |
| WORKOUT_EXERCISE_ID | BIGINT | FK, NOT NULL | 운동 종목 ID |
| ATTACH_URL | VARCHAR(500) | NOT NULL | 첨부파일 URL |
| ATTACH_TYPE | VARCHAR(20) | NOT NULL | 첨부파일 유형 (사진, 동영상) |
| ATTACH_ORDER | INT | NOT NULL | 첨부파일 순서 |
| FILE_SIZE | BIGINT | | 파일 사이즈 (byte) |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 27. 운동 일지 댓글(WORKOUT_COMMENT)
운동일지 댓글 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| WORKOUT_COMMENT_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 운동일지 댓글 ID |
| WORKOUT_SESSION_ID | BIGINT | FK, NOT NULL | 운동일지 ID |
| WRITER_ID | BIGINT | NOT NULL | 작성자 ID |
| WRITER_TYPE | VARCHAR(20) | NOT NULL | 작성자 유형 (강사, 회원) |
| CONTENT | TEXT | NOT NULL | 댓글 내용 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 28. 운동 일지 대댓글(WORKOUT_REPLY)
운동일지 대댓글 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| WORKOUT_REPLY_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 대댓글 ID |
| WORKOUT_COMMENT_ID | BIGINT | FK, NOT NULL | 운동일지 댓글 ID |
| WRITER_ID | BIGINT | NOT NULL | 작성자 ID |
| WRITER_TYPE | VARCHAR(20) | NOT NULL | 작성자 유형 (강사, 회원) |
| CONTENT | TEXT | NOT NULL | 댓글 내용 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 29. 알림 내역(ALIM_HIST)
시스템 알림 내역을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| ALIM_HIST_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 알림 내역 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| DEVICE_TYPE | VARCHAR(20) | NOT NULL | 디바이스 유형 (WEB, APP) |
| ALIM_TYPE | VARCHAR(50) | NOT NULL | 알림 유형 |
| SENDER_ID | BIGINT | NOT NULL | 발신자 ID |
| SENDER_TYPE | VARCHAR(20) | NOT NULL | 발신자 유형 (사장, 직원, 회원) |
| RECIPIENT_ID | BIGINT | NOT NULL | 수신자 ID |
| RECIPIENT_TYPE | VARCHAR(20) | NOT NULL | 수신자 유형 (사장, 직원, 회원) |
| SEND_DATE | DATETIME | NOT NULL | 발송일시 |
| READ_DATE | DATETIME | | 수신일시 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 30. 유저 푸시 설정(USER_PUSH_SET)
사용자별 푸시 설정을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| USER_PUSH_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 유저 푸시 설정 ID |
| BUSINESS_ID | BIGINT | FK, NOT NULL | 사업장 ID |
| PUSH_TYPE | VARCHAR(20) | NOT NULL | 푸시 유형 (WORKOUT, DIET, LESSON) |
| USER_ID | BIGINT | NOT NULL | 사용자 ID |
| USER_TYPE | VARCHAR(20) | NOT NULL | 사용자 유형 (OWNER, EMPLOYEE, MEMBER) |
| ACTIVE_YN | CHAR(1) | DEFAULT 'Y' | 푸시 활성화 여부 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 31. 디바이스 정보(DEVICE)
직원, 회원의 로그인 디바이스 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 디바이스 ID |
| EMPLOYEE_ID | BIGINT | FK | 직원 ID |
| MEMBER_ID | BIGINT | FK | 회원 ID |
| DEVICE_TYPE | VARCHAR(20) | NOT NULL | 디바이스 종류 (iOS, AOS) |
| DEVICE_ID | VARCHAR(255) | NOT NULL | 디바이스 ID |
| PUSH_ID | VARCHAR(255) | | 푸시 ID |
| APP_VERSION | VARCHAR(20) | | 앱버전 |
| MODEL_NAME | VARCHAR(100) | | 핸드폰 기종 |
| OS_VERSION | VARCHAR(20) | | 핸드폰 OS 버전 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |

---

## 32. 설정(OPTION)
시스템 설정을 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| OPTION_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 설정 ID |
| TITLE | VARCHAR(255) | NOT NULL | 설정명 |
| DESCRIPTION | TEXT | | 설명 |
| VALUE | VARCHAR(500) | NOT NULL | 설정값 |
| USE_YN | CHAR(1) | DEFAULT 'Y' | 사용 여부 |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 33. 관리자(ADMIN)
시스템 관리자 정보를 관리하는 테이블

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| ADMIN_ID | BIGINT | PK, NOT NULL, AUTO_INCREMENT | 관리자 ID |
| USERNAME | VARCHAR(50) | UNIQUE, NOT NULL | 로그인 ID |
| PASSWORD | VARCHAR(255) | NOT NULL | 비밀번호 (암호화) |
| NAME | VARCHAR(100) | NOT NULL | 이름 |
| STATUS | VARCHAR(20) | NOT NULL | 상태 (활성, 비활성) |
| AUTH_ID | BIGINT | FK, NOT NULL | 권한 ID |
| CREATED_AT | DATETIME | NOT NULL | 생성일자 |
| CREATED_BY | BIGINT | NOT NULL | 생성자 ID |
| UPDATED_AT | DATETIME | | 수정일자 |
| UPDATED_BY | BIGINT | | 수정자 ID |
| DELETED_AT | DATETIME | | 삭제일자 |
| DELETED_BY | BIGINT | | 삭제자 ID |


---

## 주요 관계 정의

### 1. 인증 및 사용자 관련
- AUTH ↔ USER (1:1 또는 1:N)
- USER ↔ BUSINESS (1:N) - 사업주로서
- USER ↔ BUSINESS_EMPLOYEE (1:N) - 직원으로서
- USER ↔ BUSINESS_MEMBER (1:N) - 회원으로서

### 2. 사업장 관련
- BUSINESS ↔ BUSINESS_EMPLOYEE (1:N)
- BUSINESS ↔ BUSINESS_MEMBER (1:N)
- BUSINESS ↔ PRODUCT (1:N)
- BUSINESS ↔ LOCKER (1:N)

### 3. 결제 및 이용권 관련
- PAYMENT ↔ MEMBERSHIP (1:1)
- MEMBERSHIP ↔ LOCKER (1:1) - 락커 이용권인 경우
- PRODUCT ↔ PAYMENT (1:N)
- PAYMENT ↔ OUTSTANDING_PAYMENT (1:N)

### 4. 레슨 관련
- LESSON_SCHEDULE ↔ LESSON_PARTICIPANT (1:N)
- LESSON_SCHEDULE ↔ WORKOUT_SESSION (1:N)
- WORKOUT_SESSION ↔ WORKOUT_EXERCISE (1:N)
- WORKOUT_EXERCISE ↔ EXERCISE_SETS (1:N)

### 5. 커뮤니케이션 관련
- SMS_HIST ↔ SMS_RECIPIENT (1:N)
- WORKOUT_SESSION ↔ WORKOUT_COMMENT (1:N)
- WORKOUT_COMMENT ↔ WORKOUT_REPLY (1:N)

---

## 2차 개발 예정 테이블
- 커뮤니티 관련 테이블
- 사업장 일정 테이블  
- 식단 기록 관련 테이블
- 체중 기록 관련 테이블
