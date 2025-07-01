# 테이블 설계
## 계정 (AUTH)

- AUTH_ID : 계정 ID (PK)
- USERNAME : 로그인 ID
- PASSWORD : 로그인 PW
- KAKAO_ID : 카카오톡 로그인 ID
- APPLE_ID : 애플 로그인 ID
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 유저 (USER)

- USER_ID : 유저 ID (PK)
- NAME : 이름
- BIRTHDAY : 생년월일
- GENDER : 성별
- STATUS : 회원 상태 (활성, 비활성)
- PHONE_NUMBER : 연락처
- CI : CI값
- PHOTO_URL : 프로필 사진
- ADDRESS : 자택주소
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 사업장 (BUSINESS)

- BUSINESS_ID : 사업장 ID (PK)
- USER_ID : 사업주 유저 ID (FK)
- BUSINESS_NAME : 사업장명
- BUSINESS_NUMBER : 사업자 번호
- STATUS : 사업장 상태 (활성, 비활성, 대기, 승인거절)
- PHONE : 사업장 전화번호
- ADDRESS : 사업장 주소
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 사업장 소속 직원 (BUSINESS_EMPLOYEE)

- BUSINESS_EMPLOYEE_ID : 사업장 소속 직원 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- USER_ID : 직원 ID(FK)
- STATUS : 상태
- POSITION : 직책
- WORKING_START_TIME : 근무 시작 시간
- WORKING_END_TIME : 근무 시작 시간
- BANK_NAME : 주거래 은행명
- ACCOUNT_NUMBER : 주거래 계좌주소
- MEMO : 사장님 메모
- JOIN_DATE : 입사 일시
- RESIGN_DATE : 퇴사 일시
- APPROVED_AT : 승인일자
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 사업장 소속 회원 (BUSINESS_MEMBER)

- BUSINESS_MEMBER_ID : 회원의 사업장 내의 ID
- MEMBER_ID : 회원 ID (FK)
- BUSINESS_ID : 사업장 ID (FK)
- EMPLOYEE_ID : 담당 직원 ID (FK)
- STATUS : 회원 상태
- SMS_YN : 문자수신동의 여부
- MEMO : 메모
- JOIN_DATE : 가입일자
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

---

## 상품 (PRODUCT)

- PRODUCT_ID : 상품 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- PRODUCT_TYPE : 상품 유형 (회원권, 개인 레슨권, 락커, 기타)
- NAME : 상품명
- PRICE : 상품가격
- STATUS : 상태(활성, 비활성)
- DESCRIPTION : 상품 설명
- VALID_DAY : 이용 가능 일수
- USAGE_CNT : 사용 가능 횟수
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 락커 (LOCKER)

- LOCKER_ID : 락커 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- LOCKER_NUMBER : 락커 번호
- STATUS : 상태 (이용가능, 이용중, 만료)
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 결제 (PAYMENT)

- PAYMENT_ID : 결재 ID
- BUSINESS_ID : 사업장 ID
- MEMBER_ID : 회원 ID
- PRODUCT_ID : 상품 ID
- CONSULTANT_ID : 상품 담당자 ID
- TRAINER_ID : 담당 강사 ID
- PRODUCT_PRICE : 상품 금액
- ACTUAL_PRICE : 실제 결제 금액
- CONSULTANT_EMPLOYEE_COMMISSION : 상품 담당자 실적
- OUTSTANDING_AMOUNT : 현재 미수금
- INITIAL_OUTSTANDING_AMOUNT : 최초 결제 당시 미수금
- PAYMENT_METHOD : 결제 방법
- PAYMENT_DATE : 결제 일시
- PURCHASE_PURPOSE : 구매 목적
- MEMO : 메모
- PAYMENT_DATE : 결제일시
- STATUS : 결제 상태 (대기, 완료, 취소)
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 미수금 결제 이력 (OUTSTANDING_PAYMENT)

