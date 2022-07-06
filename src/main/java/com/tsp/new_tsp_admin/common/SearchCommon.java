package com.tsp.new_tsp_admin.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
     */
    public Map<String, Object> searchCommon(Page page, Map<String, Object> paramMap) {

        Map<String, Object> searchMap = new HashMap<>();

        // 페이징 처리
        page.setPage(StringUtil.getInt(page.getPage(), 1));
        page.setSize(StringUtil.getInt(page.getSize(), 10));

        // 검색 조건
        searchMap.put("searchType", StringUtil.getString(paramMap.get("searchType"), ""));
        searchMap.put("searchKeyword", StringUtil.getString(paramMap.get("searchKeyword"), ""));
        searchMap.put("jpaStartPage", StringUtil.getInt(page.getStartPage(), 0));
        searchMap.put("startPage", StringUtil.getInt(page.getPage(), 1));
        searchMap.put("size", StringUtil.getInt(page.getSize(), 10));

        return searchMap;
    }
}