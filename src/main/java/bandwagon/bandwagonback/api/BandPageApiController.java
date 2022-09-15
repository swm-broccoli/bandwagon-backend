package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.BandPageDto;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.ImageResponseDto;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "BandPageApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandPageApiController {

    private final BandService bandService;
    private final BandGigService bandGigService;
    private final BandPracticeService bandPracticeService;
    private final BandPhotoService bandPhotoService;
    private final GenreService genreService;
    private final AreaService areaService;
    private final DayService dayService;

    @Operation(description = "(자신의) 밴드 페이지 불러오기")
    @GetMapping("/api/band")
    public ResponseEntity<?> getBandPage(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        BandPageDto bandPageDto = bandService.getUsersBandPage(email);
        return ResponseEntity.ok().body(bandPageDto);
    }

    @Operation(description = "(아무나) 밴드 페이지 불러오기")
    @GetMapping("/api/band/{band_id}/bandpage")
    public ResponseEntity<?> getOtherBandPage(@PathVariable("band_id") Long bandId) {
        BandPageDto bandPageDto = bandService.getOtherBandPage(bandId);
        return ResponseEntity.ok(bandPageDto);
    }

    @Operation(description = "밴드 공연 추가")
    @PostMapping("/api/band/{band_id}/gig")
    public ResponseEntity<?> postBandGig(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestBody PerformanceDto performanceDto) {
        String email = userDetails.getUsername();
        bandGigService.saveBandGig(bandId, email, performanceDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 공연 삭제")
    @DeleteMapping("/api/band/{band_id}/gig/{band_gig_id}")
    public ResponseEntity<?> deleteBandGig(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_gig_id") Long bandGigId) {
        String email = userDetails.getUsername();
        bandGigService.deleteBandGig(bandId, email, bandGigId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 공연 수정")
    @PutMapping("/api/band/{band_id}/gig/{band_gig_id}")
    public ResponseEntity<?> editBandGig(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_gig_id") Long bandGigId, @RequestBody PerformanceDto performanceDto) {
        String email = userDetails.getUsername();
        bandGigService.updateBandGig(bandId, email, bandGigId, performanceDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 연습 추가")
    @PostMapping("/api/band/{band_id}/practice")
    public ResponseEntity<?> postBandPractice(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestBody PerformanceDto performanceDto) {
        String email = userDetails.getUsername();
        bandPracticeService.saveBandPractice(bandId, email, performanceDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 연습 삭제")
    @DeleteMapping("/api/band/{band_id}/practice/{band_practice_id}")
    public ResponseEntity<?> deleteBandPractice(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_practice_id") Long bandPracticeId) {
        String email = userDetails.getUsername();
        bandPracticeService.deleteBandPractice(bandId, email, bandPracticeId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 연습 수정")
    @PutMapping("/api/band/{band_id}/practice/{band_practice_id}")
    public ResponseEntity<?> editBandPractice(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_practice_id") Long bandPracticeId, @RequestBody PerformanceDto performanceDto) {
        String email = userDetails.getUsername();
        bandPracticeService.updateBandPractice(bandId, email, bandPracticeId, performanceDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 선호 장르 추가")
    @PostMapping("/api/band/{band_id}/genres/{genre_id}")
    public ResponseEntity<?> postBandGenre(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("genre_id") Long genreId) {
        String email = userDetails.getUsername();
        genreService.addGenreToBand(email, bandId, genreId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 선호 장르 제거")
    @DeleteMapping("/api/band/{band_id}/genres/{genre_id}")
    public ResponseEntity<?> deleteBandGenre(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("genre_id") Long genreId) {
        String email = userDetails.getUsername();
        genreService.deleteGenreFromBand(email, bandId, genreId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 활동 지역 추가")
    @PostMapping("/api/band/{band_id}/areas/{area_id}")
    public ResponseEntity<?> postBandArea(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("area_id") Long areaId) {
        String email = userDetails.getUsername();
        areaService.addAreaToBand(email, bandId, areaId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 활동 지역 제거")
    @DeleteMapping("/api/band/{band_id}/areas/{area_id}")
    public ResponseEntity<?> deleteBandArea(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("area_id") Long areaId) {
        String email = userDetails.getUsername();
        areaService.deleteAreaFromBand(email, bandId, areaId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 활동 요일 추가")
    @PostMapping("/api/band/{band_id}/days/{day_id}")
    public ResponseEntity<?> postDayArea(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("day_id") Long dayId) {
        String email = userDetails.getUsername();
        dayService.addDayToBand(email, bandId, dayId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 활동 요일 제거")
    @DeleteMapping("/api/band/{band_id}/days/{day_id}")
    public ResponseEntity<?> deleteDayArea(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("day_id") Long dayId) {
        String email = userDetails.getUsername();
        dayService.deleteDayFromBand(email, bandId, dayId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 사진 추가")
    @PostMapping("/api/band/{band_id}/photos")
    public ResponseEntity<?> postBandPhoto(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestParam("image")MultipartFile multipartFile) {
        String email = userDetails.getUsername();
        try {
            String imgUrl = bandPhotoService.addPhotoToBand(email, bandId, multipartFile);
            return ResponseEntity.ok().body(new ImageResponseDto(imgUrl));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 사진 제거")
    @DeleteMapping("/api/band/{band_id}/photos/{photo_id}")
    public ResponseEntity<?> deleteBandPhoto(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("photo_id") Long photoId) {
        String email = userDetails.getUsername();
        try {
            bandPhotoService.removePhotoFromBand(email, bandId, photoId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }
}
