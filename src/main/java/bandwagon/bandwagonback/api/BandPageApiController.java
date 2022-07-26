package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.BandPageDto;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.ImageResponseDto;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.dto.exception.NoBandException;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JwtUtil jwtTokenUtil;

    @GetMapping("/api/band")
    public ResponseEntity<?> getBandPage(HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            BandPageDto bandPageDto = bandService.getUsersBandPage(email);
            return ResponseEntity.ok().body(bandPageDto);
        } catch (NoBandException nbe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(nbe.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

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

    @PutMapping("/api/band/{band_id}/gig/{band_gig_id}")
    public ResponseEntity<?> editBandGig(@PathVariable("band_id") Long bandId, @PathVariable("band_gig_id") Long bandGigId, @RequestBody PerformanceDto performanceDto, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandGigService.updateBandGig(bandId, email, bandGigId, performanceDto);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/band/{band_id}/practice")
    public ResponseEntity<?> postBandPractice(@PathVariable("band_id") Long bandId, @RequestBody PerformanceDto performanceDto, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandPracticeService.saveBandPractice(bandId, email, performanceDto);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/band/{band_id}/practice/{band_practice_id}")
    public ResponseEntity<?> deleteBandPractice(@PathVariable("band_id") Long bandId, @PathVariable("band_practice_id") Long bandPracticeId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandPracticeService.deleteBandPractice(bandId, email, bandPracticeId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/api/band/{band_id}/practice/{band_practice_id}")
    public ResponseEntity<?> editBandPractice(@PathVariable("band_id") Long bandId, @PathVariable("band_practice_id") Long bandPracticeId, @RequestBody PerformanceDto performanceDto, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandPracticeService.updateBandPractice(bandId, email, bandPracticeId, performanceDto);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/band/{band_id}/genres/{genre_id}")
    public ResponseEntity<?> postBandGenre(@PathVariable("band_id") Long bandId, @PathVariable("genre_id") Long genreId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            genreService.addGenreToBand(email, bandId, genreId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/band/{band_id}/genres/{genre_id}")
    public ResponseEntity<?> deleteBandGenre(@PathVariable("band_id") Long bandId, @PathVariable("genre_id") Long genreId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            genreService.deleteGenreFromBand(email, bandId, genreId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/band/{band_id}/areas/{area_id}")
    public ResponseEntity<?> postBandArea(@PathVariable("band_id") Long bandId, @PathVariable("area_id") Long areaId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            areaService.addAreaToBand(email, bandId, areaId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/band/{band_id}/areas/{area_id}")
    public ResponseEntity<?> deleteBandArea(@PathVariable("band_id") Long bandId, @PathVariable("area_id") Long areaId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            areaService.deleteAreaFromBand(email, bandId, areaId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/band/{band_id}/photos")
    public ResponseEntity<?> postBandPhoto(@PathVariable("band_id") Long bandId, @RequestParam("image")MultipartFile multipartFile, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            String imgUrl = bandPhotoService.addPhotoToBand(email, bandId, multipartFile);
            return ResponseEntity.ok().body(new ImageResponseDto(imgUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/band/{band_id}/photos/{photo_id}")
    public ResponseEntity<?> deleteBandPhoto(@PathVariable("band_id") Long bandId, @PathVariable("photo_id") Long photoId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandPhotoService.removePhotoFromBand(email, bandId, photoId);
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
