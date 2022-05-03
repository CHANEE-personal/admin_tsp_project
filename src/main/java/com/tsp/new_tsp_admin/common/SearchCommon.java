package com.tsp.new_tsp_admin.common;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchCommon {

    /**
     * <pre>
     * 1. MethodName : searchCommon
     * 2. ClassName  : SearchCommon.java
     * 3. Comment    : 페이징 및 검색 조건 공통
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 08. 08.
     * </pre>
     *
     * @param page
     * @param paramMap
     * @return ConcurrentHashMap
     * @throws Exception
     */
    public ConcurrentHashMap<String, Object> searchCommon(Page page, Map<String, Object> paramMap) {

        ConcurrentHashMap<String, Object> searchMap = new ConcurrentHashMap<>();

        // 페이징 처리
        Integer pageCnt = StringUtil.getInt(page.getPage(), 1);
        Integer pageSize = StringUtil.getInt(page.getSize(), 10);
        page.setPage(pageCnt);
        page.setSize(pageSize);

        // 검색 조건
        searchMap.put("searchType", StringUtil.getString(paramMap.get("searchType"), ""));
        searchMap.put("searchKeyword", StringUtil.getString(paramMap.get("searchKeyword"), ""));
        searchMap.put("jpaStartPage", StringUtil.getInt(page.getStartPage(), 0));
        searchMap.put("startPage", pageCnt);
        searchMap.put("size", pageSize);

        return searchMap;
    }

    /**
     * <pre>
     * 1. MethodName : giveAuth
     * 2. ClassName  : SearchCommon.java
     * 3. Comment    : jwt 인증자
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 08. 08.
     * </pre>
     *
     * @param request
     * @return ConcurrentHashMap
     * @throws Exception
     */
    public void giveAuth(HttpServletRequest request, NewCommonDTO newCommonDTO) throws Exception {
        // creator, updater 공통 DTO

        // JWT token 값 존재 시 유저 인증 값 부여
        if (request.getHeader("Authorization") != null) {
//            String userSeq = adminUserMapper.selectAdminSeq(StringUtil.getString(request.getHeader("Authorization"), ""));
//            newCommonDTO.setCreator(StringUtil.getInt(userSeq, 0));
//            newCommonDTO.setUpdater(StringUtil.getInt(userSeq, 0));
        }
    }
}