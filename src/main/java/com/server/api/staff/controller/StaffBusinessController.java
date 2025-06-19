package com.gymmanager.api.staff.controller;

import com.gymmanager.api.common.controller.BaseRestController;
import com.gymmanager.common.dto.ApiResponse;
import com.gymmanager.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 직원 앱용 사업장 관리 API
 * 
 * Flutter 직원 앱에서 사용하는 사업장 관련 REST API입니다.
 * 직원이 가입 가능한 사업장 조회 및 가입 신청 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/v1/staff/businesses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "직원 앱 - 사업장", description = "직원용 사업장 조회 및 가입 신청 API")
public class StaffBusinessController extends BaseRestController {

    /**
     * 가입 가능한 사업장 목록 조회 (인증 불필요)
     */
    @GetMapping("/available")
    @Operation(summary = "가입 가능한 사업장 목록", description = "직원이 가입 신청할 수 있는 사업장 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<Object>>> getAvailableBusinesses(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "지역 필터") @RequestParam(required = false) String region,
            @Parameter(description = "페이징 정보") Pageable pageable) {
        
        log.info("가입 가능한 사업장 목록 조회 - 키워드: {}, 지역: {}", keyword, region);
        
        // TODO: 실제 서비스 로직 구현
        List<Object> businesses = List.of(
                Map.of(
                        "id", 1L,
                        "name", "강남 피트니스",
                        "address", "서울시 강남구",
                        "phone", "02-1234-5678",
                        "businessType", "헬스장"
                ),
                Map.of(
                        "id", 2L,
                        "name", "홍대 필라테스",
                        "address", "서울시 마포구",
                        "phone", "02-9876-5432",
                        "businessType", "필라테스"
                )
        );
        
        PageResponse<Object> pageResponse = PageResponse.<Object>builder()
                .content(businesses)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(businesses.size())
                .totalPages(1)
                .first(true)
                .last(true)
                .build();
        
        return ResponseEntity.ok(success("가입 가능한 사업장 목록을 조회했습니다.", pageResponse));
    }

    /**
     * 특정 사업장 상세 정보 조회
     */
    @GetMapping("/{businessId}/detail")
    @Operation(summary = "사업장 상세 정보", description = "특정 사업장의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<Object>> getBusinessDetail(
            @Parameter(description = "사업장 ID") @PathVariable Long businessId) {
        
        log.info("사업장 상세 정보 조회 - 사업장: {}", businessId);
        
        // TODO: 실제 서비스 로직 구현
        Map<String, Object> businessDetail = Map.of(
                "id", businessId,
                "name", "강남 피트니스",
                "address", "서울시 강남구 테헤란로 123",
                "phone", "02-1234-5678",
                "businessType", "헬스장",
                "description", "최신 시설을 갖춘 프리미엄 헬스장입니다.",
                "facilities", List.of("헬스장", "개인레슨룸", "샤워실", "락커룸"),
                "workingHours", "06:00 - 24:00",
                "isActive", true
        );
        
        return ResponseEntity.ok(success("사업장 상세 정보를 조회했습니다.", businessDetail));
    }

    /**
     * 사업장 가입 신청
     */
    @PostMapping("/{businessId}/join-request")
    @Operation(summary = "사업장 가입 신청", description = "특정 사업장에 직원 가입을 신청합니다.")
    @PreAuthorize("hasRole('STAFF') and hasAuthority('STAFF_APP')")
    public ResponseEntity<ApiResponse<Object>> requestJoinBusiness(
            @Parameter(description = "사업장 ID") @PathVariable Long businessId,
            @Parameter(description = "가입 신청 정보") @RequestBody Map<String, Object> joinData) {
        
        Long staffId = getCurrentUserId();
        log.info("사업장 가입 신청 - 직원: {}, 사업장: {}, 데이터: {}", staffId, businessId, joinData);
        
        // TODO: 실제 서비스 로직 구현
        Map<String, Object> joinRequest = Map.of(
                "id", 1L,
                "businessId", businessId,
                "staffId", staffId,
                "status", "PENDING",
                "requestedAt", java.time.LocalDateTime.now(),
                "message", joinData.get("message")
        );
        
        return ResponseEntity.ok(success("사업장 가입 신청이 완료되었습니다.", joinRequest));
    }

    /**
     * 내 가입 신청 목록 조회
     */
    @GetMapping("/my-requests")
    @Operation(summary = "내 가입 신청 목록", description = "내가 신청한 사업장 가입 요청 목록을 조회합니다.")
    @PreAuthorize("hasRole('STAFF') and hasAuthority('STAFF_APP')")
    public ResponseEntity<ApiResponse<PageResponse<Object>>> getMyJoinRequests(
            @Parameter(description = "페이징 정보") Pageable pageable) {
        
        Long staffId = getCurrentUserId();
        log.info("내 가입 신청 목록 조회 - 직원: {}", staffId);
        
        // TODO: 실제 서비스 로직 구현
        List<Object> requests = List.of(
                Map.of(
                        "id", 1L,
                        "businessName", "강남 피트니스",
                        "status", "PENDING",
                        "requestedAt", "2024-01-01T10:00:00",
                        "message", "열정적으로 일하겠습니다."
                ),
                Map.of(
                        "id", 2L,
                        "businessName", "홍대 필라테스",
                        "status", "APPROVED",
                        "requestedAt", "2024-01-01T10:00:00",
                        "approvedAt", "2024-01-02T14:30:00"
                )
        );
        
        PageResponse<Object> pageResponse = PageResponse.<Object>builder()
                .content(requests)
                .pageNumber(0)
                .pageSize(10)
                .totalElements(requests.size())
                .totalPages(1)
                .first(true)
                .last(true)
                .build();
        
        return ResponseEntity.ok(success("내 가입 신청 목록을 조회했습니다.", pageResponse));
    }

    /**
     * 가입 신청 취소
     */
    @DeleteMapping("/requests/{requestId}")
    @Operation(summary = "가입 신청 취소", description = "대기 중인 가입 신청을 취소합니다.")
    @PreAuthorize("hasRole('STAFF') and hasAuthority('STAFF_APP')")
    public ResponseEntity<ApiResponse<Object>> cancelJoinRequest(
            @Parameter(description = "가입 신청 ID") @PathVariable Long requestId) {
        
        Long staffId = getCurrentUserId();
        log.info("가입 신청 취소 - 직원: {}, 신청: {}", staffId, requestId);
        
        // TODO: 실제 서비스 로직 구현
        
        return ResponseEntity.ok(success("가입 신청이 취소되었습니다."));
    }
}