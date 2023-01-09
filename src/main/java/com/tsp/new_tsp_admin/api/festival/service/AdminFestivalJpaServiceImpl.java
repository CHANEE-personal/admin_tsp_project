package com.tsp.new_tsp_admin.api.festival.service;

import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalDTO;
import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminFestivalJpaServiceImpl implements AdminFestivalJpaService {

    private final AdminFestivalJpaRepository adminFestivalJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findFestivalCount
     * 2. ClassName  : AdminFestivalJpaService.java
     * 3. Comment    : 관리자 행사 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public int findFestivalCount(Map<String, Object> festivalMap) {
        return adminFestivalJpaRepository.findFestivalCount(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : AdminFestivalJpaService.java
     * 3. Comment    : 관리자 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public List<AdminFestivalDTO> findFestivalList(Map<String, Object> festivalMap) {
        return adminFestivalJpaRepository.findFestivalList(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFestival
     * 2. ClassName  : AdminFestivalJpaService.java
     * 3. Comment    : 관리자 행사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public AdminFestivalDTO findOneFestival(Long idx) {
        return adminFestivalJpaRepository.findOneFestival(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertFestival
     * 2. ClassName  : AdminFestivalJpaService.java
     * 3. Comment    : 관리자 행사 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public AdminFestivalDTO insertFestival(AdminFestivalEntity adminFestivalEntity) {
        try {
            return adminFestivalJpaRepository.changeFestival(adminFestivalEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_FESTIVAL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateFestival
     * 2. ClassName  : AdminFestivalJpaService.java
     * 3. Comment    : 관리자 행사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public AdminFestivalDTO updateFestival(AdminFestivalEntity adminFestivalEntity) {
        try {
            return adminFestivalJpaRepository.changeFestival(adminFestivalEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_FESTIVAL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteFestival
     * 2. ClassName  : AdminFestivalJpaService.java
     * 3. Comment    : 관리자 행사 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public Long deleteFestival(Long idx) {
        try {
            return adminFestivalJpaRepository.deleteFestival(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_FESTIVAL, e);
        }
    }
}