- OUTSTANDING_PAYMENT_ID : 미수금 결제 ID(PK)
- PAYMENT_ID : 원본 결제 ID (FK)
- BUSINESS_ID : 사업장 ID(FK)
- USER_ID : 회원 ID (FK)
- PAID_AMOUNT : 미수금 결제 금액
- PAYMENT_METHOD : 결제 수단
- PAYMENT_DATE : 결제 일시
- MEMO : 메모
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 이용권 (MEMBERSHIP)

- MEMBERSHIP_ID : 이용권 ID
- PAYMENT_ID : 결제 ID (FK)
- USER_ID : 회원 ID (FK)
- TYPE : 이용권 유형 (회원권, PT, 필라테스 ..)
- LOCKER_ID : 락커 번호 (FK)
- PRODUCT_ID :  상품 ID (FK)
- SERVICE_START_DATE : 서비스 시작일
- SERVICE_END_DATE : 서비스 종료일
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## ~~락커 배정 (LOCKER_ASSIGN)~~

- ~~LOCKER_ASSIGN_ID : 락커 배정 ID (PK)~~
- ~~LOCKER_ID : 락커 ID (FK)~~
- ~~MEMBER_ID : 회원 ID (FK)~~
- ~~PAYMENT_ID : 락커 결제 ID (FK)~~
- ~~START_DATE : 락커 사용 시작일~~
- ~~END_DATE : 락커 사용 종료일~~
- ~~STATUS : 배정 상태 (사용중, 만료, 일시중지)~~
- ~~MEMO : 메모~~
- ~~CREATED_AT : 생성일자~~
- ~~CREATED_BY : 생성자 ID~~
- ~~UPDATED_AT : 수정일자~~
- ~~UPDATED_BY : 수정자 ID~~
- ~~DELETED_AT : 삭제일자~~
- ~~DELETED_BY : 삭제자 ID~~

## 일일권 (DAY_PASS)

- DAY_PASS_ID : 일일권 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- CUSTOMER_NAME : 이용자 이름
- CUSTOMER_PHONE : 이용자 연락처
- AMOUNT : 결제 금액
- PAYMENT_METHOD : 결제 수단
- VISIT_DATE : 방문일자
- VISIT_TIME : 방문시간
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 출석 (ATTENDANCE)

- ATTENDANCE_ID : 출석 ID
- BUSINESS_ID : 사업장 ID (FK)
- USER_ID : 회원 ID (FK)
- ATTENDANCE_DATE : 출석일자
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 지출 (EXPENSE)

- EXPENSE_ID : 지출 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- EXPENSE_TYPE : 지출 유형
- TITLE : 지출 제목
- AMOUNT : 지출금액
- DESCRIPTION : 지출 상세내용
- EXPENSE_DATE : 지출일자
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 레슨 일정 (LESSON_SCHEDULE)

- LESSON_SCHEDULE_ID : 레슨 일정 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- USER_ID : 강사 ID (FK)
- LESSON_TYPE : 레슨 유형 (1:1 PT, 그룹 PT, 온라인 PT, 필라테스 개인레슨, 필라테스 그룹레슨, ..)
- STATUS : 스케줄 상태 (진행예정,진행중, 완료, 취소)
- TITLE : 일정 제목
- DESCRIPTION : 일정 메모
- SCHEDULE_START_DATE : 레슨 시작일자
- SCHEDULE_END_DATE : 레슨 종료일자
- DURATION_MINUTE : 소요시간 (분)
- MAX_PARTICIPANT : 최대 참가자 수
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 레슨 일정 참여 회원 (LESSON_PARTICIPANT)

- LESSON_PARTICIPANT_ID : 레슨 일정 참여 회원 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- LESSON_SCHEDULE_ID : 레슨 일정 ID (FK)
- USER_ID : 회원 ID (FK)
- PAYMENT_ID : 레슨권 ID (FK)
- PARTICIPANT_STATUS : 참가 상태 (승인대기, 승인완료, 출석, 미출석)
- REGISTERED_DATE: 레슨 신청일시
- APPROVED_DATE : 강사 승인시간
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 회원권 정지 이력 (MEMBERSHIP_SUSPENSION)

