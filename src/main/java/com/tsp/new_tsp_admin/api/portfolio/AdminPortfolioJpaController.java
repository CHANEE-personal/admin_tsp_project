package com.tsp.new_tsp_admin.api.portfolio;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.portfolio.service.AdminPortfolioJpaService;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/jpa-portfolio")
@Api(tags = "포트폴리오관련 API")
@RequiredArgsConstructor
public class AdminPortfolioJpaController {

    private final AdminPortfolioJpaService adminPortfolioJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getPortfolioList
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     *
     * @param page
     */
    @ApiOperation(value = "포트폴리오 조회", notes = "포트폴리오를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ConcurrentHashMap<String, Object> getPortfolioList(Page page, @RequestParam(required = false) Map<String, Object> paramMap) {
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();

        ConcurrentHashMap<String, Object> searchMap = searchCommon.searchCommon(page, paramMap);

        Long portfolioCnt = this.adminPortfolioJpaService.findPortfoliosCount(searchMap);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();

        if(portfolioCnt > 0) {
            portfolioList = this.adminPortfolioJpaService.findPortfoliosList(searchMap);
        }

        // 리스트 수
        portfolioMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        portfolioMap.put("perPageListCnt", Math.ceil((portfolioCnt - 1) / page.getSize() + 1));
        // 전체 아이템 수
        portfolioMap.put("portfolioListCnt", portfolioCnt);

        portfolioMap.put("portfolioList", portfolioList);

        return portfolioMap;
    }

    /**
     * <pre>
     * 1. MethodName : getPortfolioEdit
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     * @param idx
     */
    @ApiOperation(value = "포트폴리오 상세 조회", notes = "포트폴리오를 상세 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public AdminPortFolioDTO getPortfolioEdit(@PathVariable("idx") Integer idx) {
        AdminPortFolioEntity adminPortFolioEntity = AdminPortFolioEntity.builder().idx(idx).build();

        return this.adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 draft 상태로 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @ApiOperation(value = "포트폴리오 저장", notes = "포트폴리오를 저장한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "포트폴리오 등록성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public Integer insertPortfolio(@RequestBody AdminPortFolioEntity adminPortFolioEntity) throws Exception {
        if (this.adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity) > 0) {
            return this.adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);
        } else {
            return 0;
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @ApiOperation(value = "포트폴리오 수정", notes = "포트폴리오를 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "포트폴리오 수정성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminPortFolioEntity updatePortfolio(@RequestBody AdminPortFolioEntity adminPortFolioEntity,
                                                @PathVariable("idx") Integer idx) throws Exception {
        return adminPortfolioJpaService.updatePortfolio(adminPortFolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @ApiOperation(value = "포트폴리오 삭제", notes = "포트폴리오를 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "포트폴리오 삭제성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public AdminPortFolioEntity deletePortfolio(@RequestBody AdminPortFolioEntity adminPortFolioEntity,
                                                @PathVariable("idx") Integer idx) throws Exception {
        adminPortFolioEntity.setIdx(idx);
        return adminPortfolioJpaService.deletePortfolio(adminPortFolioEntity);
    }
}
