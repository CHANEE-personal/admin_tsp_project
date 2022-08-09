package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_COMMON_LIST;

@Service
@RequiredArgsConstructor
public class AdminCommonJpaServiceImpl implements AdminCommonJpaService {

    private final AdminCommonJpaRepository adminCommonJpaRepository;

    /**
     * <pre>
     * 1. MethodName : commonCodeListCount
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    public Integer commonCodeListCount(Map<String, Object> commonMap) throws TspException {
        try {
            return adminCommonJpaRepository.commonCodeListCount(commonMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMON_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : commonCodeList
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommonCodeDTO> commonCodeList(Map<String, Object> commonMap) throws TspException {
        try {
            return adminCommonJpaRepository.commonCodeList(commonMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMON_LIST, e);
        }
    }
}
