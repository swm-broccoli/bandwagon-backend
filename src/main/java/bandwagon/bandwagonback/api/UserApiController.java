package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.LoginForm;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.AuthUserDetailsService;
import bandwagon.bandwagonback.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserApiController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final AuthUserDetailsService userDetailsService;

    private final JwtUtil jwtTokenUtil;

    //for Auth testing
    @GetMapping("/")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginForm form) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(form.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @Data
    static class LoginResponse {
        private String jwt;

        public LoginResponse(String jwt) {
            this.jwt = jwt;
        }
    }
}
