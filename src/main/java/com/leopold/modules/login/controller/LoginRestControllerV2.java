package com.leopold.modules.login.controller;

import com.leopold.modules.login.dto.AccessTokenDto;
import com.leopold.modules.login.dto.LoginRequestDto;
import com.leopold.modules.login.dto.RefreshTokenDto;
import com.leopold.modules.login.dto.TokensResponseDto;
import com.leopold.modules.login.service.LoginService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v2")
public class LoginRestControllerV2 {
    private final LoginService loginService;
    private final UserService userService;

    public LoginRestControllerV2(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    /**
     *
     * @param loginRequestDto username and password
     * @return code 200 and access, refresh tokens if ok
     * @throws CredentialException returns 401 if token is not ok
     */
    @PostMapping(value="/login")
    @Operation(summary = "username and password login, returns access and refresh token if ok, 401 otherwise")
    public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws CredentialException {
        System.out.println("New query");
        System.out.println(loginRequestDto.toString());
        UserEntity user = userService.getUserByUsername(loginRequestDto.getUsername());
        return ResponseEntity.ok(loginService.jwtLoginUsernamePassword(user, loginRequestDto.getPassword()));
    }

    /**
     *
     * @param access access token from filter
     * @return code 200 if successful logout, 401 or 500 otherwise
     */
    @Operation(summary = "logout the session on a specific device if ok, 401 otherwise")
    @GetMapping(value="/logout")
    public ResponseEntity<Void> logoutSession(@RequestAttribute("accessToken") String access) throws AuthenticationException {
        loginService.logoutSession(access);
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param access access token from filter
     * @return code 200 if successful logout all sessions, 401 or 500 otherwise
     */
    @Operation(summary = "logout all the sessions on all your devices if ok, 401 otherwise")
    @GetMapping(value="/logout-all-sessions")
    public ResponseEntity<Void> logoutAllSessions(@RequestAttribute("accessToken") String access) throws AuthenticationException {
        loginService.logoutAllSessions(access);
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @return 200 if token is ok, 401 or 500 otherwise
     */
    @GetMapping(value="/verify-access")
    @Operation(summary = "return 200 if token if ok, 401 otherwise")
    public ResponseEntity<Void> verifyAccess() {
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param dto refresh token dto
     * @return code 200 and new access token, 401 or 500 otherwise
     */
    @PostMapping(value="/refresh")
    @Operation(summary = "you send me refresh token as post, i send you an access one, it's public url, no authorization header needed")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(@RequestBody RefreshTokenDto dto) {
        String access = loginService.refreshToken(dto.getRefresh());
        return ResponseEntity.ok(new AccessTokenDto(access));
    }

    @ExceptionHandler({
        NoSuchElementException.class,
        HttpMessageNotReadableException.class,
    })
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
