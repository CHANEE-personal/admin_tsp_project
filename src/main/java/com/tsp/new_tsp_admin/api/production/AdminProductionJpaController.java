package com.tsp.new_tsp_admin.api.production;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.production.service.AdminProductionJpaService;
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
import static org.springframework.web.client.HttpClientErrorException.*;

@RestController
@RequestMapping("/api/production")
@Api(tags = "프로덕션 관련 API")
@RequiredArgsConstructor
public class AdminProductionJpaController {
    private final AdminProductionJpaService adminProductionJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 조회", notes = "프로덕션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findProductionList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> productionMap = new HashMap<>();

        int productionCnt = this.adminProductionJpaService.findProductionCount(searchCommon.searchCommon(page, paramMap));
        List<AdminProductionDTO> productionList = new ArrayList<>();

        if (productionCnt > 0) {
            productionList = this.adminProductionJpaService.findProductionList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        productionMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        productionMap.put("perPageListCnt", ceil((double) productionCnt / page.getSize()));
        // 전체 아이템 수
        productionMap.put("productionListCnt", productionCnt);

        productionMap.put("productionList", productionList);

        return ResponseEntity.ok().body(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 15.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 상세 조회", notes = "프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 상세 조회 성공", response = AdminProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminProductionDTO> findOneProduction(@PathVariable Long idx) {
        return ResponseEntity.ok(adminProductionJpaService.findOneProduction(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 이전 프로덕션 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    @ApiOperation(value = "이전 프로덕션 상세 조회", notes = "이전 프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 프로덕션 상세조회 성공", response = AdminProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminProductionDTO> findPrevOneProduction(@PathVariable Long idx) {
        return ResponseEntity.ok(adminProductionJpaService.findPrevOneProduction(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 다음 프로덕션 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    @ApiOperation(value = "다음 프로덕션 상세 조회", notes = "다음 프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 프로덕션 상세조회 성공", response = AdminProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminProductionDTO> findNextOneProduction(@PathVariable Long idx) {
        return ResponseEntity.ok(adminProductionJpaService.findNextOneProduction(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 저장", notes = "프로덕션을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "프로덕션 등록성공", response = AdminProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminProductionDTO> insertProduction(@Valid @RequestBody AdminProductionEntity adminProductionEntity) {
        return ResponseEntity.created(URI.create("")).body(adminProductionJpaService.insertProduction(adminProductionEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 수정", notes = "프로덕션을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 수정 성공", response = AdminProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminProductionDTO> updateProduction(@PathVariable Long idx, @Valid @RequestBody AdminProductionEntity adminProductionEntity) {
        if (adminProductionJpaService.findOneProduction(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminProductionJpaService.updateProduction(adminProductionEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 삭제", notes = "프로덕션을 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "프로덕션 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public ResponseEntity<Long> deleteProduction(@PathVariable Long idx) {
        if (adminProductionJpaService.findOneProduction(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        adminProductionJpaService.deleteProduction(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : findProductionAdminComment
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 어드민 코멘트 조회", notes = "프로덕션 어드민 코멘트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 어드민 코멘트 조회성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/admin-comment")
    public ResponseEntity<List<AdminCommentDTO>> findProductionAdminComment(@PathVariable Long idx) {
        if (adminProductionJpaService.findOneProduction(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminProductionJpaService.findProductionAdminComment(idx));
    }
}
