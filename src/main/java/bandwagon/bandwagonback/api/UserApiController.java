package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/login")
    public LoginResponse login(@ModelAttribute LoginForm form) {
        User user = userService.findOneByEmail(form.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("가입되지 않은 이메일 입니다.");
        }
        log.info("email={}, password={}", user.getEmail(), user.getPassword());

        if (user.getPassword().equals(form.getPassword())) {
            return new LoginResponse(user.getId(), user.getName());
        } else {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
    }

    @Data
    static class LoginResponse {
        private Long userId;
        private String username;

        public LoginResponse(Long userId, String username) {
            this.userId = userId;
            this.username = username;
        }
    }
}
