package com.example.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // JWT에서 클레임을 추출해 Subject를 가져와 주체를 식별한다.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //특정 클레임을 추출한다. 모든 클레임을 추출하여 주어진 함수 claimsResolver를 적용해서 특정 클레임을 추출한다.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 사용자 정보를 기반으로 JWT를 생성한다.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //추가 클레임과 사용자 정보를 포함한 JWT를 생성한다.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // JWT를 실제로 구축한다. Jwts 빌더를 사용해서 클레임, 주체, 발행 시간, 만료 시간을 설정하고 서명한다.
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //주어진 토큰이 유효한지 검사한다.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //토큰이 만료되었는지 확인한다. 토큰의 만료 시간을 추출해서 현재 시간과 비교한다.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //토큰의 만료시간을 추출한다.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //토큰에서 모든 클레임을 추출한다. JWT 파서를 이용해서 토큰을 파싱하고 클레임을 추출한다.
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰 서명에 사용할 키를 생성한다. Base64URL로 인코딩된 비밀 키를 디코딩해서 HMAC-SHA키로 변환한다.
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    generateToken과 buildToken은 토큰 생성 과정을 담당한다.
    extractClaim은 extractUsername, extractExpiration 등 다양한 클레임 추출 메서드의 기반이 된다.
    isTokenValid는 extractUsername과 isTokenExpired를 사용하여 토큰의 유효성을 검증한다.
     */
}
