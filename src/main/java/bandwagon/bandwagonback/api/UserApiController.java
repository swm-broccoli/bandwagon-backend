package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.AuthUserDetailsService;
import bandwagon.bandwagonback.service.UserPerformanceService;
import bandwagon.bandwagonback.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "UserApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserApiController {

    private final UserService userService;

    private final UserPerformanceService userPerformanceService;
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
            log.info("Login init...");
            User user = userService.findOneByEmail(form.getEmail());
            if(user == null || user.getIsSocial()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("가입되지 않은 회원입니다!"));
            }
            // 로그인 authentication 통과 못할 시 BadCredentialsException
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));

            Map<String, String> tokens = jwtTokenUtil.generateToken(new UserTokenDto(user.getEmail(), false));


            log.info("Logging in User: {}", user.getEmail());

            return ResponseEntity.ok(new LoginResponse(tokens.get("accessToken"), tokens.get("refreshToken"), user.getNickname(), jwtTokenUtil.extractExpiration(tokens.get("accessToken"))));

        } catch (BadCredentialsException e) {
            log.error("Wrong Password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("비밀번호가 올바르지 않습니다!"));
        }
    }

    @Operation(description = "Refresh Token으로 token 재발급")
    @PostMapping("/api/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        // 여기 뭔가 모순이 있는듯, 토큰에서 추출한 이메일로 토큰 validation 검사를 함
        String username = jwtTokenUtil.extractUsername(refreshToken);
        Boolean isSocial = jwtTokenUtil.extractIsSocial(refreshToken);
        UserTokenDto userTokenDto = new UserTokenDto(username, isSocial);
        if(jwtTokenUtil.validateToken(refreshToken, username)) {
            if(!jwtTokenUtil.extractIsRefresh(refreshToken)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Refresh Token이 아닌 AccessToken으로 refresh 시도"));
            }
            Map<String, String> tokens = jwtTokenUtil.generateToken(userTokenDto);
            User user = userService.findOneByEmail(username);
            return ResponseEntity.ok(new LoginResponse(tokens.get("accessToken"), tokens.get("refreshToken"), user.getNickname(), jwtTokenUtil.extractExpiration(tokens.get("accessToken"))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Refresh Token 만료! 로그인 필요"));
    }

    @Operation(description = "회원가입")
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            log.info("Signup init...");
            Long id = userService.join(request);
            log.info("Signup complete: User_id = {}", id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SignUpResponse(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "이메일 중복 확인")
    @PostMapping("/api/duplicate")
    public ResponseEntity<?> duplicate(@RequestBody DuplicateRequest request) {
        try {
            userService.validateDuplicateUser(request.getEmail());
            return ResponseEntity.ok(new DuplicateResponse(request.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "유저 기본정보 요청")
    @GetMapping("/api/users/edit/{email}")
    public ResponseEntity<?> getUserEditInfo(@PathVariable("email") String email, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String jwtEmail = jwtTokenUtil.extractUsername(jwt);
        if (!jwtEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User in token and user in URL is different"));
        }
        User user = userService.findOneByEmail(email);
        return ResponseEntity.ok(new UserEditDto(user));
    }

    @Operation(description = "유저 기본정보 수정")
    @PostMapping("/api/users/edit/{email}")
    public ResponseEntity<?> postUserEditInfo(@PathVariable("email") String email, @RequestBody UserEditRequest userRequest, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String jwtEmail = jwtTokenUtil.extractUsername(jwt);

        if (!jwtEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User in token and user in URL is different"));
        }

        try{
            UserEditDto userEditDto = userService.editUser(userRequest);
            return ResponseEntity.ok(userEditDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "비밀번호 변경")
    @PostMapping("/api/users/password/{email}")
    public ResponseEntity<?> postUserPassInfo(@PathVariable("email") String email, @RequestBody PasswordEditRequest passRequest, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String jwtEmail = jwtTokenUtil.extractUsername(jwt);
        Boolean isSocial = jwtTokenUtil.extractIsSocial(jwt);

        if(isSocial) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Social 계정은 비밀번호 변경이 불가능 합니다."));
        }

        if (!jwtEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User in token and user in URL is different"));
        }

        try {
            userService.editPassword(email, passRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "신규 연주기록 생성")
    @PostMapping("/api/users/performance/{email}")
    public ResponseEntity<?> postUserPerformance(@PathVariable("email") String email, @RequestBody UserPerformanceDto userPerformanceDto, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String jwtEmail = jwtTokenUtil.extractUsername(jwt);

        if (!jwtEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User in token and user in URL is different"));
        }
        try {
            userPerformanceService.saveUserPerformance(email, userPerformanceDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }

//    @Operation(description = "마이 페이지 정보 불러오기")
//    @GetMapping("/api/users/mypage/{email}")
//    public ResponseEntity<?> getMyPage(@PathVariable("email") String email, HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        String jwt = authorizationHeader.substring(7);
//        String jwtEmail = jwtTokenUtil.extractUsername(jwt);
//
//        if (!jwtEmail.equals(email)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User in token and user in URL is different"));
//        }
//        User user = userService.findOneByEmail(email);
//        return ResponseEntity.ok().body(new MyPageDto(user));
//    }
//
//    @Operation(description = "마이 페이지 정보 수정")
//    @PostMapping("/api/users/mypage/{email}")
//    public ResponseEntity<?> postMyPage(@PathVariable("email") String email, @RequestBody MyPageRequest myPageRequest, HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        String jwt = authorizationHeader.substring(7);
//        String jwtEmail = jwtTokenUtil.extractUsername(jwt);
//
//        if (!jwtEmail.equals(email)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User in token and user in URL is different"));
//        }
//        try{
//            userService.updateMyPage(email, myPageRequest);
//            return ResponseEntity.ok().body("Good");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
//        }
//    }
}
