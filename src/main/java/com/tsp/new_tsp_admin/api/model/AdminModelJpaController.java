package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@Api(tags = "모델관련 API")
@RequestMapping("/api/jpa-model")
@RequiredArgsConstructor
public class AdminModelJpaController {
    private final AdminModelJpaService adminModelJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getModelList
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/{categoryCd}")
    public Map<String, Object> getModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                            @RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        // 페이징 및 검색
        Map<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
        modelMap.put("categoryCd", categoryCd);

        Integer modelListCount = this.adminModelJpaService.findModelsCount(modelMap);
        List<AdminModelDTO> modelList = new ArrayList<>();

        if (modelListCount > 0) {
            modelList = this.adminModelJpaService.findModelsList(modelMap);
        }

        // 리스트 수
        modelMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        modelMap.put("perPageListCnt", ceil((modelListCount - 1) / page.getSize() + 1));
        // 전체 아이템 수
        modelMap.put("modelListTotalCnt", modelListCount);

        modelMap.put("modelList", modelList);

        return modelMap;
    }

    /**
     * <pre>
     * 1. MethodName : getModelEdit
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 상세
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 상세조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}")
    public AdminModelDTO getModelEdit(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                      @PathVariable Integer idx) throws Exception {
        return this.adminModelJpaService.findOneModel(AdminModelEntity.builder().idx(idx).categoryCd(categoryCd).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 draft 상태로 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 저장", notes = "모델을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public AdminModelDTO insertModel(@Valid @RequestBody AdminModelEntity adminModelEntity) throws Exception {
        return this.adminModelJpaService.insertModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 이미지 저장", notes = "모델 이미지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 이미지 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public String insertModelImage(@PathVariable Integer idx, @RequestParam("images") List<MultipartFile> fileName) throws Exception {
        return this.adminModelJpaService.insertModelImage(CommonImageEntity.builder().typeName("model").typeIdx(idx).visible("Y").build(), fileName);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 이미지 삭제", notes = "모델 이미지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 이미지 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}/images")
    public Integer deleteModelImage(@PathVariable Integer idx) throws Exception {
        return this.adminModelJpaService.deleteModelImage(idx);
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminModelDTO updateModel(@Valid @RequestBody AdminModelEntity adminModelEntity) throws Exception {
        return adminModelJpaService.updateModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     */
    @ApiOperation(value = "모델 삭제", notes = "모델을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Integer deleteModel(@PathVariable Integer idx) throws Exception {
        return adminModelJpaService.deleteModel(idx);
    }

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "모델 소속사 수정", notes = "모델 소속사를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 소속사 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/agency")
    public AdminModelDTO updateModelAgency(@Valid @RequestBody AdminModelEntity adminModelEntity,
                                           @PathVariable Integer idx) throws Exception {
        return adminModelJpaService.updateModelAgency(AdminModelEntity.builder().idx(idx).agencyIdx(adminModelEntity.getAgencyIdx()).build());
    }
}
