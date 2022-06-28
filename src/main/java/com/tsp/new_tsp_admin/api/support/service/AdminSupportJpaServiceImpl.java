package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminSupportJpaServiceImpl implements AdminSupportJpaService {

    private final AdminSupportJpaRepository adminSupportJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    public int findSupportsCount(Map<String, Object> supportMap) {
        return adminSupportJpaRepository.findSupportsCount(supportMap);
    }

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    public List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) {
        return adminSupportJpaRepository.findSupportsList(supportMap);
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    public AdminSupportDTO updateSupportModel(AdminSupportEntity adminSupportEntity) {
        return adminSupportJpaRepository.updateSupportModel(adminSupportEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    public AdminSupportDTO deleteSupportModel(AdminSupportEntity adminSupportEntity) {
        return adminSupportJpaRepository.deleteSupportModel(adminSupportEntity);
    }
}
