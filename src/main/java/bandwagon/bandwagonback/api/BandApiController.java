package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.BandCreateForm;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.ImageResponseDto;
import bandwagon.bandwagonback.dto.SimpleIdResponse;
import bandwagon.bandwagonback.service.BandMemberService;
import bandwagon.bandwagonback.service.BandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "BandApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandApiController {

    private final BandService bandService;
    private final BandMemberService bandMemberService;

    @Operation(description = "밴드 생성")
    @PostMapping("/api/band/create")
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody BandCreateForm bandCreateForm) {
        String email = userDetails.getUsername();
        bandService.createBand(email, bandCreateForm);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 해체")
    @DeleteMapping("/api/band/{band_id}")
    public ResponseEntity<?> deleteBand(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId) {
        String email = userDetails.getUsername();
        bandService.disbandBand(email, bandId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 이름 수정")
    @PostMapping("/api/band/{band_id}/name")
    public ResponseEntity<?> editName(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestBody EditNameForm editNameForm) {
        String email = userDetails.getUsername();
        bandService.editName(email, bandId, editNameForm.getName());
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 소개 수정")
    @PostMapping("/api/band/{band_id}/description")
    public ResponseEntity<?> editDescription(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestBody EditDescriptionForm editDescriptionForm) {
        String email = userDetails.getUsername();
        bandService.editDescription(email, bandId, editDescriptionForm.getDescription());
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 아바타 수정")
    @PostMapping("/api/band/{band_id}/avatar")
    public ResponseEntity<?> postBandAvatar(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestParam("image") MultipartFile multipartFile) {
        String email = userDetails.getUsername();
        try {
            String imgUrl = bandService.uploadAvatar(email, bandId, multipartFile);
            return ResponseEntity.ok().body(new ImageResponseDto(imgUrl));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    // 추후엔 유저 검색해서 찾고 합류 신청 보내면 바로 합류 되는게 아니라 수락 해야 join 되도록
    @Operation(description = "밴드 멤버 추가")
    @PostMapping("/api/band/{band_id}/member")
    public ResponseEntity<?> addMember(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @RequestBody AddMemberForm addMemberForm) {
        String email = userDetails.getUsername();
        Long newMemberId = bandMemberService.addMemberToBand(email, bandId, addMemberForm.getEmail());
        return ResponseEntity.ok().body(new SimpleIdResponse(newMemberId));
    }

    @Operation(description = "밴드 멤버 제거")
    @DeleteMapping("/api/band/{band_id}/member/{band_member_id}")
    public ResponseEntity<?> removeMember(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_member_id") Long bandMemberId) {
        String email = userDetails.getUsername();
        bandMemberService.removeMemberFromBand(email, bandId, bandMemberId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "프런트맨 변경")
    @PostMapping("/api/band/{band_id}/frontman/{band_member_id}")
    public ResponseEntity<?> changeFrontman(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_member_id") Long bandMemberId) {
        String email = userDetails.getUsername();
        bandMemberService.changeFrontman(email, bandMemberId, bandId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 멤버 포지션 추가")
    @PostMapping("/api/band/{band_id}/member/{band_member_id}/positions/{position_id}")
    public ResponseEntity<?> addPositionToMember(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_member_id") Long bandMemberId, @PathVariable("position_id") Long positionId) {
        String email = userDetails.getUsername();
        bandMemberService.addPositionToBandMember(email, bandId, bandMemberId, positionId);
        return ResponseEntity.ok().body(null);
    }
    
    @Operation(description = "밴드 멤버 포지션 제거")
    @DeleteMapping("/api/band/{band_id}/member/{band_member_id}/positions/{position_id}")
    public ResponseEntity<?> deletePositionFromMember(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId, @PathVariable("band_member_id") Long bandMemberId, @PathVariable("position_id") Long positionId) {
        String email = userDetails.getUsername();
        bandMemberService.deletePositionFromBandMember(email, bandId, bandMemberId, positionId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 탈퇴(본인이)")
    @DeleteMapping("/api/band/{band_id}/member")
    public ResponseEntity<?> withdrawFromBand(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("band_id") Long bandId) {
        String email = userDetails.getUsername();
        bandMemberService.withdrawMemberFromBand(email, bandId);
        return ResponseEntity.ok(null);
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
}
