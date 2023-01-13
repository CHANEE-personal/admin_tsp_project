package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.api.model.service.schedule.AdminScheduleJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;

@Validated
@RestController
@Api(tags = "모델 스케줄 관련 API")
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class AdminScheduleJpaController {
    private final AdminScheduleJpaService adminScheduleJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 조회", notes = "모델 스케줄을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findScheduleList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                                @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                                @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                                Page page) {
        // 페이징 및 검색
        Map<String, Object> scheduleMap = searchCommon.searchCommon(page, paramMap);

        if (searchStartTime != null && searchEndTime != null) {
            scheduleMap.put("searchStartTime", searchStartTime);
            scheduleMap.put("searchEndTime", searchEndTime);
        }

        int scheduleListCount = this.adminScheduleJpaService.findScheduleCount(scheduleMap);
        List<AdminScheduleDTO> scheduleList = new ArrayList<>();

        if (scheduleListCount > 0) {
            scheduleList = this.adminScheduleJpaService.findScheduleList(scheduleMap);
        }

        // 리스트 수
        scheduleMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        scheduleMap.put("perPageListCnt", ceil((double) scheduleListCount / page.getSize()));
        // 전체 아이템 수
        scheduleMap.put("scheduleListTotalCnt", scheduleListCount);

        scheduleMap.put("scheduleList", scheduleList);

        return ResponseEntity.ok().body(scheduleMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 상세 조회", notes = "모델 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 상세 조회 성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminScheduleDTO> findOneSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminScheduleJpaService.findOneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 이전 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @ApiOperation(value = "이전 모델 스케줄 상세 조회", notes = "이전 모델 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 모델 스케줄 상세 조회 성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminScheduleDTO> findPrevOneSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminScheduleJpaService.findPrevOneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 다음 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @ApiOperation(value = "다음 모델 스케줄 상세 조회", notes = "다음 모델 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 모델 스케줄 상세 조회 성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminScheduleDTO> findNextOneSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminScheduleJpaService.findNextOneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스켸줄 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 저장", notes = "모델 스케줄을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 스케줄 등록성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminScheduleDTO> insertSchedule(@Valid @RequestBody AdminModelEntity adminModelEntity, @Valid @RequestBody AdminScheduleEntity adminScheduleEntity) throws Exception {
        return ResponseEntity.created(URI.create("")).body(adminScheduleJpaService.insertSchedule(adminModelEntity, adminScheduleEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 수정", notes = "모델 스케줄을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 수정성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminScheduleDTO> updateSchedule(@Valid @RequestBody AdminScheduleEntity adminScheduleEntity) {
        return ResponseEntity.ok(adminScheduleJpaService.updateSchedule(adminScheduleEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 삭제", notes = "모델 스케줄을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 스케줄 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteSchedule(@PathVariable Long idx) {
        adminScheduleJpaService.deleteSchedule(idx);
        return ResponseEntity.noContent().build();
    }
}
