package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.User.Service.Repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.api.domain.User.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AdminUserJpaRepository adminUserJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        AdminUserEntity adminUserEntity = null;
        try {
            adminUserEntity = adminUserJpaRepository.findAdminUserEntityByUserId(id);

            // 아이디 일치하는지 확인
            return new User(adminUserEntity.getUsername(),
                   adminUserEntity.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminUserEntity;
    }
}
