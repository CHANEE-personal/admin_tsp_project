package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/jpa-model")
@RequiredArgsConstructor
public class AdminModelJpaController {

    private final AdminModelJpaService adminModelJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getModelList
     * 2. ClassName  : AdminModelJpaApi.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     * @param categoryCd
     * @param paramMap
     * @param page
     */
    @ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/{categoryCd}")
    public ConcurrentHashMap<String, Object> getModelList(@PathVariable("categoryCd")
                                                          @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          Map<String, Object> paramMap, Page page) {

        // 페이징 및 검색
        ConcurrentHashMap<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
        modelMap.put("categoryCd", categoryCd);

        Long modelListCnt = this.adminModelJpaService.getModelsCount(modelMap);

        List<AdminModelEntity> modelList = new ArrayList<>();

        if (modelListCnt > 0) {
            modelList = this.adminModelJpaService.getModelsList(modelMap);
        }

        // 리스트 수
        modelMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        modelMap.put("perPageListCnt", Math.ceil((modelListCnt - 1) / page.getSize() + 1));
        // 전체 아이템 수
        modelMap.put("modelListTotalCnt", modelListCnt);

        modelMap.put("modelList", modelList);

        return modelMap;
    }

    /**
     * <pre>
     * 1. MethodName : getModelEdit
     * 2. ClassName  : AdminModelJpaApi.java
     * 3. Comment    : 관리자 모델 상세
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     * @param categoryCd
     * @param idx
     */
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}")
    public AdminModelEntity getModelEdit(@PathVariable("categoryCd")
                                      @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                      @PathVariable("idx") Integer idx) {

        AdminModelEntity adminModelEntity = AdminModelEntity.builder().idx(idx).categoryCd(categoryCd).build();

        return this.adminModelJpaService.getOneModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : modelCommonCode
     * 2. ClassName  : AdminModelJpaApi.java
     * 3. Comment    : 관리자 모델 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     */
    @ApiOperation(value = "모델 공통 코드 조회", notes = "모델을 공통 코드를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "브랜드 등록성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/common")
    public ConcurrentHashMap<String, Object> modelCommonCode() {
        ConcurrentHashMap<String, Object> modelCmmCode = new ConcurrentHashMap<>();

        CommonCodeEntity modelCodeEntity = CommonCodeEntity.builder().cmmType("model").build();

        modelCmmCode.put("modelCmmCode", this.adminModelJpaService.modelCommonCode(modelCodeEntity));

        return modelCmmCode;
    }
}