package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.mapper.UserMapper;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.common.StringUtils;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.user.QAdminUserEntity.adminUserEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminUserJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
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
    public AdminUserEntity findOneUser(String id) {
        return queryFactory.selectFrom(adminUserEntity)
                .where(adminUserEntity.userId.eq(id))
                .fetchOne();
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaRepository.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    public String adminLogin(AdminUserEntity existAdminUserEntity) {

        Map<String, Object> userMap = new HashMap<>();

        try {
            final String db_pw = StringUtils.nullStrToStr(findOneUser(existAdminUserEntity.getUserId()).getPassword());
            String result;

            if (passwordEncoder.matches(existAdminUserEntity.getPassword(), db_pw)) {
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
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Modifying
    @Transactional
    public Integer insertUserTokenByEm(AdminUserEntity adminUserEntity) {
        try {
            AdminUserEntity adminUser = em.find(AdminUserEntity.class, adminUserEntity.getIdx());
            adminUser.setUserToken(adminUserEntity.getUserToken());
            em.flush();
            em.clear();

            return adminUser.getIdx();
        } catch (Exception e) {
            e.printStackTrace();
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
    @Modifying
    @Transactional
    public Integer insertUserToken(AdminUserEntity existAdminUserEntity) {

        try {
            JPAUpdateClause update = new JPAUpdateClause(em, adminUserEntity);

            update.set(adminUserEntity.userToken, existAdminUserEntity.getUserToken())
                    .set(adminUserEntity.updater, 1)
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
            em.persist(adminUserEntity);

            return UserMapper.INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_USER);
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
    @Transactional
    public AdminUserDTO deleteAdminUser(AdminUserEntity adminUserEntity) {
        try {
            adminUserEntity = em.find(AdminUserEntity.class, adminUserEntity.getIdx());
            em.remove(adminUserEntity);
            em.flush();
            em.clear();

            return UserMapper.INSTANCE.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_MODEL);
        }
    }
}
