package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.*;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaQueryRepository;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import com.tsp.new_tsp_admin.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserJpaServiceImpl implements AdminUserJpaService {
    private final AdminUserJpaQueryRepository adminUserJpaQueryRepository;
    private final AdminUserJpaRepository adminUserJpaRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private AdminUserEntity oneUser(Long idx) {
        return adminUserJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_USER));
    }

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminUserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest) {
        return adminUserJpaQueryRepository.findUserList(userMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminUserDTO findOneUser(String id) {
        AdminUserEntity oneUser = adminUserJpaRepository.findByUserId(id)
                .orElseThrow(() -> new TspException(NOT_FOUND_USER));
        return AdminUserEntity.toDto(oneUser);
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional
    public JwtUtil.TokenInfo adminLogin(LoginRequest loginRequest) {
        // 패스워드 일치할 시
        if (passwordEncoder.matches(loginRequest.getPassword(), findOneUser(loginRequest.getUserId()).getPassword())) {
            Authentication authentication = authenticate(loginRequest.getUserId(), loginRequest.getPassword());
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof AuthenticationRequest) {
                    AuthenticationRequest principalDetails = (AuthenticationRequest) principal;
                    AdminUserEntity user = principalDetails.getAdminUserEntity();
                    // accessToken
                    String accessToken = jwtUtil.doGenerateToken(principalDetails.getUsername());
                    user.updateToken(accessToken);
                    // refreshToken
                    String refreshToken = jwtUtil.doGenerateRefreshToken(principalDetails.getUsername());
                    user.updateRefreshToken(refreshToken);

                    return jwtUtil.getJwtTokens(accessToken, refreshToken);
                }
            }
        }
        return null;
    }

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @Override
    @Transactional
    public AdminUserDTO insertAdminUser(SignUpRequest signUpRequest) {
        try {
            if (adminUserJpaRepository.findByUserId(signUpRequest.getUserId()).isPresent()) {
                throw new TspException(EXIST_USER);
            }

            return AdminUserEntity.toDto(adminUserJpaRepository.save(AdminUserEntity.builder()
                    .userId(signUpRequest.getUserId())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .name(signUpRequest.getName())
                    .email(signUpRequest.getEmail())
                    .role(Role.ROLE_ADMIN)
                    .visible("Y")
                    .build()));

        } catch (Exception e) {
            throw new TspException(ERROR_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @Override
    @Transactional
    public AdminUserDTO updateAdminUser(Long idx, AdminUserEntity adminUserEntity) {
        try {
            oneUser(idx).update(adminUserEntity);
            return AdminUserEntity.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @Override
    @Transactional
    public void deleteAdminUser(AdminUserEntity adminUserEntity) {
        try {
            adminUserJpaRepository.delete(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_USER);
        }
    }

    private Authentication authenticate(String userId, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("BadCredentialsException");
        } catch(DisabledException e) {
            throw new DisabledException("DisabledException");
        } catch(LockedException e) {
            throw new LockedException("LockedException");
        } catch(UsernameNotFoundException e) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        } catch(AuthenticationException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
