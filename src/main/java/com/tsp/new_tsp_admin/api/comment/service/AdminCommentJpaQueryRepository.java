package com.tsp.new_tsp_admin.api.comment.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity.adminCommentEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_COMMENT;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminCommentJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchComment(Map<String, Object> commentMap) {
        String searchKeyword = getString(commentMap.get("searchKeyword"), "");
        return adminCommentEntity.comment.contains(searchKeyword);
    }

    /**
     * <pre>
     * 1. MethodName : findAdminCommentCount
     * 2. ClassName  : AdminCommentJpaRepository.java
     * 3. Comment    : 관리자 어드민 코멘트 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public int findAdminCommentCount(Map<String, Object> commentMap) {
        return queryFactory.selectFrom(adminCommentEntity).where(searchComment(commentMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaRepository.java
     * 3. Comment    : 관리자 어드민 코멘트 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public List<AdminCommentDTO> findAdminCommentList(Map<String, Object> commentMap) {
        List<AdminCommentEntity> commentList = queryFactory
                .selectFrom(adminCommentEntity)
                .orderBy(adminCommentEntity.idx.desc())
                .where(searchComment(commentMap))
                .offset(getInt(commentMap.get("jpaStartPage"), 0))
                .limit(getInt(commentMap.get("size"), 0))
                .fetch();

        return commentList != null ? toDtoList(commentList) : emptyList();
    }
}
