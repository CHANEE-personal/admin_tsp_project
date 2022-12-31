package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@Api(tags = "모델 관련 API")
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class AdminModelJpaController {
    private final AdminModelJpaService adminModelJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
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
    public ResponseEntity<Map<String, Object>> findModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                             @RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        // 페이징 및 검색
        Map<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
        modelMap.put("categoryCd", categoryCd);

        int modelListCount = this.adminModelJpaService.findModelCount(modelMap);
        List<AdminModelDTO> modelList = new ArrayList<>();

        if (modelListCount > 0) {
            modelList = this.adminModelJpaService.findModelList(modelMap);
        }

        // 리스트 수
        modelMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        modelMap.put("perPageListCnt", ceil((double) modelListCount / page.getSize()));
        // 전체 아이템 수
        modelMap.put("modelListTotalCnt", modelListCount);

        modelMap.put("modelList", modelList);

        return ResponseEntity.ok().body(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 상세조회 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminModelDTO> findOneModel(@PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findOneModel(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 이전 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    @ApiOperation(value = "이전 모델 상세 조회", notes = "이전 모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 모델 상세조회 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/prev")
    public ResponseEntity<AdminModelDTO> findPrevOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findPrevOneModel(AdminModelEntity.builder().idx(idx).categoryCd(categoryCd).build()));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 다음 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    @ApiOperation(value = "다음 모델 상세 조회", notes = "다음 모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 모델 상세조회 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/next")
    public ResponseEntity<AdminModelDTO> findNextOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findNextOneModel(AdminModelEntity.builder().idx(idx).categoryCd(categoryCd).build()));
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 draft 상태로 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 저장", notes = "모델을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 등록성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminModelDTO> insertModel(@Valid @RequestBody AdminModelEntity adminModelEntity) {
        return ResponseEntity.created(URI.create("")).body(adminModelJpaService.insertModel(adminModelEntity));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 이미지 저장", notes = "모델 이미지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 이미지 등록성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CommonImageDTO>> insertModelImage(@PathVariable Long idx, @RequestParam("images") List<MultipartFile> fileName) {
        return ResponseEntity.created(URI.create("")).body(adminModelJpaService.insertModelImage(CommonImageEntity.builder().typeName(EntityType.MODEL).typeIdx(idx).build(), fileName));
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 이미지 삭제", notes = "모델 이미지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 이미지 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}/images")
    public ResponseEntity<Long> deleteModelImage(@PathVariable Long idx) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        adminModelJpaService.deleteImage(CommonImageEntity.builder().typeIdx(idx).typeName(EntityType.MODEL).build());
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 수정성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminModelDTO> updateModel(@PathVariable Long idx, @Valid @RequestBody AdminModelEntity adminModelEntity) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminModelJpaService.updateModel(adminModelEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    @ApiOperation(value = "모델 삭제", notes = "모델을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteModel(@PathVariable Long idx) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        adminModelJpaService.deleteModel(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @ApiOperation(value = "모델 소속사 수정", notes = "모델 소속사를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 소속사 수정성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/agency")
    public ResponseEntity<AdminModelDTO> updateModelAgency(@PathVariable Long idx, @RequestParam Long agencyIdx) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminModelJpaService.updateModelAgency(AdminModelEntity.builder().idx(idx).agencyIdx(agencyIdx).build()));
    }

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @ApiOperation(value = "모델 어드민 코멘트 조회", notes = "모델 어드민 코멘트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 어드민 코멘트 조회성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/admin-comment")
    public ResponseEntity<List<AdminCommentDTO>> findModelAdminComment(@PathVariable Long idx) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminModelJpaService.findModelAdminComment(idx));
    }

    /**
     * <pre>
     * 1. MethodName : toggleModelNewYn
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 새로운 모델 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 29.
     * </pre>
     */
    @ApiOperation(value = "새로운 모델 설정", notes = "새로운 모델을 설정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "새로운 모델 설정 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/toggle-new")
    public ResponseEntity<AdminModelDTO> toggleModelNewYn(@PathVariable Long idx) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminModelJpaService.toggleModelNewYn(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 03.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 조회", notes = "모델 스케줄을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 조회성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/schedule")
    public ResponseEntity<List<AdminScheduleDTO>> findOneModelSchedule(@PathVariable Long idx) {
        if (adminModelJpaService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adminModelJpaService.findOneModelSchedule(idx));
    }
}
