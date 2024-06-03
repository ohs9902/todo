package com.sparta.todo.jwt;

import com.sparta.todo.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Getter
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") //application.properties 에 있는 시크릿키를 가져온다.
    private static String secretKey;

    private static Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //암호화 알고리즘

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    private final Map<String,String> refreshTokenStore = new HashMap<>();


    @PostConstruct //딱한번만 받아오 되는 값을 사용할 때마다  요청을 새로하는 실수를 방지하기 위해 사용
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public void saveRefreshToken(String username , String refreshToken){
        refreshTokenStore.put(username,refreshToken);
    }

    public boolean validateRefreshToken(String username,String refreshToken){
        return refreshToken.equals(refreshTokenStore.get(username));
    }

    public static String createRefreshToken() {
        // 리프레시 토큰 생성 로직 (예: JWT 생성)
        return Jwts.builder()
                .setSubject("refresh")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7일 유효 기간
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public static String createToken(String username, UserRoleEnum role){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) //사용자 식별값
                        .claim(AUTHORIZATION_KEY,role) //사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //만료시간
                        .setIssuedAt(date) //발급
                        .signWith(key,signatureAlgorithm) //암호화 알고리즘
                        .compact();
    }

    public static void addJwtToCookie(String token, HttpServletResponse res){
        try{
            token = URLEncoder.encode(token,"utf-8").replaceAll("\\+","%20");
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER,token);
            cookie.setPath("/"); //웹 어플리케이션 전체에서 쿠키에 접근 가능

            res.addCookie(cookie);

        }catch (UnsupportedEncodingException e){
            logger.error(e.getMessage());
        }
    }

    public static String substringToken(String tokenValue){
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)){ //tokenValue가 null이나 공백이 아니고 BEARER_PREFIX로 시작할 때
            return tokenValue.substring(7); //공백 포함 7칸 제거 후 반환
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public static Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public static String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // HttpServletRequest 에서 Cookie Value : Refresh Token 가져오기
    public static String getRefreshTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
