package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.AuthUserDetailsService;
import bandwagon.bandwagonback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "UserApiController")
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
    @Operation(description = "로그인 확인용 Mock api")
    @GetMapping("/")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    @Operation(description = "로그인")
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginForm form) {
        try {
            // User 없을 시 UsernameNotFoundException
            UserDetails userDetails = userDetailsService.loadUserByUsername(form.getEmail());
            // 로그인 authentication 통과 못할 시 BadCredentialsException
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));

            Map<String, String> tokens = jwtTokenUtil.generateToken(userDetails);

            User user = userService.findOneByEmail(form.getEmail());

            return ResponseEntity.ok(new LoginResponse(tokens.get("accessToken"), tokens.get("refreshToken"), user.getNickname(), jwtTokenUtil.extractExpiration(tokens.get("accessToken"))));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("이메일이나 비밀번호가 올바르지 않습니다!"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("가입되지 않은 회원입니다!"));
        }
    }

    @Operation(description = "Refresh Token으로 token 재발급")
    @PostMapping("/api/refresh")
    public ResponseEntity<?> refresh(@RequestBody HashMap<String, String> bodyJson) {
        String refreshToken = bodyJson.get("refreshToken");
        String username = jwtTokenUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(jwtTokenUtil.validateToken(refreshToken, userDetails)) {
            Map<String, String> tokens = jwtTokenUtil.generateToken(userDetails);
            User user = userService.findOneByEmail(username);
            return ResponseEntity.ok(new LoginResponse(tokens.get("accessToken"), tokens.get("refreshToken"), user.getNickname(), jwtTokenUtil.extractExpiration(tokens.get("accessToken"))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Refresh Token 만료! 로그인 필요"));
    }

    @Operation(description = "회원가입")
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            Long id = userService.join(request);
            return ResponseEntity.ok(new SignUpResponse(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }
}
