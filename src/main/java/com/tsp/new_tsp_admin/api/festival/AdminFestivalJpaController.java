package com.tsp.new_tsp_admin.api.festival;

import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalDTO;
import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalEntity;
import com.tsp.new_tsp_admin.api.festival.service.AdminFestivalJpaService;
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

@RestController
@RequestMapping("/api/festival")
@Api(tags = "행사 관련 API")
@RequiredArgsConstructor
public class AdminFestivalJpaController {

    private final AdminFestivalJpaService adminFestivalJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 리스트 조회", notes = "행사 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findFestivalList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> festivalMap = new HashMap<>();

        int festivalCount = this.adminFestivalJpaService.findFestivalCount(searchCommon.searchCommon(page, paramMap));
        List<AdminFestivalDTO> festivalList = new ArrayList<>();

        if (festivalCount > 0) {
            festivalList = this.adminFestivalJpaService.findFestivalList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        festivalMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        festivalMap.put("perPageListCnt", ceil((double) festivalCount / page.getSize()));
        // 전체 아이템 수
        festivalMap.put("festivalListCnt", festivalCount);

        festivalMap.put("festivalList", festivalList);

        return ResponseEntity.ok().body(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 상세 조회", notes = "행사 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 상세 조회 성공", response = AdminFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<AdminFestivalDTO> findOneFestival(@PathVariable Long idx) {
        return ResponseEntity.ok(adminFestivalJpaService.findOneFestival(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 등록", notes = "행사를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "행사 등록 성공", response = AdminFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminFestivalDTO> insertFestival(@Valid @RequestBody AdminFestivalEntity adminFestivalEntity) {
        return ResponseEntity.created(URI.create("")).body(adminFestivalJpaService.insertFestival(adminFestivalEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 수정", notes = "행사를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 수정 성공", response = AdminFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminFestivalDTO> updateFestival(@PathVariable Long idx, @Valid @RequestBody AdminFestivalEntity adminFestivalEntity) {
        if (adminFestivalJpaService.findOneFestival(idx) == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminFestivalJpaService.updateFestival(adminFestivalEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 삭제", notes = "행사를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "행사 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteFestival(@PathVariable Long idx) {
        if (adminFestivalJpaService.findOneFestival(idx) == null) {
            ResponseEntity.notFound().build();
        }
        adminFestivalJpaService.deleteFestival(idx);
        return ResponseEntity.noContent().build();
    }
}
