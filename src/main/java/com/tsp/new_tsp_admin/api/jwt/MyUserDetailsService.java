package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AdminUserJpaRepository adminUserJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        try {
            AdminUserEntity adminUserEntity = adminUserJpaRepository.findOneUser(id);
            adminUserEntity.setUserRefreshToken(adminUserEntity.getUserToken());
            adminUserJpaRepository.insertUserTokenByEm(adminUserEntity);

            // 아이디 일치하는지 확인
            return new User(adminUserEntity.getName(), adminUserEntity.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TspException(ApiExceptionType.NOT_FOUND_USER);
        }
    }
}
