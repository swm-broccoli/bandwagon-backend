package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "BandPageApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandPageApiController {

    private final BandService bandService;
    private final BandMemberService bandMemberService;
    private final BandGigService bandGigService;
    private final BandPracticeService bandPracticeService;
    private final BandPhotoService bandPhotoService;
    private final JwtUtil jwtTokenUtil;

    @PostMapping("/api/band/{band_id}/gig")
    public ResponseEntity<?> postBandGig(@PathVariable("band_id") Long bandId, @RequestBody PerformanceDto performanceDto, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandGigService.saveBandGig(bandId, email, performanceDto);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/band/{band_id}/gig/{band_gig_id}")
    public ResponseEntity<?> deleteBandGig(@PathVariable("band_id") Long bandId, @PathVariable("band_gig_id") Long bandGigId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandGigService.deleteBandGig(bandId, email, bandGigId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }


    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }
}
