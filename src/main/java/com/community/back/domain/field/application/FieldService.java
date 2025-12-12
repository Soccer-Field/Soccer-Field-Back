package com.community.back.domain.field.application;

import com.community.back.domain.field.domain.Field;
import com.community.back.domain.field.domain.FieldStatus;
import com.community.back.domain.field.domain.repository.FieldRepository;
import com.community.back.domain.field.presentation.dto.request.CreateFieldRequest;
import com.community.back.domain.field.presentation.dto.response.ApproveFieldResponse;
import com.community.back.domain.field.presentation.dto.response.CreateFieldResponse;
import com.community.back.domain.field.presentation.dto.response.FieldDetailResponse;
import com.community.back.domain.field.presentation.dto.response.FieldListResponse;
import com.community.back.global.exception.CustomException;
import com.community.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FieldService {

    private final FieldRepository fieldRepository;

    /**
     * 승인된 축구장 목록 조회
     */
    public List<FieldListResponse> getAllFields() {
        log.info("Fetching all approved fields");
        return fieldRepository.findByStatus(FieldStatus.APPROVED)
                .stream()
                .map(FieldListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 축구장 상세 조회
     */
    public FieldDetailResponse getFieldById(Long fieldId) {
        log.info("Fetching field details for id: {}", fieldId);
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new CustomException(ErrorCode.FIELD_NOT_FOUND));

        // 승인된 축구장만 조회 가능
        if (field.getStatus() != FieldStatus.APPROVED) {
            throw new CustomException(ErrorCode.FIELD_NOT_FOUND);
        }

        return FieldDetailResponse.from(field);
    }

    /**
     * 축구장 검색
     */
    public List<FieldListResponse> searchFields(String keyword) {
        log.info("Searching fields with keyword: {}", keyword);
        return fieldRepository.findByNameContainingOrAddressContaining(keyword, keyword)
                .stream()
                .filter(field -> field.getStatus() == FieldStatus.APPROVED)
                .map(FieldListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 축구장 등록 요청
     */
    @Transactional
    public CreateFieldResponse createField(CreateFieldRequest request) {
        log.info("Creating new field registration request: {}", request.getName());

        // 주소 기반으로 위경도 변환 (간단한 구현 - 실제로는 Geocoding API 사용 필요)
        // 여기서는 임시로 기본값 설정
        Double lat = 37.5665; // 서울시청 위도 (기본값)
        Double lng = 126.9780; // 서울시청 경도 (기본값)

        Field field = Field.builder()
                .name(request.getName())
                .address(request.getAddress())
                .lat(lat)
                .lng(lng)
                .image(request.getImageUrl())
                .grassType(request.getGrassType())
                .shoeType(request.getRecommendedShoe())
                .grassCondition("보통") // 기본값
                .status(FieldStatus.PENDING_APPROVAL)
                .build();

        Field savedField = fieldRepository.save(field);
        log.info("Field registration request created with id: {}", savedField.getFieldId());

        return CreateFieldResponse.builder()
                .fieldId(savedField.getFieldId())
                .status("pending_approval")
                .build();
    }

    /**
     * 축구장 승인 (관리자용)
     */
    @Transactional
    public ApproveFieldResponse approveField(Long fieldId) {
        log.info("Approving field with id: {}", fieldId);

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new CustomException(ErrorCode.FIELD_NOT_FOUND));

        // 이미 승인된 축구장인지 확인
        if (field.getStatus() == FieldStatus.APPROVED) {
            log.warn("Field {} is already approved", fieldId);
            return ApproveFieldResponse.builder()
                    .fieldId(field.getFieldId())
                    .status("approved")
                    .message("Field is already approved")
                    .build();
        }

        // 승인 처리
        field.approve();
        fieldRepository.save(field);

        log.info("Field {} approved successfully", fieldId);

        return ApproveFieldResponse.builder()
                .fieldId(field.getFieldId())
                .status("approved")
                .message("Field approved successfully")
                .build();
    }
}
