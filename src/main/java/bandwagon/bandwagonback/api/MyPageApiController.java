package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.DescriptionDto;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.MyPageDto;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "MyPageApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MyPageApiController {

    private final UserService userService;
    private final UserPerformanceService userPerformanceService;
    private final PositionService positionService;
    private final GenreService genreService;
    private final AreaService areaService;
    private final JwtUtil jwtTokenUtil;

    @Operation(description = "유저 페이지 불러오기")
    @GetMapping("/api/users/{email}/mypage")
    public ResponseEntity<?> getMyPage(@PathVariable("email") String email, HttpServletRequest request) {
        User user = userService.findOneByEmail(email);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User does not exist"));
        }
        return ResponseEntity.ok().body(new MyPageDto(user));
    }

    @Operation(description = "자기소개 수정")
    @PutMapping("/api/users/{email}/description")
    public ResponseEntity<?> putUserDescription(@PathVariable("email") String email, @RequestBody DescriptionDto descriptionDto, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            userService.editDescription(email, descriptionDto.getDescription());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "신규 연주기록 생성")
    @PostMapping("/api/users/{email}/performance")
    public ResponseEntity<?> postUserPerformance(@PathVariable("email") String email, @RequestBody PerformanceDto performanceDto, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            userPerformanceService.saveUserPerformance(email, performanceDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "연주기록 삭제")
    @DeleteMapping("/api/users/{email}/performance/{user_performance_id}")
    public ResponseEntity<?> deleteUserPerformance(@PathVariable("email") String email, @PathVariable("user_performance_id") Long user_performance_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            userPerformanceService.deleteUserPerformance(email, user_performance_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "연주기록 수정")
    @PutMapping("/api/users/{email}/performance/{user_performance_id}")
    public ResponseEntity<?> putUserPerformance(@PathVariable("email") String email, @PathVariable("user_performance_id") Long user_performance_id,
                                                @RequestBody PerformanceDto performanceDto, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            userPerformanceService.updateUserPerformance(email, user_performance_id, performanceDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "포지션 추가")
    @PostMapping("/api/users/{email}/positions/{position_id}")
    public ResponseEntity<?> postPosition(@PathVariable("email") String email, @PathVariable("position_id") Long position_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            positionService.addPositionToUser(email, position_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "포지션 삭제")
    @DeleteMapping("/api/users/{email}/positions/{position_id}")
    public ResponseEntity<?> deletePosition(@PathVariable("email") String email, @PathVariable("position_id") Long position_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            positionService.deletePositionFromUser(email, position_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "선호 장르 추가")
    @PostMapping("/api/users/{email}/genres/{genre_id}")
    public ResponseEntity<?> postGenre(@PathVariable("email") String email, @PathVariable("genre_id") Long genre_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            genreService.addGenreToUser(email, genre_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "선호 장르 삭제")
    @DeleteMapping("/api/users/{email}/genres/{genre_id}")
    public ResponseEntity<?> deleteGenre(@PathVariable("email") String email, @PathVariable("genre_id") Long genre_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            genreService.deleteGenreFromUser(email, genre_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "활동 지역 추가")
    @PostMapping("/api/users/{email}/areas/{area_id}")
    public ResponseEntity<?> postArea(@PathVariable("email") String email, @PathVariable("area_id") Long area_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            areaService.addAreaToUser(email, area_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "활동 지역 삭제")
    @DeleteMapping("/api/users/{email}/areas/{area_id}")
    public ResponseEntity<?> deleteArea(@PathVariable("email") String email, @PathVariable("area_id") Long area_id, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(email, request);
            areaService.deleteAreaFromUser(email, area_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    private String getJwtFromHeader(String email, HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt =  authorizationHeader.substring(7);
        String jwtEmail = jwtTokenUtil.extractUsername(jwt);
        if (!jwtEmail.equals(email)) {
            throw new Exception("User in token and user in URL is different");
        }
        return jwt;
    }
}
