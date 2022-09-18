package com.tsp.new_tsp_admin.api.notice;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
import com.tsp.new_tsp_admin.api.notice.service.AdminNoticeJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/jpa-notice")
@Api(tags = "공지사항 관련 API")
@RequiredArgsConstructor
public class AdminNoticeJpaController {
    private final AdminNoticeJpaService adminNoticeJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findNoticesList
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 조회", notes = "공지사항을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findNoticesList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        Map<String, Object> noticeMap = new HashMap<>();

        Integer noticeCount = this.adminNoticeJpaService.findNoticeCount(searchCommon.searchCommon(page, paramMap));
        List<AdminNoticeDTO> noticeList = new ArrayList<>();

        if (noticeCount > 0) {
            noticeList = this.adminNoticeJpaService.findNoticesList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        noticeMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        noticeMap.put("perPageListCnt", ceil((double) noticeCount / page.getSize()));
        // 전체 아이템 수
        noticeMap.put("noticeListCnt", noticeCount);

        noticeMap.put("noticeList", noticeList);

        return noticeMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public AdminNoticeDTO findOneNotice(@PathVariable Integer idx) throws Exception {
        return adminNoticeJpaService.findOneNotice(AdminNoticeEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    @ApiOperation(value = "이전 공지사항 상세 조회", notes = "이전 공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 공지사항 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public AdminNoticeDTO findPrevOneNotice(@PathVariable Integer idx) throws Exception {
        return adminNoticeJpaService.findPrevOneNotice(AdminNoticeEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 다음 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    @ApiOperation(value = "다음 공지사항 상세 조회", notes = "다음 공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 공지사항 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public AdminNoticeDTO findNextOneNotice(@PathVariable Integer idx) throws Exception {
        return adminNoticeJpaService.findNextOneNotice(AdminNoticeEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 저장", notes = "공지사항을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public AdminNoticeDTO insertNotice(@Valid @RequestBody AdminNoticeEntity adminNoticeEntity) throws Exception {
        return this.adminNoticeJpaService.insertNotice(adminNoticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 수정", notes = "공지사항을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}", consumes = APPLICATION_JSON_VALUE)
    public AdminNoticeDTO updateNotice(@Valid @RequestBody AdminNoticeEntity adminNoticeEntity) throws Exception {
        return adminNoticeJpaService.updateNotice(adminNoticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항을 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 삭제 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public Integer deleteNotice(@PathVariable Integer idx) throws Exception {
        return adminNoticeJpaService.deleteNotice(idx);
    }
}
