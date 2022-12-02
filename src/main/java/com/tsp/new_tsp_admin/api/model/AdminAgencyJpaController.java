package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.model.service.agency.AdminAgencyJpaService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Validated
@RestController
@Api(tags = "모델 소속사 관련 API")
@RequestMapping("/api/agency")
@RequiredArgsConstructor
public class AdminAgencyJpaController {
    private final AdminAgencyJpaService adminAgencyJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 조회", notes = "소속사를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findAgencyList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        // 페이징 및 검색
        Map<String, Object> agencyMap = searchCommon.searchCommon(page, paramMap);

        Integer agencyListCount = this.adminAgencyJpaService.findAgencyCount(agencyMap);
        List<AdminAgencyDTO> agencyList = new ArrayList<>();

        if (agencyListCount > 0) {
            agencyList = this.adminAgencyJpaService.findAgencyList(agencyMap);
        }

        // 리스트 수
        agencyMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        agencyMap.put("perPageListCnt", ceil((double) agencyListCount / page.getSize()));
        // 전체 아이템 수
        agencyMap.put("agencyListTotalCnt", agencyListCount);

        agencyMap.put("agencyList", agencyList);

        return agencyMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 상세 조회", notes = "소속사를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public AdminAgencyDTO findOneAgency(@PathVariable Long idx) throws Exception {
        return this.adminAgencyJpaService.findOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 저장", notes = "소속사를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "소속사 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public AdminAgencyDTO insertAgency(@Valid @RequestBody AdminAgencyEntity adminAgencyEntity) throws Exception {
        return this.adminAgencyJpaService.insertAgency(adminAgencyEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 수정", notes = "소속사를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "소속사 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminAgencyDTO updateAgency(@Valid @RequestBody AdminAgencyEntity adminAgencyEntity) throws Exception {
        return adminAgencyJpaService.updateAgency(adminAgencyEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 삭제", notes = "소속사를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "소속사 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Long deleteAgency(@PathVariable Long idx) throws Exception {
        return adminAgencyJpaService.deleteAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertAgencyImage
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 Image 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 이미지 저장", notes = "소속사 이미지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "소속사 이미지 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public String insertAgencyImage(@PathVariable Long idx, @RequestParam("images") List<MultipartFile> fileName) throws Exception {
        return this.adminAgencyJpaService.insertAgencyImage(CommonImageEntity.builder().typeName("agency").typeIdx(idx).visible("Y").build(), fileName);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgencyImage
     * 2. ClassName  : AdminAgencyJpaController.java
     * 3. Comment    : 관리자 소속사 Image 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "소속사 이미지 삭제", notes = "소속사 이미지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 이미지 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}/images")
    public Long deleteAgencyImage(@PathVariable Long idx) throws Exception {
        return this.adminAgencyJpaService.deleteAgencyImage(idx);
    }
}
