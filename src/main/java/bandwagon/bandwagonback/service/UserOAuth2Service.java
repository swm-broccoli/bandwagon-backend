package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserInfo;
import bandwagon.bandwagonback.dto.OAuthAttributes;
import bandwagon.bandwagonback.repository.UserInfoRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserOAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("Loading OAuth user");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        log.info("OAuth2User: {}", oAuth2User.toString());
        saveOrUpdate(attributes);
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private void saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .orElse(null);
        if (user == null) {
            user = attributes.toEntity();
            userRepository.save(user);
            log.info("User Saved");
            UserInfo userInfo = new UserInfo();
            log.info("UserInfo created");
            userInfo.setUser(user);
            log.info("UserInfo's user set");
            userInfoRepository.save(userInfo);
            log.info("UserInfo saved");
        }
        log.info("Saved user from OAuth = {}", attributes.getEmail());
    }
}
