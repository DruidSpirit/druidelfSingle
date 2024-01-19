package druidelf.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Log4j2
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMin}")
    private int jwtExpirationInMin;

    /**
     * 生成 JWT
     *
     * @param username 用户名称
     * @return JWT 字符串
     */
    public String generateToken( String username ) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (long) jwtExpirationInMin *60*1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从 JWT 中获取用户名
     *
     * @param token JWT 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 验证 JWT 的有效性
     *
     * @param token JWT 字符串
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 检查JWT是否在有效期内
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            return !expirationDate.before(now); // 如果过期时间在当前时间之前，则返回false
        } catch (Exception e) {
            return false;
        }
    }

}

