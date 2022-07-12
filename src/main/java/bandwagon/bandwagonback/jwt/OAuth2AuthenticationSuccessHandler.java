package bandwagon.bandwagonback.jwt;

import bandwagon.bandwagonback.domain.AuthUserDetails;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.OAuthAttributes;
import bandwagon.bandwagonback.repository.UserRepository;
import bandwagon.bandwagonback.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            log.info("응답이 이미 커밋된 상태입니다. 리다이렉트하도록 바꿀 수 없습니다.");
            return;
        }
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        // User 찾아서 AuthUserDetails 만드려 UserSerivce, Repo 부르면 loop 형성.. 따로 OAuth 위해 email만 받는 constructor 사용
        Map<String, String> tokens = jwtUtil.generateToken(new AuthUserDetails(email));
        String url = makeRedirectUrl(tokens.get("accessToken"), tokens.get("refreshToken"));
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect/?accessToken="+accessToken+
                        "?refreshToken=" + refreshToken)
                .build().toUriString();
    }
}
