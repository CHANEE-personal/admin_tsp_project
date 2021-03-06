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
import io.swagger.annotations.*;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.rmi.ServerError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;
import static org.springframework.web.client.HttpClientErrorException.*;

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
     * 1. MethodName : findUsersList
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "Admin ?????? ??????", notes = "Admin ????????? ????????????.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "??????", response = Map.class),
            @ApiResponse(code = 400, message = "????????? ??????", response = BadRequest.class),
            @ApiResponse(code = 401, message = "???????????? ?????? ?????????", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "????????????", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "?????? ??????", response = ServerError.class)
    })
    @GetMapping
    public List<AdminUserDTO> findUsersList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) throws Exception {
        return adminUserJpaService.findUsersList(searchCommon.searchCommon(page, paramMap));
    }

    /**
     * <pre>
     * 1. MethodName : login
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User ????????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "Admin ?????? ????????? ??????", notes = "Admin ????????? ????????? ????????????.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "??????", response = Map.class),
            @ApiResponse(code = 400, message = "????????? ??????", response = BadRequest.class),
            @ApiResponse(code = 401, message = "???????????? ?????? ?????????", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "????????????", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "?????? ??????", response = ServerError.class)
    })
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {
        Map<String, Object> userMap = new HashMap<>();

        AdminUserEntity adminUserEntity = builder()
                .userId(authenticationRequest.getUserId())
                .password(authenticationRequest.getPassword())
                .build();

        if ("Y".equals(adminUserJpaService.adminLogin(adminUserEntity))) {
            userMap.put("loginYn", "Y");
            userMap.put("userId", adminUserEntity.getUserId());
            userMap.put("token", createAuthenticationToken(authenticationRequest));

            // ????????? ?????? ??? ????????? token ??? DB??? ??????
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            adminUserEntity.setUserToken(accessToken);
            adminUserEntity.setUserRefreshToken(refreshToken);
            jwtTokenUtil.setHeaderAccessToken(response, accessToken);
            jwtTokenUtil.setHeaderRefreshToken(response, refreshToken);

            adminUserJpaService.insertToken(adminUserEntity);
        }

        return userMap;

    }

    /**
     * <pre>
     * 1. MethodName : createAuthenticationToken
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : ????????? ????????? ??? JWT ?????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 02.
     * </pre>
     */
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // id, password ??????
        authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

        // ????????? ?????? ?????? ??? token ??????
        String token = jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername(authenticationRequest.getUserId()));

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @ApiOperation(value = "JWT ?????? ?????????", notes = "JWT ????????? ?????????")
    @PostMapping(value = "/refresh")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "REFRESH-TOKEN", value = "refresh-token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> createAuthenticationRefreshToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // id, password ??????
        authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

        // ????????? ?????? ?????? ??? token ??????
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetailsService.loadUserByUsername(authenticationRequest.getUserId()));

        return ResponseEntity.ok(new AuthenticationResponse(refreshToken));
    }

    /**
     * <pre>
     * 1. MethodName : authenticate
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : ????????? ????????? ??? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 02.
     * </pre>
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
     * 3. Comment    : ????????? ????????????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 11.
     * </pre>
     */
    @ApiOperation(value = "Admin ???????????? ??????", notes = "Admin ??????????????? ????????????.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "??????", response = Map.class),
            @ApiResponse(code = 400, message = "????????? ??????", response = BadRequest.class),
            @ApiResponse(code = 401, message = "???????????? ?????? ?????????", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "????????????", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "?????? ??????", response = ServerError.class)
    })
    @PostMapping
    public AdminUserDTO insertAdminUser(@Valid @RequestBody AdminUserEntity adminUserEntity) throws Exception {
        return adminUserJpaService.insertAdminUser(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : ????????? ?????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 11.
     * </pre>
     */
    @ApiOperation(value = "Admin ?????? ?????? ??????", notes = "Admin ?????? ????????? ????????????.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "??????", response = Map.class),
            @ApiResponse(code = 400, message = "????????? ??????", response = BadRequest.class),
            @ApiResponse(code = 401, message = "???????????? ?????? ?????????", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "????????????", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "?????? ??????", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminUserDTO updateAdminUser(@Valid @RequestBody AdminUserEntity adminUserEntity) throws Exception {
        return adminUserJpaService.updateAdminUser(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : ????????? ?????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 11.
     * </pre>
     */
    @ApiOperation(value = "Admin ???????????? ??????", notes = "Admin ???????????? ????????????.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "??????", response = Map.class),
            @ApiResponse(code = 400, message = "????????? ??????", response = BadRequest.class),
            @ApiResponse(code = 401, message = "???????????? ?????? ?????????", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "????????????", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "?????? ??????", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Integer deleteAdminUser(@PathVariable Integer idx) throws Exception {
        return adminUserJpaService.deleteAdminUser(idx);
    }
}
