package com.yule.dashboard.pbl.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class SecurityProvider {

    private final SecurityProperties properties;
    private final ObjectMapper om;
    private SecretKeySpec spec;

    @PostConstruct
    public void init() {
        this.spec = new SecretKeySpec(
                properties.getJwt().getSecret().getBytes(),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    /* --- generate token --- */
    private String genToken(SecurityPrincipal principal, Long tokenValidDuration) {
        String json;

        try {
            json = om.writeValueAsString(principal);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Jwts.builder()
                .claims(Jwts.claims().add("user", json).build())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenValidDuration))
                .signWith(spec)
                .compact();
    }

    /* --- generate token --- */
    public String genAT(SecurityPrincipal principal) {
        return genToken(principal, properties.getJwt().getAccessTokenExpiry());
    }

    /* --- generate token --- */
    public String genRT(SecurityPrincipal principal) {
        return genToken(principal, properties.getJwt().getRefreshTokenExpiry());
    }


    /* --- extract --- */
    public String getTokenFromHeader(HttpServletRequest request) {
        String auth = request.getHeader(properties.getJwt().getHeader());
        return auth == null ?
                null :
                auth.startsWith(properties.getJwt().getType()) ?
                        auth.substring(properties.getJwt().getType().length()).trim() :
                        null;
    }

    /* --- check is validated Token --- */
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(spec)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    /* --- check is validated Token --- */
    public boolean isValidatedToken(String token) {
        return !getAllClaims(token).getExpiration().before(new Date());
    }

    /* --- get user details from Token --- */
    public UserDetails getUserDetailsFromToken(String token) {
        Claims claims = getAllClaims(token);
        String json = (String) claims.get("user");
        SecurityPrincipal principal;
        try {
            principal = om.readValue(json, SecurityPrincipal.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new SecurityUserDetails(principal);
    }

    /* --- get Authentication from UserDetails --- */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
