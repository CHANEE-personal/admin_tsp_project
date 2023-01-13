package com.tsp.new_tsp_admin.api.comment;

import com.tsp.new_tsp_admin.api.comment.service.AdminCommentJpaService;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/jpa-comment")
@Api(tags = "어드민 코멘트 관련 API")
@RequiredArgsConstructor
public class AdminCommentJpaController {
    private final AdminCommentJpaService adminCommentJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "어드민 코멘트 조회", notes = "어드민 코멘트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "어드민 코멘트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findAdminCommentList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> commentMap = new HashMap<>();

        int commentCount = this.adminCommentJpaService.findAdminCommentCount(searchCommon.searchCommon(page, paramMap));
        List<AdminCommentDTO> commentList = new ArrayList<>();

        if (commentCount > 0) {
            commentList = this.adminCommentJpaService.findAdminCommentList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        commentMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        commentMap.put("perPageListCnt", ceil((double) commentCount / page.getSize()));
        // 전체 아이템 수
        commentMap.put("commentListCnt", commentCount);

        commentMap.put("commentList", commentList);

        return ResponseEntity.ok().body(commentMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "어드민 코멘트 상세 조회", notes = "어드민 코멘트를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "어드민 코멘트 상세 조회 성공", response = AdminCommentDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminCommentDTO> findOneAdminComment(@PathVariable Long idx) {
        return ResponseEntity.ok(adminCommentJpaService.findOneAdminComment(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 모델 코멘트 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "모델 어드민 코멘트 저장", notes = "어드민 코멘트를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 어드민 코멘트 등록성공", response = AdminCommentDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/model/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCommentDTO> insertModelAdminComment(Long idx, @Valid @RequestBody AdminCommentEntity adminCommentEntity) {
        return ResponseEntity.created(URI.create("")).body(adminCommentJpaService.insertModelAdminComment(idx, adminCommentEntity));
    }

    /**
     * <pre>
     * 1. MethodName : insertProductionAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 프로덕션 코멘트 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 어드민 코멘트 저장", notes = "프로덕션 어드민 코멘트를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "프로덕션 어드민 코멘트 등록성공", response = AdminCommentDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/production/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCommentDTO> insertProductionAdminComment(Long idx, @Valid @RequestBody AdminCommentEntity adminCommentEntity) {
        return ResponseEntity.created(URI.create("")).body(adminCommentJpaService.insertProductionAdminComment(idx, adminCommentEntity));
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 모델 코멘트 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 어드민 코멘트 저장", notes = "포트폴리오 어드민 코멘트를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "포트폴리오 어드민 코멘트 등록성공", response = AdminCommentDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/portfolio/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCommentDTO> insertPortfolioAdminComment(Long idx, @Valid @RequestBody AdminCommentEntity adminCommentEntity) {
        return ResponseEntity.created(URI.create("")).body(adminCommentJpaService.insertPortfolioAdminComment(idx, adminCommentEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "어드민 코멘트 수정", notes = "어드민 코멘트를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "어드민 코멘트 수정 성공", response = AdminCommentDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCommentDTO> updateAdminComment(@PathVariable Long idx, @Valid @RequestBody AdminCommentEntity adminCommentEntity) {
        return ResponseEntity.ok(adminCommentJpaService.updateAdminComment(idx, adminCommentEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "어드민 코멘트 삭제", notes = "어드민 코멘트를 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "어드민 코멘트 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public ResponseEntity<Long> deleteAdminComment(@PathVariable Long idx) {
        adminCommentJpaService.deleteAdminComment(idx);
        return ResponseEntity.noContent().build();
    }
}
