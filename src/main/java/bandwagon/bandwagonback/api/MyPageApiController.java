package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.DescriptionDto;
import bandwagon.bandwagonback.dto.MyPageDto;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


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

    @Operation(description = "유저 페이지 불러오기")
    @GetMapping("/api/users/{email}/mypage")
    public ResponseEntity<?> getMyPage(@PathVariable("email") String email) {
        User user = userService.findOneByEmail(email);
        return ResponseEntity.ok().body(new MyPageDto(user));
    }

    // TODO: API URI 상 email 필요 없게 변경, 추후 아래 API URI에서 email 제거 요망
    @Operation(description = "자기소개 수정")
    @PutMapping("/api/users/{email}/description")
    public ResponseEntity<?> putUserDescription(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DescriptionDto descriptionDto) {
        String email = userDetails.getUsername();
        userService.editDescription(email, descriptionDto.getDescription());
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "신규 연주기록 생성")
    @PostMapping("/api/users/{email}/performance")
    public ResponseEntity<?> postUserPerformance(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PerformanceDto performanceDto) {
        String email = userDetails.getUsername();
        userPerformanceService.saveUserPerformance(email, performanceDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "연주기록 삭제")
    @DeleteMapping("/api/users/{email}/performance/{user_performance_id}")
    public ResponseEntity<?> deleteUserPerformance(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("user_performance_id") Long user_performance_id) {
        String email = userDetails.getUsername();
        userPerformanceService.deleteUserPerformance(email, user_performance_id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "연주기록 수정")
    @PutMapping("/api/users/{email}/performance/{user_performance_id}")
    public ResponseEntity<?> putUserPerformance(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("user_performance_id") Long user_performance_id,
                                                @RequestBody PerformanceDto performanceDto) {
        String email = userDetails.getUsername();
        userPerformanceService.updateUserPerformance(email, user_performance_id, performanceDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "포지션 추가")
    @PostMapping("/api/users/{email}/positions/{position_id}")
    public ResponseEntity<?> postPosition(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("position_id") Long position_id) {
        String email = userDetails.getUsername();
        positionService.addPositionToUser(email, position_id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "포지션 삭제")
    @DeleteMapping("/api/users/{email}/positions/{position_id}")
    public ResponseEntity<?> deletePosition(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("position_id") Long position_id) {
        String email = userDetails.getUsername();
        positionService.deletePositionFromUser(email, position_id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "선호 장르 추가")
    @PostMapping("/api/users/{email}/genres/{genre_id}")
    public ResponseEntity<?> postGenre(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("genre_id") Long genre_id) {
        String email = userDetails.getUsername();
        genreService.addGenreToUser(email, genre_id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "선호 장르 삭제")
    @DeleteMapping("/api/users/{email}/genres/{genre_id}")
    public ResponseEntity<?> deleteGenre(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("genre_id") Long genre_id) {
        String email = userDetails.getUsername();
        genreService.deleteGenreFromUser(email, genre_id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "활동 지역 추가")
    @PostMapping("/api/users/{email}/areas/{area_id}")
    public ResponseEntity<?> postArea(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("area_id") Long area_id) {
        String email = userDetails.getUsername();
        areaService.addAreaToUser(email, area_id);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "활동 지역 삭제")
    @DeleteMapping("/api/users/{email}/areas/{area_id}")
    public ResponseEntity<?> deleteArea(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("area_id") Long area_id) {
        String email = userDetails.getUsername();
        areaService.deleteAreaFromUser(email, area_id);
        return ResponseEntity.ok().body(null);
    }

}
