package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.common.StringUtils;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserJpaServiceImpl implements AdminUserJpaService {

    private final AdminUserJpaRepository adminUserJpaRepository;
    private final PasswordEncoder passwordEncoder;

//    @Override
//    public void signUpUser(AdminUserEntity adminUserEntity) {
//        String password = adminUserEntity.getPassword();
//    }


    @Override
    public List<AdminUserEntity> getAdminUserList() {
        return adminUserJpaRepository.findAll();
    }

    @Override
    public String adminLogin(AdminUserEntity adminUserEntity) {

        try {
            final String db_pw = StringUtils.nullStrToStr(adminUserJpaRepository.findAdminUserEntityByPassword(adminUserEntity.getUserId()));

            String result;

            if (passwordEncoder.matches(adminUserEntity.getPassword(), db_pw)) {
                result = "Y";
            } else {
                result = "N";
            }
            return result;
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_USER);
        }
    }

    @Override
    public void saveToken(String userId, String token) {
        AdminUserEntity adminUserEntity = adminUserJpaRepository.findAdminUserEntityByUserId(userId);
        adminUserEntity.setUserToken(token);
        adminUserJpaRepository.save(adminUserEntity);
    }
}
