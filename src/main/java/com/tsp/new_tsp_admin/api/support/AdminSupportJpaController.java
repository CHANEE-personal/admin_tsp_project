package com.tsp.new_tsp_admin.api.support;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
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

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;

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
     *
     */
    @ApiOperation(value = "지원모델 조회", notes = "지원모델을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/lists")
    public Map<String, Object> getSupportList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                Page page) throws Exception {
        // 페이징 및 검색
        Map<String, Object> supportMap = searchCommon.searchCommon(page, paramMap);

        List<AdminSupportDTO> supportList = new ArrayList<>();

        if (this.adminSupportJpaService.findSupportsCount(supportMap) > 0) {
            supportList = this.adminSupportJpaService.findSupportsList(supportMap);
        }

        // 리스트 수
        supportMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        supportMap.put("perPageListCnt", ceil((this.adminSupportJpaService.findSupportsCount(supportMap) - 1) / page.getSize() + 1));
        // 전체 아이템 수
        supportMap.put("modelListTotalCnt", this.adminSupportJpaService.findSupportsCount(supportMap));

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
     *
     */
    @ApiOperation(value = "지원모델 수정", notes = "지원모델을 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "모델 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminSupportDTO updateSupportModel(@RequestBody AdminSupportEntity adminSupportEntity) throws Exception {
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
     *
     */
    @ApiOperation(value = "지원모델 삭제", notes = "지원모델을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "모델 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public AdminSupportDTO deleteSupportModel(@RequestBody AdminSupportEntity adminSupportEntity,
                                              @PathVariable("idx") Integer idx) throws Exception {
        adminSupportEntity.setIdx(idx);
        return adminSupportJpaService.deleteSupportModel(adminSupportEntity);
    }
}
