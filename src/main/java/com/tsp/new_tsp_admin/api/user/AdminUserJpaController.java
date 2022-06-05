package com.tsp.new_tsp_admin.api.user;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.user.service.AdminUserJpaService;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import com.tsp.new_tsp_admin.api.jwt.AuthenticationResponse;
import com.tsp.new_tsp_admin.api.jwt.JwtUtil;
import com.tsp.new_tsp_admin.api.jwt.MyUserDetailsService;
import com.tsp.new_tsp_admin.common.Page;
import com.tsp.new_tsp_admin.common.SearchCommon;
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

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;

@RestController
@RequestMapping("/api/jpa-user")
@RequiredArgsConstructor
public class AdminUserJpaController {

    private final AdminUserJpaService adminUserJpaService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : userEntityList
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @ApiOperation(value = "Admin 회원 조회", notes = "Admin 회원을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public List<AdminUserDTO> userEntityList(@RequestParam(required = false) Map<String, Object> paramMap,
                                             Page page) throws Exception {

        // 페이징 및 검색
        ConcurrentHashMap<String, Object> userMap = searchCommon.searchCommon(page, paramMap);

        return adminUserJpaService.getAdminUserList(userMap);
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
     */
    @ApiOperation(value = "Admin 회원 로그인 처리", notes = "Admin 회원을 로그인 처리한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/login")
    public ConcurrentHashMap<String, Object> login(@RequestBody AuthenticationRequest authenticationRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {

        ConcurrentHashMap<String, Object> userMap = new ConcurrentHashMap<>();

        AdminUserEntity adminUserEntity = builder()
                        .userId(authenticationRequest.getUserId())
                        .password(authenticationRequest.getPassword())
                        .build();

        String resultValue = adminUserJpaService.adminLogin(adminUserEntity);

        if ("Y".equals(resultValue)) {
            userMap.put("loginYn", resultValue);
            userMap.put("userId", adminUserEntity.getUserId());
            userMap.put("token", createAuthenticationToken(authenticationRequest));

            // 로그인 완료 시 생성된 token 값 DB에 저장
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
            String token = jwtTokenUtil.generateToken(userDetails);
            adminUserEntity.setUserToken(token);

            adminUserJpaService.insertToken(adminUserEntity);
        }

        return userMap;
    }

    /**
     * <pre>
     * 1. MethodName : createAuthenticationToken
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 로그인 시 JWT 토큰 발급
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 04. 23.
     * </pre>
     *
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
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 로그인 시 인증
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 04. 23.
     * </pre>
     *
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

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 로그인 회원가입
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @ApiOperation(value = "Admin 회원가입 처리", notes = "Admin 회원가입을 처리한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public AdminUserDTO insertAdminUser(@RequestBody AdminUserEntity adminUserEntity) throws Exception {
        return adminUserJpaService.insertAdminUser(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @ApiOperation(value = "Admin 회원탈퇴 처리", notes = "Admin 회원탈퇴 처리한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping
    public AdminUserDTO deleteAdminUser(@RequestBody AdminUserEntity adminUserEntity) throws Exception {
        return adminUserJpaService.deleteAdminUser(adminUserEntity);
    }
}
