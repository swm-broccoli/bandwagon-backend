package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.BandCreateForm;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.BandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "BandApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandApiController {

    private final BandService bandService;
    private final JwtUtil jwtTokenUtil;

    @PostMapping("/api/band/create")
    public ResponseEntity<?> create(@RequestBody BandCreateForm bandCreateForm, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandService.createBand(email, bandCreateForm);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/api/band/{band_id}/name")
    public ResponseEntity<?> editName(@PathVariable("band_id") Long bandId, @RequestBody EditNameForm editNameForm, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandService.editName(email, bandId, editNameForm.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }

    @Data
    static class EditNameForm {
        private String name;
    }
}
