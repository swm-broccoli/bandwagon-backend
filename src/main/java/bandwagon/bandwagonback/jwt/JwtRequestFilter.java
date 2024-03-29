package bandwagon.bandwagonback.jwt;

import bandwagon.bandwagonback.dto.UserTokenDto;
import bandwagon.bandwagonback.service.AuthUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authorizing JWT Token");
        // Request의 header에서 'Authorization' 헤더 가져옴
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Authorization header value에서 앞에 'Bearer ' 제거한 부분 추출.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer") && authorizationHeader.length() > 7) {
            log.info("JWT Token Exists in Header");
            log.info("Extracting JWT Token from header ...");
            jwt = authorizationHeader.substring(7);
            log.info("Extracted JWT Token: {}", jwt);
            log.info("Extracting email from Token ...");
            username = jwtUtil.extractUsername(jwt);
            log.info("Extracted email from Token: {}", username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Username extracted from JWT Token and is not yet Authenticated");
            // Username (email) 로 유저 추출
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // Jwt token 유효한지 검사 후 user authenticate
            if (jwtUtil.validateToken(jwt, username)) {
                log.info("JWT Token is Valid");
                log.info("Authenticating user: {} ...", username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                log.info("Authenticated user: {}", username);
            }
        }
        filterChain.doFilter(request, response);
    }
}
