package com.application.sushi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.application.sushi.domain.response.ResLoginDto;

@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM =MacAlgorithm.HS256;

    @Value("${nam.jwt.base64-secret}")
    private String jwtKey;
    @Value("${nam.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    public String createAccessToken(String email, ResLoginDto ResLoginDto){
        ResLoginDto.UserInsideToken user_token = new ResLoginDto.UserInsideToken();
        user_token.setId(ResLoginDto.getUserLogin().getId());
        user_token.setEmail(ResLoginDto.getUserLogin().getEmail());
        user_token.setName(ResLoginDto.getUserLogin().getName());
        user_token.setRole(ResLoginDto.getUserLogin().getRole());
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("user", user_token)
            .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String createTokenForResetPassword(String email, ResLoginDto resLoginDto){
        ResLoginDto.UserInsideToken user_token = new ResLoginDto.UserInsideToken();
        user_token.setId(resLoginDto.getUserLogin().getId());
        user_token.setEmail(resLoginDto.getUserLogin().getEmail());
        user_token.setName(resLoginDto.getUserLogin().getName());
        user_token.setRole(resLoginDto.getUserLogin().getRole());
        Instant now = Instant.now();
        Instant validity = now.plus(300, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("user", user_token)
            .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
    public String createOtp(){
        int randomOtp = (int)(Math.random()*90000)+10000;
        return String.valueOf(randomOtp);
    }
}
