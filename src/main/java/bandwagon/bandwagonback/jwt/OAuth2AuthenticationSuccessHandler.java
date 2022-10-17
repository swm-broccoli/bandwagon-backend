package bandwagon.bandwagonback.jwt;

import bandwagon.bandwagonback.dto.UserTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    JwtUtil jwtUtil;

    @Value("${OAUTH_FRONT_URL}")
    private String oauthFrontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth authentication Success!!");
        if (response.isCommitted()) {
            log.info("응답이 이미 커밋된 상태입니다. 리다이렉트하도록 바꿀 수 없습니다.");
            return;
        }
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("From principal: {}", oAuth2User.toString());
        // SuccessHandler에서 registrationId를 못 얻어 kakao인지 naver인지 구분하기 위해 아래 방식으로 처리..
        // 더 좋은 방법있으면 Refactor
        String email;
        if(oAuth2User.getAttributes().get("kakao_account") != null) {
            // Kakao 로그인이면
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
        } else {
            //Naver 로그인이면
            email = (String) oAuth2User.getAttributes().get("email");
        }
        // User 찾아서 AuthUserDetails 만드려 UserSerivce, Repo 부르면 loop 형성.. 따로 OAuth 위해 email만 받는 constructor 사용
        Map<String, String> tokens = jwtUtil.generateToken(new UserTokenDto(email, true));
        String url = makeRedirectUrl(email, tokens.get("accessToken"), tokens.get("refreshToken"));
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String email, String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(oauthFrontUrl + "/oauth2/redirect/?email=" + email + "&accessToken="+accessToken+
                        "&refreshToken=" + refreshToken)
                .build().toUriString();
    }
}
