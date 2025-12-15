package com.community.back.domain.comment.presentation;

import com.community.back.domain.comment.application.CommentService;
import com.community.back.domain.comment.presentation.dto.request.CreateCommentRequest;
import com.community.back.domain.comment.presentation.dto.request.UpdateCommentRequest;
import com.community.back.domain.comment.presentation.dto.response.CommentResponse;
import com.community.back.domain.comment.presentation.dto.response.CreateCommentResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관리 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "특정 리뷰의 댓글 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class))))
    })
    @GetMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId) {
        log.info("GET /reviews/{}/comments - 댓글 목록 조회", reviewId);
        List<CommentResponse> comments = commentService.getCommentsByReviewId(reviewId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "댓글 작성", description = "리뷰에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = CreateCommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
            @Parameter(description = "리뷰 ID", required = true)
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateCommentRequest request) {
        log.info("POST /reviews/{}/comments - 댓글 작성", reviewId);
        Long userId = getUserIdFromAuth();
        CreateCommentResponse response = commentService.createComment(reviewId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "댓글 ID", required = true)
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request) {
        log.info("PUT /comments/{} - 댓글 수정", commentId);
        Long userId = getUserIdFromAuth();
        CommentResponse response = commentService.updateComment(commentId, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "댓글 ID", required = true)
            @PathVariable Long commentId) {
        log.info("DELETE /comments/{} - 댓글 삭제", commentId);
        Long userId = getUserIdFromAuth();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("User is not authenticated");
            throw new com.community.back.global.exception.CustomException(
                com.community.back.global.exception.ErrorCode.INVALID_TOKEN);
        }

        String userIdStr = authentication.getPrincipal().toString();
        log.info("UserId from authentication: {}", userIdStr);

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            log.error("Failed to parse userId: {}", userIdStr);
            throw new com.community.back.global.exception.CustomException(
                com.community.back.global.exception.ErrorCode.INVALID_TOKEN);
        }
    }
}
