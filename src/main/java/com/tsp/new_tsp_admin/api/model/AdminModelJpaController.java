package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;

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

        Long modelListCnt = this.adminModelJpaService.findModelsCount(modelMap);

        List<AdminModelDTO> modelList = new ArrayList<>();

        if (modelListCnt > 0) {
            modelList = this.adminModelJpaService.findModelsList(modelMap);
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
     * 2. ClassName  : AdminModelJpaController.java
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
    public AdminModelDTO getModelEdit(@PathVariable("categoryCd")
                                      @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                      @PathVariable("idx") Integer idx) {
        return this.adminModelJpaService.findOneModel(builder().idx(idx).categoryCd(categoryCd).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 draft 상태로 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @ApiOperation(value = "모델 저장", notes = "모델을 저장한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "모델 등록성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public AdminModelDTO insertModel(@RequestBody AdminModelEntity adminModelEntity) throws Exception {
        return this.adminModelJpaService.insertModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "모델 수정성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminModelDTO updateModel(@RequestBody AdminModelEntity adminModelEntity,
                                        @PathVariable("idx") Integer idx) throws Exception {
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
     *
     */
    @ApiOperation(value = "모델 삭제", notes = "모델을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "모델 삭제성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public AdminModelDTO deleteModel(@RequestBody AdminModelEntity adminModelEntity,
                                     @PathVariable("idx") Integer idx) throws Exception {
        adminModelEntity.setIdx(idx);
        return adminModelJpaService.deleteModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @ApiOperation(value = "모델 이미지 저장", notes = "모델 이미지를 저장한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "모델 이미지 등록성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String insertModelImage(AdminModelEntity adminModelEntity,
                                   CommonImageEntity commonImageEntity,
                                   @RequestParam("images") List<MultipartFile> fileName) throws Exception {
        CommonImageEntity.builder().typeName("model").typeIdx(adminModelEntity.getIdx()).visible("Y").build();

        return this.adminModelJpaService.insertModelImage(commonImageEntity, fileName);
    }

    /**
     * <pre>
     * 1. MethodName : modelCommonCode
     * 2. ClassName  : AdminModelJpaController.java
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

        modelCmmCode.put("modelCmmCode", this.adminModelJpaService.modelCommonCode(CommonCodeEntity.builder().cmmType("model").build()));

        return modelCmmCode;
    }
}
