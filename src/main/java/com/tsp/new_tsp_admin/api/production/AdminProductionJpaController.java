package com.tsp.new_tsp_admin.api.production;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.client.HttpClientErrorException.*;

@RestController
@RequestMapping("/api/jpa-production")
@Api(tags = "프로덕션관련 API")
@RequiredArgsConstructor
public class AdminProductionJpaController {
    private final AdminProductionJpaService adminProductionJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getProductionList
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @ApiOperation(value = "프로덕션 조회", notes = "프로덕션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> getProductionList(Page page, @RequestParam(required = false) Map<String, Object> paramMap) {
        Map<String, Object> productionMap = new HashMap<>();

        int productionCnt = this.adminProductionJpaService.findProductionsCount(searchCommon.searchCommon(page, paramMap));

        List<AdminProductionDTO> productionList = new ArrayList<>();

        if (productionCnt > 0) {
            productionList = this.adminProductionJpaService.findProductionsList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        productionMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        productionMap.put("perPageListCnt", ceil((productionCnt - 1) / page.getSize() + 1));
        // 전체 아이템 수
        productionMap.put("productionListCnt", productionCnt);

        productionMap.put("productionList", productionList);

        return productionMap;
    }

    /**
     * <pre>
     * 1. MethodName : getProductionEdit
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 15.
     * </pre>
     *
     */
    @ApiOperation(value = "프로덕션 상세 조회", notes = "프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public AdminProductionDTO getProductionEdit(@PathVariable("idx") Integer idx) {
        return adminProductionJpaService.findOneProduction(AdminProductionEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 draft 상태로 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     *
     */
    @ApiOperation(value = "프로덕션 저장", notes = "프로덕션을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public AdminProductionDTO insertProduction(@RequestBody AdminProductionEntity adminProductionEntity) {
        return this.adminProductionJpaService.insertProduction(adminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     *
     */
    @ApiOperation(value = "프로덕션 수정", notes = "프로덕션을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value ="/{idx}", consumes = APPLICATION_JSON_VALUE)
    public AdminProductionDTO updateProduction(@RequestBody AdminProductionEntity adminProductionEntity) {
        return adminProductionJpaService.updateProduction(adminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     */
    @ApiOperation(value = "프로덕션 삭제", notes = "프로덕션을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public Integer deleteProduction(@PathVariable("idx") Integer idx) {
        return adminProductionJpaService.deleteProduction(idx);
    }
}
