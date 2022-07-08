package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.common.StringUtils;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.tsp.new_tsp_admin.api.domain.user.QAdminUserEntity.adminUserEntity;
import static com.tsp.new_tsp_admin.api.user.mapper.UserMapper.INSTANCE;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_USER;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_USER_LIST;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminUserJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public List<AdminUserDTO> findUserList(Map<String, Object> userMap) throws TspException {
        try {
            List<AdminUserEntity> userList = queryFactory.selectFrom(adminUserEntity)
                    .where(adminUserEntity.visible.eq("Y"))
                    .orderBy(adminUserEntity.idx.desc())
                    .offset(getInt(userMap.get("jpaStartPage"), 0))
                    .limit(getInt(userMap.get("size"), 0))
                    .fetch();

            userList.forEach(list -> userList.get(userList.indexOf(list))
                    .setRnum(getInt(userMap.get("startPage"), 1)*(getInt(userMap.get("size"),1))-(2-userList.indexOf(list))));

            return INSTANCE.toDtoList(userList);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 유저 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    public AdminUserEntity findOneUser(String id) throws TspException {
        try {
            return queryFactory.selectFrom(adminUserEntity)
                    .where(adminUserEntity.userId.eq(id))
                    .fetchOne();
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 토큰을 이용한 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public String findOneUserByToken(String token) {
        return Objects.requireNonNull(queryFactory.selectFrom(adminUserEntity)
                .where(adminUserEntity.userToken.eq(token))
                .fetchOne()).getUserId();
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    public String adminLogin(AdminUserEntity existAdminUserEntity) throws TspException {
        try {
            final String db_pw = StringUtils.nullStrToStr(findOneUser(existAdminUserEntity.getUserId()).getPassword());
            String result;

            if (passwordEncoder.matches(existAdminUserEntity.getPassword(), db_pw)) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(existAdminUserEntity.getUserId(), existAdminUserEntity.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                result = "Y";
            } else {
                result = "N";
            }
            return result;
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertUserTokenByEm
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 회원 로그인 후 토큰 등록 By EntityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer insertUserTokenByEm(AdminUserEntity adminUserEntity) throws TspException {
        try {
            em.merge(adminUserEntity);
            em.flush();
            em.clear();

            return adminUserEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ERROR_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertUserToken
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 회원 로그인 후 토큰 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer insertUserToken(AdminUserEntity existAdminUserEntity) throws TspException {

        try {
            JPAUpdateClause update = new JPAUpdateClause(em, adminUserEntity);

            update.set(adminUserEntity.userToken, existAdminUserEntity.getUserToken())
                  .set(adminUserEntity.updater, "1")
                  .set(adminUserEntity.updateTime, new Date())
                  .where(adminUserEntity.userId.eq(existAdminUserEntity.getUserId())).execute();

            return existAdminUserEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ERROR_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 회원가입 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminUserDTO insertAdminUser(AdminUserEntity adminUserEntity) throws TspException {
        try {
            //회원 등록
            em.persist(adminUserEntity);

            return INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 회원 수정 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminUserDTO updateAdminUser(AdminUserEntity adminUserEntity) throws TspException {
        try {
            em.merge(adminUserEntity);
            em.flush();
            em.clear();

            return INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteAdminUser(Integer idx) throws TspException {
        try {
            em.remove(em.find(AdminUserEntity.class, idx));
            em.flush();
            em.clear();

            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_USER, e);
        }
    }
}
