package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.Role;
import com.tsp.new_tsp_admin.api.user.mapper.UserMapper;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.common.StringUtils;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
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
    public List<AdminUserDTO> findUserList(Map<String, Object> userMap) {

        try {
            List<AdminUserEntity> userList = queryFactory.selectFrom(adminUserEntity)
                    .where(adminUserEntity.visible.eq("Y"))
                    .orderBy(adminUserEntity.idx.desc())
                    .offset(StringUtil.getInt(userMap.get("jpaStartPage"), 0))
                    .limit(StringUtil.getInt(userMap.get("size"), 0))
                    .fetch();

            userList.forEach(list -> userList.get(userList.indexOf(list)).setRnum(StringUtil.getInt(userMap.get("startPage"), 1)*(StringUtil.getInt(userMap.get("size"),1))-(2-userList.indexOf(list))));

            return UserMapper.INSTANCE.toDtoList(userList);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_USER_LIST);
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
    @Transactional(readOnly = true)
    public AdminUserEntity findOneUser(String id) {
        try {
            return queryFactory.selectFrom(adminUserEntity)
                    .where(adminUserEntity.userId.eq(id))
                    .fetchOne();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_USER);
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
    public String adminLogin(AdminUserEntity existAdminUserEntity) {

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
            throw new TspException(ApiExceptionType.NOT_FOUND_USER);
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
    @Transactional
    public Integer insertUserTokenByEm(AdminUserEntity adminUserEntity) {
        try {
            AdminUserEntity adminUser = em.find(AdminUserEntity.class, adminUserEntity.getIdx());
            em.flush();
            em.clear();

            return adminUser.getIdx();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_USER);
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
    public Integer insertUserToken(AdminUserEntity existAdminUserEntity) {

        try {
            JPAUpdateClause update = new JPAUpdateClause(em, adminUserEntity);

            update.set(adminUserEntity.userToken, existAdminUserEntity.getUserToken())
                    .set(adminUserEntity.updater, "1")
                    .set(adminUserEntity.updateTime, new Date())
                    .where(adminUserEntity.userId.eq(existAdminUserEntity.getUserId())).execute();

            return existAdminUserEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_USER);
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
    @Transactional
    public AdminUserDTO insertAdminUser(AdminUserEntity adminUserEntity) {

        try {
            //회원 등록
            adminUserEntity.setRole(Role.ROLE_ADMIN);
            em.persist(adminUserEntity);

            return UserMapper.INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_USER);
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
    public AdminUserDTO updateAdminUser(AdminUserEntity adminUserEntity) {
        try {
            em.merge(adminUserEntity);
            em.flush();
            em.clear();

            return UserMapper.INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_UPDATE_USER);
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
    public AdminUserDTO deleteAdminUser(AdminUserEntity adminUserEntity) {
        try {
            adminUserEntity = em.find(AdminUserEntity.class, adminUserEntity.getIdx());
            em.remove(adminUserEntity);
            em.flush();
            em.clear();

            return UserMapper.INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_USER);
        }
    }
}
