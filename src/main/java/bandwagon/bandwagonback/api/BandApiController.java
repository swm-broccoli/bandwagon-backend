package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.BandCreateForm;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.ImageResponseDto;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.BandMemberService;
import bandwagon.bandwagonback.service.BandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "BandApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandApiController {

    private final BandService bandService;
    private final BandMemberService bandMemberService;
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

    @PostMapping("/api/band/{band_id}/description")
    public ResponseEntity<?> editDescription(@PathVariable("band_id") Long bandId, @RequestBody EditDescriptionForm editDescriptionForm, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            bandService.editDescription(email, bandId, editDescriptionForm.getDescription());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/api/band/{band_id}/avatar")
    public ResponseEntity<?> postBandAvatar(@PathVariable("band_id") Long bandId, @RequestParam("image") MultipartFile multipartFile, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            String imgUrl = bandService.uploadAvatar(email, bandId, multipartFile);
            return ResponseEntity.ok().body(new ImageResponseDto(imgUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    // 추후엔 유저 검색해서 찾고 합류 신청 보내면 바로 합류 되는게 아니라 수락 해야 join 되도록
    @PostMapping("/api/band/{band_id}/member")
    public ResponseEntity<?> addMember(@PathVariable("band_id") Long bandId, @RequestBody AddMemberForm addMemberForm, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try{
            Long newMemberId = bandMemberService.addMemberToBand(email, bandId, addMemberForm.getEmail());
            return ResponseEntity.ok().body(new AddMemberResponse(newMemberId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }

    @Data
    static class EditNameForm {
        private String name;
    }
    @Data
    static class EditDescriptionForm {
        private String description;
    }
    @Data
    static class AddMemberForm {
        private String email;
    }
    @Data
    static class AddMemberResponse {
        private Long id;
        public AddMemberResponse(Long id) {
            this.id = id;
        }
    }
}
