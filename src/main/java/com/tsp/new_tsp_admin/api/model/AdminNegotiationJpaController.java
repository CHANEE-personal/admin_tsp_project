package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.new_tsp_admin.api.model.service.negotiation.AdminNegotiationJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;

@Validated
@RestController
@Api(tags = "모델 섭외 관련 API")
@RequestMapping("/api/negotiation")
@RequiredArgsConstructor
public class AdminNegotiationJpaController {
    private final AdminNegotiationJpaService adminNegotiationJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 조회", notes = "모델 섭외를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findModelNegotiationList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                                        @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                                        @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                                        Page page) {
        // 페이징 및 검색
        Map<String, Object> negotiationMap = searchCommon.searchCommon(page, paramMap);

        if (searchStartTime != null && searchEndTime != null) {
            negotiationMap.put("searchStartTime", searchStartTime);
            negotiationMap.put("searchEndTime", searchEndTime);
        }

        int negotiationCount = this.adminNegotiationJpaService.findNegotiationCount(negotiationMap);
        List<AdminModelDTO> negotiationList = new ArrayList<>();

        if (negotiationCount > 0) {
            negotiationList = this.adminNegotiationJpaService.findModelNegotiationList(negotiationMap);
        }

        // 리스트 수
        negotiationMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        negotiationMap.put("perPageListCnt", ceil((double) negotiationCount / page.getSize()));
        // 전체 아이템 수
        negotiationMap.put("negotiationListTotalCnt", negotiationCount);

        negotiationMap.put("negotiationList", negotiationList);

        return ResponseEntity.ok().body(negotiationMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 상세 조회", notes = "모델 섭외를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 상세 조회 성공", response = AdminNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminNegotiationDTO> findOneNegotiation(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNegotiationJpaService.findOneNegotiation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 이전 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 이전 상세 조회", notes = "모델 섭외를 이전 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 이전 상세 조회 성공", response = AdminNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminNegotiationDTO> findPrevOneNegotiation(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNegotiationJpaService.findPrevOneNegotiation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 다음 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 다음 상세 조회", notes = "모델 섭외를 다음 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 다음 상세 조회 성공", response = AdminNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminNegotiationDTO> findNextOneNegotiation(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNegotiationJpaService.findNextOneNegotiation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 저장", notes = "모델 섭외를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 섭외 등록성공", response = AdminNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminNegotiationDTO> insertModelNegotiation(@Valid @RequestBody AdminNegotiationEntity adminNegotiationEntity) {
        return ResponseEntity.created(URI.create("")).body(adminNegotiationJpaService.insertModelNegotiation(adminNegotiationEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 수정", notes = "모델 섭외를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 수정성공", response = AdminNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminNegotiationDTO> updateModelNegotiation(@PathVariable Long idx, @Valid @RequestBody AdminNegotiationEntity adminNegotiationEntity) {
        if (adminNegotiationJpaService.findOneNegotiation(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminNegotiationJpaService.updateModelNegotiation(adminNegotiationEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaController.java
     * 3. Comment    : 관리자 모델 섭외 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 삭제", notes = "모델 섭외를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 섭외 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteModelNegotiation(@PathVariable Long idx) {
        if (adminNegotiationJpaService.findOneNegotiation(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        adminNegotiationJpaService.deleteModelNegotiation(idx);
        return ResponseEntity.noContent().build();
    }
}
