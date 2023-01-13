package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.user.QAdminUserEntity.adminUserEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtils.nullStrToStr;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_USER;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminUserJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 유저 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public List<AdminUserDTO> findUserList(Map<String, Object> userMap) {
        List<AdminUserEntity> userList = queryFactory.selectFrom(adminUserEntity)
                .where(adminUserEntity.visible.eq("Y"))
                .orderBy(adminUserEntity.idx.desc())
                .offset(getInt(userMap.get("jpaStartPage"), 0))
                .limit(getInt(userMap.get("size"), 0))
                .fetch();

        return userList != null ? toDtoList(userList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 유저 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    public AdminUserEntity findOneUser(String id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(adminUserEntity)
                .where(adminUserEntity.userId.eq(id))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_USER));
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 토큰을 이용한 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    public String findOneUserByToken(String token) {
        return requireNonNull(queryFactory.selectFrom(adminUserEntity)
                .where(adminUserEntity.userToken.eq(token))
                .fetchOne()).getUserId();
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public String adminLogin(AdminUserEntity existAdminUserEntity) {
        final String db_pw = nullStrToStr(findOneUser(existAdminUserEntity.getUserId()).getPassword());
        String result;

        if (passwordEncoder.matches(existAdminUserEntity.getPassword(), db_pw)) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(existAdminUserEntity.getUserId(), existAdminUserEntity.getPassword())
            );
            getContext().setAuthentication(authentication);
            result = "Y";
        } else {
            result = "N";
        }
        return result;
    }

    /**
     * <pre>
     * 1. MethodName : insertUserTokenByEm
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 회원 로그인 후 토큰 등록 By EntityManager
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Long insertUserTokenByEm(AdminUserEntity adminUserEntity) {
        em.merge(adminUserEntity);
        return adminUserEntity.getIdx();
    }

    /**
     * <pre>
     * 1. MethodName : insertUserToken
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 회원 로그인 후 토큰 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    public Long insertUserToken(AdminUserEntity existAdminUserEntity) {
        JPAUpdateClause update = new JPAUpdateClause(em, adminUserEntity);

        update.set(adminUserEntity.userToken, existAdminUserEntity.getUserToken())
                .set(adminUserEntity.updater, "1")
                .set(adminUserEntity.updateTime, LocalDateTime.now())
                .where(adminUserEntity.userId.eq(existAdminUserEntity.getUserId())).execute();

        return existAdminUserEntity.getIdx();
    }
}
