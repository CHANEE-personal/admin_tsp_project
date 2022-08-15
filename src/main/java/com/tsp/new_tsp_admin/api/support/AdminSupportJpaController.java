package com.tsp.new_tsp_admin.api.support;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.new_tsp_admin.api.support.service.AdminSupportJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@Api(tags = "지원모델관련 API")
@RequestMapping("/api/jpa-support")
@RequiredArgsConstructor
public class AdminSupportJpaController {
    private final AdminSupportJpaService adminSupportJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getSupportList
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 조회", notes = "지원모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/lists")
    public Map<String, Object> getSupportList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        // 페이징 및 검색
        Map<String, Object> supportMap = searchCommon.searchCommon(page, paramMap);

        Integer supportListCount = this.adminSupportJpaService.findSupportsCount(supportMap);
        List<AdminSupportDTO> supportList = new ArrayList<>();

        if (supportListCount > 0) {
            supportList = this.adminSupportJpaService.findSupportsList(supportMap);
        }

        // 리스트 수
        supportMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        supportMap.put("perPageListCnt", ceil((supportListCount - 1) / page.getSize() + 1));
        // 전체 아이템 수
        supportMap.put("modelListTotalCnt", supportListCount);

        supportMap.put("supportList", supportList);

        return supportMap;
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 수정", notes = "지원모델을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminSupportDTO updateSupportModel(@Valid @RequestBody AdminSupportEntity adminSupportEntity) throws Exception {
        return adminSupportJpaService.updateSupportModel(adminSupportEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 삭제", notes = "지원모델을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 삭제 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Integer deleteSupportModel(@PathVariable Integer idx) throws Exception {
        return adminSupportJpaService.deleteSupportModel(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationsList
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 평가 리스트 조회", notes = "지원모델을 평가 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 리스트 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/evaluation/lists")
    public Map<String, Object> findEvaluationsList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        // 페이징 및 검색
        Map<String, Object> evaluationMap = searchCommon.searchCommon(page, paramMap);

        Integer evaluationCount = this.adminSupportJpaService.findEvaluationsCount(evaluationMap);
        List<EvaluationDTO> evaluationsList = new ArrayList<>();

        if (evaluationCount > 0) {
            evaluationsList = this.adminSupportJpaService.findEvaluationsList(evaluationMap);
        }

        // 리스트 수
        evaluationMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        evaluationMap.put("perPageListCnt", ceil((evaluationCount - 1) / page.getSize() + 1));
        // 전체 아이템 수
        evaluationMap.put("modelListTotalCnt", evaluationCount);

        evaluationMap.put("supportList", evaluationsList);

        return evaluationMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneEvaluation
     * 2. ClassName  : findEvaluationsList.java
     * 3. Comment    : 관리자 지원모델 평가 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 평가 상세 조회", notes = "지원모델 평가를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 상세 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/evaluation/{idx}")
    public EvaluationDTO findOneEvaluation(@PathVariable Integer idx) throws Exception {
        return this.adminSupportJpaService.findOneEvaluation(EvaluationEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : evaluationSupportModel
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 평가", notes = "지원모델을 평가한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/{idx}/evaluation")
    public EvaluationDTO evaluationSupportModel(@Valid @RequestBody EvaluationEntity evaluationEntity,
                                                @PathVariable("idx") Integer idx) throws Exception {
        return adminSupportJpaService.evaluationSupportModel(EvaluationEntity.builder().supportIdx(idx)
                .evaluateComment(evaluationEntity.getEvaluateComment())
                .visible("Y").build());
    }

    /**
     * <pre>
     * 1. MethodName : updateEvaluation
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 평가 수정", notes = "지원모델을 평가를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/evaluation")
    public EvaluationDTO updateEvaluation(@Valid @RequestBody EvaluationEntity evaluationEntity) throws Exception {
        return adminSupportJpaService.updateEvaluation(evaluationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteEvaluation
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "지원모델 평가 삭제", notes = "지원모델을 평가를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/evaluation")
    public Integer deleteEvaluation(@PathVariable Integer idx) throws Exception {
        return adminSupportJpaService.deleteEvaluation(idx);
    }
}
