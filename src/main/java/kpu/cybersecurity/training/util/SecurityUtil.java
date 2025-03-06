package kpu.cybersecurity.training.util;

import com.nimbusds.jose.util.Base64;
import kpu.cybersecurity.training.config.EnvProperties;
import kpu.cybersecurity.training.config.JwtConfig;
import kpu.cybersecurity.training.domain.dto.response.ResLoginDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLoginWithCookieDTO;
import kpu.cybersecurity.training.domain.entity.User;
import kpu.cybersecurity.training.service.UserService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@Service
@Log4j2
public class SecurityUtil {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private EnvProperties envProperties;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;


    public String createAccessToken(String userId, ResLoginDTO.UserLogin dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.envProperties.getJwt().getAccessTokenValidTime(), ChronoUnit.SECONDS);


        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(userId)
                .claim("user", dto)
                .claim("permission", "ADMIN")
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JwtConfig.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(String userID, ResLoginDTO dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.envProperties.getJwt().getRefreshTokenValidTime(), ChronoUnit.SECONDS);


        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(userID)
                .claim("user", dto.getUser())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JwtConfig.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(jwtConfig.getSecretKey())
                .macAlgorithm(JwtConfig.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch(Exception e) {
            log.error(">>> Refresh Token error: " + e.getMessage());
            throw e;
        }
    }

    public ResLoginWithCookieDTO createTokensAndSetCookie(String userId) {
        User currentUser = this.userService.getUserByUserID(userId);

        ResLoginDTO res = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                currentUser.getUserId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail(),
                currentUser.getRole()
        );

        String access_token = this.createAccessToken(userId, userLogin);

        res.setUser(userLogin);
        res.setAccessToken(access_token);

        //create refresh token
        String refresh_token = this.createRefreshToken(userId, res);

        //update user
        this.userService.updateUserToken(refresh_token, userId);

        //set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.envProperties.getJwt().getRefreshTokenValidTime())
                .build();

        return new ResLoginWithCookieDTO(res, cookie);
    }


    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

}
