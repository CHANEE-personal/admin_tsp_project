package com.tsp.new_tsp_admin.api.faq;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import com.tsp.new_tsp_admin.api.faq.service.AdminFaqJpaService;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/jpa-faq")
@Api(tags = "FAQ 관련 API")
@RequiredArgsConstructor
public class AdminFaqJpaController {
    private final AdminFaqJpaService adminFaqJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "FAQ 조회", notes = "FAQ를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findFaqList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> faqMap = new HashMap<>();

        int faqCount = this.adminFaqJpaService.findFaqCount(searchCommon.searchCommon(page, paramMap));
        List<AdminFaqDTO> faqList = new ArrayList<>();

        if (faqCount > 0) {
            faqList = this.adminFaqJpaService.findFaqList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        faqMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        faqMap.put("perPageListCnt", ceil((double) faqCount / page.getSize()));
        // 전체 아이템 수
        faqMap.put("faqListCnt", faqCount);

        faqMap.put("faqList", faqList);

        return ResponseEntity.ok().body(faqMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "FAQ 상세 조회", notes = "FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 상세 조회 성공", response = AdminFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminFaqDTO> findOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(adminFaqJpaService.findOneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 이전 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @ApiOperation(value = "이전 FAQ 상세 조회", notes = "이전 FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 FAQ 상세 조회 성공", response = AdminFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminFaqDTO> findPrevOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(adminFaqJpaService.findPrevOneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 다음 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @ApiOperation(value = "다음 FAQ 상세 조회", notes = "다음 FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 FAQ 상세 조회 성공", response = AdminFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminFaqDTO> findNextOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(adminFaqJpaService.findNextOneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 FAQ 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "FAQ 저장", notes = "FAQ를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "FAQ 등록성공", response = AdminFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminFaqDTO> insertFaq(@Valid @RequestBody AdminFaqEntity adminFaqEntity) {
        return ResponseEntity.created(URI.create("")).body(adminFaqJpaService.insertFaq(adminFaqEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 FAQ 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "FAQ 수정", notes = "FAQ를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 수정 성공", response = AdminFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminFaqDTO> updateFaq(@PathVariable Long idx, @Valid @RequestBody AdminFaqEntity adminFaqEntity) {
        if (adminFaqJpaService.findOneFaq(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminFaqJpaService.updateFaq(adminFaqEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : AdminFaqJpaController.java
     * 3. Comment    : 관리자 FAQ 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @ApiOperation(value = "FAQ 삭제", notes = "FAQ를 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "FAQ 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public ResponseEntity<Long> deleteFaq(@PathVariable Long idx) {
        if (adminFaqJpaService.findOneFaq(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        adminFaqJpaService.deleteFaq(idx);
        return ResponseEntity.noContent().build();
    }
}
