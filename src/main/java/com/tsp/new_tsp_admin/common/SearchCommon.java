package com.tsp.new_tsp_admin.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;

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
     */
    public Map<String, Object> searchCommon(Paging paging, Map<String, Object> paramMap) {

        Map<String, Object> searchMap = new HashMap<>();

        // 페이징 처리
        paging.setPage(getInt(paging.getPage(), 1));
        paging.setSize(getInt(paging.getSize(), 10));

        // 검색 조건
        if (paramMap.get("searchType") != null && paramMap.get("searchKeyword") != null) {
            searchMap.put("searchType", getString(paramMap.get("searchType"), ""));
            searchMap.put("searchKeyword", getString(paramMap.get("searchKeyword"), ""));
        }
        searchMap.put("jpaStartPage", getInt(paging.getStartPage(), 0));
        searchMap.put("startPage", getInt(paging.getPage(), 1));
        searchMap.put("size", getInt(paging.getSize(), 10));

        return searchMap;
    }
}