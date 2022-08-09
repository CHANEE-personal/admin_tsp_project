package com.tsp.new_tsp_admin.api.common;

import com.tsp.new_tsp_admin.api.common.service.AdminCommonJpaService;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
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

import static com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity.*;
import static java.lang.Math.ceil;

@Validated
@RestController
@Api(tags = "공통코드관련 API")
@RequestMapping("/api/jpa-common")
@RequiredArgsConstructor
public class AdminCommonJpaController {

    private final AdminCommonJpaService adminCommonJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : commonCodeList
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "공통 코드 리스트 조회", notes = "공통 코드 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통 코드 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> commonCodeList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        // 페이징 및 검색
        Map<String, Object> commonMap = searchCommon.searchCommon(page, paramMap);

        Integer commonCodeListCount = this.adminCommonJpaService.findCommonCodeListCount(commonMap);
        List<CommonCodeDTO> commonCodeList = new ArrayList<>();

        if (commonCodeListCount > 0) {
            commonCodeList = this.adminCommonJpaService.findCommonCodeList(commonMap);
        }

        // 리스트 수
        commonMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        commonMap.put("perPageListCnt", ceil((commonCodeListCount - 1) / page.getSize() + 1));
        // 전체 아이템 수
        commonMap.put("commonCodeListTotalCnt", commonCodeListCount);

        commonMap.put("commonCodeList", commonCodeList);

        return commonMap;
    }

    /**
     * <pre>
     * 1. MethodName : commonCodeInfo
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 상세
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "공통코드 상세 조회", notes = "공통코드를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public CommonCodeDTO commonCodeInfo(@PathVariable Integer idx) throws Exception {
        return this.adminCommonJpaService.findOneCommonCode(builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "공통코드 저장", notes = "공통코드를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통코드 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public CommonCodeDTO insertCommonCode(@Valid @RequestBody CommonCodeEntity commonCodeEntity) throws Exception {
        return this.adminCommonJpaService.insertCommonCode(commonCodeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "공통코드 수정", notes = "공통코드를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통코드 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public CommonCodeDTO updateCommonCode(@Valid @RequestBody CommonCodeEntity commonCodeEntity) throws Exception {
        return adminCommonJpaService.updateCommonCode(commonCodeEntity);
    }
}
