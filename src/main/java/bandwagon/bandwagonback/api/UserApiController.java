package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "UserApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserApiController {

    private final UserService userService;
    private final PostService postService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;

    @Operation(description = "로그인")
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginForm form) {
        log.info("Login init...");
        User user = userService.findOneByEmail(form.getEmail());
        if(user.getIsSocial()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("가입되지 않은 회원입니다!"));
        }
        // 로그인 authentication 통과 못할 시 BadCredentialsException
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));
        Map<String, String> tokens = jwtTokenUtil.generateToken(new UserTokenDto(user.getEmail(), false));
        log.info("Logging in User: {}", user.getEmail());
        return ResponseEntity.ok(new LoginResponse(tokens.get("accessToken"), tokens.get("refreshToken"), user.getNickname(), jwtTokenUtil.extractExpiration(tokens.get("accessToken"))));
    }

    @Operation(description = "회원 탈퇴")
    @DeleteMapping("/api/unregister")
    public ResponseEntity<?> unregisterUser(HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            userService.unregister(email);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "유저 이메일 찾기")
    @PostMapping("/api/find/email")
    public ResponseEntity<?> findUserEmail(@RequestBody FindUserEmailRequest request) {
        FindUserEmailDto findUserEmailDto = userService.findUserEmail(request);
        return ResponseEntity.ok(findUserEmailDto);
    }

    @Operation(description = "유저 비밀번호 찾기")
    @PostMapping("/api/find/password")
    public ResponseEntity<?> findUserPassword(@RequestBody FindUserPasswordRequest request) {
        try {
            userService.findUserPassword(request);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "유저 기본 정보 조회")
    @GetMapping("/api/users")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        User user = userService.findOneByEmail(email);
        return ResponseEntity.ok(new UserEditDto(user));
    }

    @Operation(description = "유저 기본 정보/비밀번호 수정")
    @PutMapping("/api/users")
    public ResponseEntity<?> editUserInfo(@RequestBody UserEditRequest userEditRequest, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            String email = jwtTokenUtil.extractUsername(jwt);
            UserEditDto userEditDto = userService.editUser(email, userEditRequest);
            return ResponseEntity.ok(userEditDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "유저 아바타 변경")
    @PostMapping("/api/users/{email}/avatar")
    public ResponseEntity<?> postUserAvatar(@PathVariable("email") String email, @RequestParam("image")MultipartFile multipartFile, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            String jwtEmail = jwtTokenUtil.extractUsername(jwt);
            if (!jwtEmail.equals(email)) {
                throw new Exception("User in token and user in URL is different");
            }
            String imgUrl =  userService.uploadAvatar(email, multipartFile);
            return ResponseEntity.ok().body(new ImageResponseDto(imgUrl));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "유저 찜한 게시글 목록")
    @GetMapping("/api/users/{email}/likes")
    public ResponseEntity<?> getUserLikePosts(@PathVariable("email") String email,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            String jwtEmail = jwtTokenUtil.extractUsername(jwt);
            if (!jwtEmail.equals(email)) {
                throw new Exception("User in token and user in URL is different");
            }
            User user = userService.findOneByEmail(email);
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Post> likedPosts = postService.getLikedPosts(email, pageRequest);
            LikedPostPageDto likedPostPageDto = new LikedPostPageDto(likedPosts.getContent().stream().map(post -> {
                if (post.getDtype().equals("User")) {
                    return UserPostDto.makeUserPostDto((UserPost) post, user);
                } else {
                    return BandPostDto.makeBandPostDto((BandPost) post, user);
                }
            }).collect(Collectors.toList()), likedPosts.getNumber(), likedPosts.getTotalElements(), likedPosts.getTotalPages());
            return ResponseEntity.ok(likedPostPageDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }
}
