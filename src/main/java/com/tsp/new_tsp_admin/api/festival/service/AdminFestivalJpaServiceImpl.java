package com.tsp.new_tsp_admin.api.festival.service;

import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalDTO;
import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminFestivalJpaServiceImpl implements AdminFestivalJpaService {

    private final AdminFestivalJpaQueryRepository adminFestivalJpaQueryRepository;
    private final AdminFestivalJpaRepository adminFestivalJpaRepository;

    private AdminFestivalEntity oneFestival(Long idx) {
        return adminFestivalJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_FESTIVAL));
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
    public Page<AdminFestivalDTO> findFestivalList(Map<String, Object> festivalMap, PageRequest pageRequest) {
        return adminFestivalJpaQueryRepository.findFestivalList(festivalMap, pageRequest);
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
        return AdminFestivalEntity.toDto(oneFestival(idx));
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
            return AdminFestivalEntity.toDto(adminFestivalJpaRepository.save(adminFestivalEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_FESTIVAL);
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
    public AdminFestivalDTO updateFestival(Long idx, AdminFestivalEntity adminFestivalEntity) {
        try {
            Optional.ofNullable(oneFestival(idx))
                    .ifPresent(adminFestival -> adminFestival.update(adminFestivalEntity));
            return AdminFestivalEntity.toDto(adminFestivalEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_FESTIVAL);
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
            adminFestivalJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_FESTIVAL);
        }
    }
}
