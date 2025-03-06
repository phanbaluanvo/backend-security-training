package kpu.cybersecurity.training.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kpu.cybersecurity.training.config.EnvProperties;
import kpu.cybersecurity.training.domain.dto.request.ReqLoginDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLoginDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLoginWithCookieDTO;
import kpu.cybersecurity.training.domain.entity.User;
import kpu.cybersecurity.training.service.UserService;
import kpu.cybersecurity.training.util.SecurityUtil;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private EnvProperties envProperties;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    @ApiMessage("Login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO dto){

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginWithCookieDTO loginWithCookie = this.securityUtil.createTokensAndSetCookie(authentication.getName());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, loginWithCookie.getCookie().toString())
                .body(loginWithCookie.getUserLogin());
    }

    @GetMapping("/account")
    @ApiMessage("Get Account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount(){

        String userId = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUser = this.userService.getUserByUserID(userId);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        userLogin.setUserId(currentUser.getUserId());
        userLogin.setFirstName(currentUser.getFirstName());
        userLogin.setLastName(currentUser.getLastName());
        userLogin.setEmail(currentUser.getEmail());
        userLogin.setRole(currentUser.getRole());

        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/refresh")
    @ApiMessage("Refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token) {

        //check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);

        String userId = decodedToken.getSubject();

        //check by token + userId
        User currentUser = this.userService.getUserByRefreshTokenAndUserId(refresh_token, userId);
        if(currentUser == null ) {
            throw new EntityNotFoundException("Refresh Token is invalid");
        }

        //issue new token/set cookie
        ResLoginWithCookieDTO loginWithCookie = this.securityUtil.createTokensAndSetCookie(userId);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, loginWithCookie.getCookie().toString())
                .body(loginWithCookie.getUserLogin());
    }

    @PostMapping("/logout")
    @ApiMessage("Logout")
    public ResponseEntity<Void> logout() {
        String userId = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        if(userId.isEmpty()) throw new BadCredentialsException("Invalid Access Token");

        // update refresh token = null
        this.userService.updateUserToken(null, userId);

        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }
}
