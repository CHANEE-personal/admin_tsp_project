package com.tsp.new_tsp_admin.api.user;

import com.tsp.new_tsp_admin.api.user.service.AdminUserJpaService;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import com.tsp.new_tsp_admin.api.jwt.AuthenticationResponse;
import com.tsp.new_tsp_admin.api.jwt.JwtUtil;
import com.tsp.new_tsp_admin.api.jwt.MyUserDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.rmi.ServerError;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/jpa-user")
@RequiredArgsConstructor
public class AdminUserJpaController {

    private final AdminUserJpaService adminUserJpaService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    /**
     * <pre>
     * 1. MethodName : userEntityList
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Admin 회원 조회", notes = "Admin 회원을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public List<AdminUserEntity> userEntityList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return adminUserJpaService.getAdminUserList();
    }

    /**
     * <pre>
     * 1. MethodName : login
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Admin 회원 로그인 처리", notes = "Admin 회원을 로그인 처리한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/login")
    public ConcurrentHashMap<String, Object> login(@RequestBody AuthenticationRequest authenticationRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {

        ConcurrentHashMap<String, Object> userMap = new ConcurrentHashMap<>();

        AdminUserEntity adminUserEntity = new AdminUserEntity();

        adminUserEntity.setUserId(authenticationRequest.getUserId());
        adminUserEntity.setPassword(authenticationRequest.getPassword());
        String resultValue = adminUserJpaService.adminLogin(adminUserEntity);

        if ("Y".equals(resultValue)) {
            userMap.put("loginYn", resultValue);
            userMap.put("userId", adminUserEntity.getUserId());
            userMap.put("token", createAuthenticationToken(authenticationRequest));

            // 로그인 완료 시 생성된 token 값 DB에 저장
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
            String token = jwtTokenUtil.generateToken(userDetails);

            adminUserJpaService.saveToken(adminUserEntity.getUserId(),token);
//            adminUserApiService.insertUserToken(newUserDTO);
        }

        return userMap;
    }

    /**
     * <pre>
     * 1. MethodName : createAuthenticationToken
     * 2. ClassName  : AdminLoginApi.java
     * 3. Comment    : 관리자 로그인 시 JWT 토큰 발급
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 04. 23.
     * </pre>
     *
     * @param  authenticationRequest
     * @throws Exception
     */
    @ApiIgnore
    @ApiOperation(value = "JWT 토근 발급", notes = "JWT 토근 발급")
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        // id, password 인증
        authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

        // 사용자 정보 조회 후 token 생성
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    /**
     * <pre>
     * 1. MethodName : authenticate
     * 2. ClassName  : AdminLoginApi.java
     * 3. Comment    : 관리자 로그인 시 인증
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 04. 23.
     * </pre>
     *
     * @param  id
     * @param  password
     * @throws Exception
     */
    private void authenticate(String id, String password) throws Exception {
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(id, password);
            if (request.getName().equals(request.getCredentials())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getCredentials()));
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}