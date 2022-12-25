package com.tsp.new_tsp_admin.api.comment.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;

import java.util.List;
import java.util.Map;

public interface AdminCommentJpaService {

    /**
     * <pre>
     * 1. MethodName : findAdminCommentCount
     * 2. ClassName  : AdminCommentJpaService.java
     * 3. Comment    : 관리자 코멘트 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    int findAdminCommentCount(Map<String, Object> commentMap);

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaService.java
     * 3. Comment    : 관리자 코멘트 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    List<AdminCommentDTO> findAdminCommentList(Map<String, Object> commentMap);

    /**
     * <pre>
     * 1. MethodName : findOneAdminComment
     * 2. ClassName  : AdminCommentJpaService.java
     * 3. Comment    : 관리자 코멘트 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    AdminCommentDTO findOneAdminComment(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertAdminComment
     * 2. ClassName  : AdminCommentJpaService.java
     * 3. Comment    : 관리자 코멘트 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    AdminCommentDTO insertAdminComment(AdminCommentEntity adminCommentEntity);

    /**
     * <pre>
     * 1. MethodName : updateAdminComment
     * 2. ClassName  : AdminCommentJpaService.java
     * 3. Comment    : 관리자 코멘트 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    AdminCommentDTO updateAdminComment(AdminCommentEntity adminCommentEntity);

    /**
     * <pre>
     * 1. MethodName : deleteAdminComment
     * 2. ClassName  : AdminCommentJpaService.java
     * 3. Comment    : 관리자 코멘트 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    Long deleteAdminComment(Long idx);
}