- MEMBERSHIP_SUSPENSION_ID : 회원권 정지 이력 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- MEMBER_ID : 회원 ID (FK)
- APPROVE_EMPLOYEE_ID : 승인직원 ID (FK)
- REASON : 정지사유
- START_DATE : 정지 시작일
- END_DATE : 정지 종료일
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 커미션 정책 (COMMISSION_POLICY)

- COMMISSION_POLICY : 직원 실적 정책 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- MIN_AMOUNT : 최소 매출액
- MAX_AMOUNT : 최대 매출액
- COMMISION_RATE : 커미션 비율 (%)
- USE_YN : 사용 여부
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 직원 월별 실적 (EMPLOY_MONTHLY_PERFORMANCE)

- EMPLOY_COMMISSION_ID : 직원 월별 실적 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- USER_ID : 직원 ID (FK)
- YEAR_MONTH : 년월
- TOTAL_AMOUNT : 총 매출
- COMMISSION_AMOUNT : 실적 금액
- NEW_MEMBER_CNT : 신규회원 유치 수
- RENEWAL_MEMBER_CNT : 재등록 수
- MANAGED_MEMBER_CNT : 담당회원 수
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 직원 실적 계산 내역 (COMMISSION_CALCULATION)

- COMMISSION_CALCULATION_ID : 계산 내역 ID (PK)
- PERFORMANCE_ID : 월별 실적 ID (FK)
- PAYMENT_ID : 결제 ID (FK)
- SALES_AMOUNT : 해당 결제의 매출액
- COMMISSION_RATE : 커미션 비율
- COMMISSION_AMOUNT : 직원의 커미션 금액
- COMMISSION_POLICY_ID : 커미션 정책 ID (FK)
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 문자 발송 이력 (SMS_HIST)

- SMS_HIST_ID : 문자발송이력 (PK)
- BUSINESS_ID : 사업장 ID (FK)
- MESSAGE_TYPE : 메시지 유형 (단건 문자, 단체 문자)
- TITLE : 문자 제목
- CONTENT : 메시지 내용
- RECIPIENT_CNT : 수신자 수
- SENT_AT : 발송시간
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 문자 수신자 (SMS_RECIPIENT)

- SMS_RECIPIENT_ID : 문자 수신자 ID (PK)
- SMS_HIST_ID : 문자 발송 이력 ID (PK)
- USER_ID : 수신 회원 ID
- PHONE_NUMBER : 회원 핸드폰 번호
- SEND_STATUS : 문자 전송 상태 (발송완료, 실패)
- SENT_AT : 발송 시간
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동일지 (WORKOUT_SESSION)

- WORKOUT_SESSION_ID : 운동일지 ID
- LESSON_SCHEDULE_ID : 스케줄 ID (FK)
- USER_ID : 회원 ID (FK)
- MEMO : 세션 메모
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동 종목 유형 (EXERCISE_TYPE)

- EXERCISE_TYPE_ID : 운동 종목 유형 (PK)
- EXERCISE_CATEGORY : 운동 카테고리 (ex) 가슴,등,어깨,하체,복근,유산소..)
- NAME : 운동 이름
- DESCRIPTION : 운동 설명
- USE_YN : 사용여부
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동 종목 (WORKOUT_EXERCISE)

- WORKOUT_EXERCISE_ID : 운동 종목 ID (PK)
- WORKOUT_LOG_ID : 운동일지 ID (FK)
- EXERCISE_TYPE_ID : 운동 종목 유형 ID (FK)
- CUSTOM_EXERCISE_NAME : 커스텀 운동 종목명
- EXERCISE_ORDER : 운동 순서
- MEMO : 종목 별 트레이너 메모
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동 종목 세트 기록 (EXERCISE_SETS)

- EXERCISE_SETS_ID : 세트 ID (PK)
- WORKOUT_EXERCISE_ID : 운동 종목 기록 ID (FK)
- SET_ORDER : 세트 순서
- WEIGHT : 운동 무게 (KG)
- REPS : 수행 횟수
- DURATION_SECOND : 수행 시간 (초)
- DURATION_MINUTE : 수행 시간 (분)
- REST_MINUTE : 세트 간 쉬는시간 (분)
- REST_SECOND : 세트 간 쉬는시간 (초)
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동 종목 첨부파일 (EXERCISE_ATTACH)

