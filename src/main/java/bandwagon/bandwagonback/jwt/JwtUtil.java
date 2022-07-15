package bandwagon.bandwagonback.jwt;

import bandwagon.bandwagonback.dto.UserTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    // TODO: Hide secret key
    private String SECRET_KEY = "secredfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgasdasdasdasdasdasd";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Map<String, String> generateToken(UserTokenDto userTokenDto) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", createToken(claims, userTokenDto.getEmail()));
        tokens.put("refreshToken", createRefreshToken(claims, userTokenDto.getEmail()));
        return tokens;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        claims.put("isRefresh", false);
        // access Token expires after 30 mins -> cur 30 secs for testing
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        claims.put("isRefresh", true);
        // refresh Token expires after 3 days -> cur 10 min for testing
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public Boolean validateToken(String token, UserTokenDto userTokenDto) {
        final String username = extractUsername(token);
        return (username.equals(userTokenDto.getEmail()) && !isTokenExpired(token));
    }
}
