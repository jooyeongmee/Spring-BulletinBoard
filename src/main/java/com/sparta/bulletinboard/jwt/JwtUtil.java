package com.sparta.bulletinboard.jwt;


import com.sparta.bulletinboard.dto.response.ResponseMessageDto;
import com.sparta.bulletinboard.dto.response.UserResponseDto;
import com.sparta.bulletinboard.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    @Transactional
    public UserResponseDto getUserInfoFromToken(HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = resolveToken(request);
        Claims claims;
        String str_role;
        UserRoleEnum role = UserRoleEnum.ADMIN;

        UserResponseDto userResponseDto = new UserResponseDto();
        if (token == null || !validateToken(token)) {
            userResponseDto.setResponse(new ResponseMessageDto("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
            return userResponseDto;
        }
        claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        str_role = (String)claims.get(AUTHORIZATION_KEY);
        System.out.println("rollllle ========" + str_role);
        if (str_role.equals("USER")) {
            role = UserRoleEnum.USER;
        }
        System.out.println("rollllle111111 ========" + role);
        userResponseDto.setUsername(claims.getSubject());
        userResponseDto.setRole(role);
        return userResponseDto;
    }

}