- EXERCISE_ATTACH_ID : 운동 종목 첨부파일 ID (PK)
- WORKOUT_EXERCISE_ID : 운동 종목 ID (FK)
- ATTACH_URL : 첨부파일 URL
- ATTACH_TYPE : 첨부파일 유형 (사진,동영상)
- ATTACH_ORDER : 첨부파일 순서
- FILE_SIZE : 파일 사이즈 (byte)
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동 일지 댓글 (WORKOUT_COMMENT)

- WORKOUT_COMMENT_ID : 운동일지 댓글 ID (PK)
- WORKOUT_SESSION_ID : 운동일지 ID (FK)
- WRITER_ID : 작성자 ID
- WRITER_TYPE : 작성자 유형 (강사, 회원)
- CONTENT : 댓글 내용
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 운동 일지 대댓글 (WORKOUT_REPLY)

- WORKOUT_REPLY_ID : 대댓글 ID (PK)
- WORKOUT_COMMENT_ID : 운동일지 댓글 ID (FK)
- WRITER_ID : 작성자 ID
- WRITER_TYPE : 작성자 유형 (강사, 회원)
- CONTENT : 댓글 내용
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 알림 내역 (ALIM_HIST)

- ALIM_HIST_ID : 알림 내역 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- DEVICE_TYPE : 디바이스 유형 (ex) WEB, APP)
- ALIM_TYPE : 알림 유형 (레슨 신청, APP 운동 댓글, WEB 커뮤니티 댓글, WEB 일정 참여자 등록, ...)
- SENDER_ID : 발신자 ID
- SENDER_TYPE : 발신자 유형 (사장,직원,회원)
- RECIPIENT_ID : 수신자 ID
- RECIPIENT_TYPE : 수신자 유형 (사장,직원,회원)
- SEND_DATE : 발송일시
- READ_DATE : 수신일시
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 유저 푸시 설정 (USER_PUSH_SET)

- USER_PUSH_ID : 유저 푸시 설정 ID (PK)
- BUSINESS_ID : 사업장 ID (FK)
- PUSH_TYPE : 푸시 유형 (ex) WORKOUT, DIET, LESSON)
- USER_ID : 사용자 ID
- USER_TYPE : 사용자 유형 (OWNER, EMPLOYEE, MEMBER)
- ACTIVE_YN : 푸시 활성화 여부
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 직원, 회원 로그인 디바이스 정보 (DEVICE)

- ID : 디바이스 ID(PK)
- EMPLOYEE_ID : 직원 ID(FK)
- MEMBER_ID : 직원 ID(FK)
- DEVICE_TYPE : 디바이스 종류 (iOS, AOS)
- DEVICE_ID : 디바이스 ID
- PUSH_ID : 푸시 ID
- APP_VERSION : 앱버전
- MODEL_NAME : 핸드폰 기종
- OS_VERSION : 핸드폰 OS 종류
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 설정 (OPTION)

```
ex) 커미션 비율 초기화 주기 : 1달

```

- OPTION_ID : 설정 ID
- TITLE : 설정명
- DESCRIPTION : 설명
- VALUE : 설정값
- USE_YN : 사용 여부
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID

## 관리자 (ADMIN)

- ADMIN_ID : 관리자 ID (PK)
- USERNAME : 로그인 ID
- PASSWORD : 패스위드
- NAME : 이름
- STATUS: 상태
- AUTH_ID : 권한 ID (FK)
- CREATED_AT : 생성일자
- CREATED_BY : 생성자 ID
- UPDATED_AT : 수정일자
- UPDATED_BY : 수정자 ID
- DELETED_AT : 삭제일자
- DELETED_BY : 삭제자 ID
---

## 커뮤니티 관련 테이블 .. (2차)
## 사업장 일정 테이블 .. (2차)

## 식단 기록 관련 테이블 ..(2차)

## 체중 기록 관련 테이블 ..(2차)

## 레슨 예약 관련 테이블 .. (2차)