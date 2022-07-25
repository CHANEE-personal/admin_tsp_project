package com.tsp.new_tsp_admin.jwt;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.AdminUserJpaService;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_USER;
import static org.springframework.security.core.authority.AuthorityUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final AdminUserJpaService adminUserJpaService;
    private final AdminUserJpaRepository adminUserJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        try {
            AdminUserEntity adminUserEntity = adminUserJpaService.findOneUser(id);
            adminUserEntity.setUserRefreshToken(adminUserEntity.getUserToken());
            adminUserJpaRepository.insertUserTokenByEm(adminUserEntity);

            // 아이디 일치하는지 확인
            return new User(adminUserEntity.getName(), adminUserEntity.getPassword(), createAuthorityList("ROLE_ADMIN"));
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER, e);
        }
    }
}
