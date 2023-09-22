package com.leopold.modules.login.controller;

import com.leopold.modules.login.dto.LoginRequestDto;
import com.leopold.modules.login.dto.TokensResponseDto;
import com.leopold.modules.login.service.LoginService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;

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
    @GetMapping(value="/logout")
    public ResponseEntity<HttpStatus> logoutSession(@RequestAttribute("accessToken") String access) throws AuthenticationException {
        loginService.logoutSession(access);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param access access token from filter
     * @return code 200 if successful logout all sessions, 401 or 500 otherwise
     */
    @GetMapping(value="/logout-all-sessions")
    public ResponseEntity<HttpStatus> logoutAllSessions(@RequestAttribute("accessToken") String access) throws AuthenticationException {
        loginService.logoutAllSessions(access);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @return 200 if token is ok, 401 or 500 otherwise
     */
    @GetMapping(value="/verify-access")
    public ResponseEntity<HttpStatus> verifyAccess() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param refresh refresh token
     * @return code 200 and new access token, 401 or 500 otherwise
     */
    @PostMapping(value="/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody String refresh) {
        String refreshed = loginService.refreshToken(refresh);
        return ResponseEntity.ok(refreshed);
    }

    @ExceptionHandler({IllegalArgumentException.class, AuthenticationException.class})
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
