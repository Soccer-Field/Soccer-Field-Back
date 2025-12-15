package com.community.back.domain.field.presentation;

import com.community.back.domain.field.application.FieldService;
import com.community.back.domain.field.presentation.dto.request.CreateFieldRequest;
import com.community.back.domain.field.presentation.dto.response.ApproveFieldResponse;
import com.community.back.domain.field.presentation.dto.response.CreateFieldResponse;
import com.community.back.domain.field.presentation.dto.response.FieldDetailResponse;
import com.community.back.domain.field.presentation.dto.response.FieldListResponse;
import com.community.back.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fields")
@RequiredArgsConstructor
@Tag(name = "Field", description = "축구장 관리 API")
public class FieldController {

    private final FieldService fieldService;

    @Operation(summary = "축구장 목록 조회", description = "등록된 축구장 목록을 조회합니다. 모든 축구장의 기본 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldListResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<FieldListResponse>> getAllFields() {
        log.info("GET /fields - 축구장 목록 조회");
        List<FieldListResponse> fields = fieldService.getAllFields();
        return ResponseEntity.ok(fields);
    }

    @Operation(summary = "승인 대기 중인 축구장 목록 조회", description = "관리자가 승인 대기 중인 축구장 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldListResponse.class))))
    })
    @GetMapping("/pending")
    public ResponseEntity<List<FieldListResponse>> getPendingFields() {
        log.info("GET /fields/pending - 승인 대기 중인 축구장 목록 조회");
        List<FieldListResponse> fields = fieldService.getPendingFields();
        return ResponseEntity.ok(fields);
    }

    @Operation(summary = "축구장 상세 조회", description = "특정 축구장의 상세 정보를 조회합니다. 잔디 상태, 추천 축구화 등 상세 정보를 포함합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FieldDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "축구장을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FieldDetailResponse> getFieldById(
            @Parameter(description = "축구장 ID", required = true)
            @PathVariable Long id) {
        log.info("GET /fields/{} - 축구장 상세 조회", id);
        FieldDetailResponse field = fieldService.getFieldById(id);
        return ResponseEntity.ok(field);
    }

    @Operation(summary = "축구장 검색", description = "키워드로 축구장을 검색합니다. 이름과 주소에서 키워드를 찾아 일치하는 축구장 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldListResponse.class))))
    })
    @GetMapping("/search")
    public ResponseEntity<List<FieldListResponse>> searchFields(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        log.info("GET /fields/search?keyword={} - 축구장 검색", keyword);
        List<FieldListResponse> fields = fieldService.searchFields(keyword);
        return ResponseEntity.ok(fields);
    }

    @Operation(summary = "축구장 등록 요청", description = "새로운 축구장 등록을 요청합니다. 관리자 승인 후 목록에 추가됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 요청 성공",
                    content = @Content(schema = @Schema(implementation = CreateFieldResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CreateFieldResponse> createField(
            @Valid @RequestBody CreateFieldRequest request) {
        log.info("POST /fields - 축구장 등록 요청: {}", request.getName());
        CreateFieldResponse response = fieldService.createField(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "축구장 승인", description = "관리자가 축구장 등록 요청을 승인합니다. 승인된 축구장은 일반 사용자에게 공개됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(schema = @Schema(implementation = ApproveFieldResponse.class))),
            @ApiResponse(responseCode = "404", description = "축구장을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApproveFieldResponse> approveField(
            @Parameter(description = "축구장 ID", required = true)
            @PathVariable Long id) {
        log.info("PATCH /fields/{}/approve - 축구장 승인", id);
        ApproveFieldResponse response = fieldService.approveField(id);
        return ResponseEntity.ok(response);
    }
}
