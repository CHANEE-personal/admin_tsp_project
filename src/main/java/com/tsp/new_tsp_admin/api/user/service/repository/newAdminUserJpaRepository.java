package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.QAdminUserEntity;
import com.tsp.new_tsp_admin.api.user.mapper.UserMapper;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.user.QAdminUserEntity.adminUserEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class newAdminUserJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     * @param userMap
     */
    public List<AdminUserDTO> findUserList(Map<String, Object> userMap) {

        try {
            List<AdminUserEntity> userList = queryFactory.selectFrom(adminUserEntity)
                    .where(adminUserEntity.visible.eq("Y"))
                    .orderBy(adminUserEntity.idx.desc())
                    .offset(StringUtil.getInt(userMap.get("jpaStartPage"), 0))
                    .limit(StringUtil.getInt(userMap.get("size"), 0))
                    .fetch();

            List<AdminUserDTO> userDtoList = UserMapper.INSTANCE.toDtoList(userList);

            for (int i = 0; i < userDtoList.size(); i++) {
                userDtoList.get(i).setRnum(StringUtil.getInt(userMap.get("startPage"), 1) * (StringUtil.getInt(userMap.get("size"), 1)) - (2 - i));
            }

            return userDtoList;
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_USER_LIST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     * @param existAdminUserEntity
     */
    public Map<String, Object> adminLogin(AdminUserEntity existAdminUserEntity) {

        Map<String, Object> userMap = new HashMap<>();

        try {
            AdminUserEntity NewAdminUserEntity = queryFactory.selectFrom(adminUserEntity)
                    .where(adminUserEntity.visible.eq("Y"), adminUserEntity.userId.eq(existAdminUserEntity.getUserId()))
                    .fetchOne();

            if (NewAdminUserEntity == null) {
                return null;
            }

            userMap.put("userId", NewAdminUserEntity.getUserId());
            userMap.put("password", NewAdminUserEntity.getPassword());

            return userMap;

        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertUserToken
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 회원 로그인 후 토큰 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     * @param existAdminUserEntity
     */
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
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 회원가입 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     * @param adminUserEntity
     */
    public Integer insertAdminUser(AdminUserEntity adminUserEntity) {

        try {
            //회원 등록
            em.persist(adminUserEntity);
            em.flush();
            em.clear();

            //회원 등록된 IDX
            AdminUserEntity newAdminUserEntity = em.find(AdminUserEntity.class, adminUserEntity.getIdx());
            Integer newIdx = newAdminUserEntity.getIdx();

            em.flush();
            em.close();

            return newIdx;
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_USER);
        }
    }
}
