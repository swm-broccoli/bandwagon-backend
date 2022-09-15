package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.PrerequisiteCheckDto;
import bandwagon.bandwagonback.dto.PrerequisiteDto;
import bandwagon.bandwagonback.dto.exception.notof.PostNotOfBandException;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "BandPrerequisiteApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandPrerequisiteApiController {

    private final BandMemberService bandMemberService;
    private final PostService postService;
    private final BandPrerequisiteService bandPrerequisiteService;

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 조회")
    @GetMapping("/api/band/post/{post_id}/prerequisites")
    public ResponseEntity<?> getAllBandPrerequisites(@AuthenticationPrincipal UserDetails userDetails,  @PathVariable("post_id") Long postId) {
        String email = userDetails.getUsername();
        Long bandId = bandMemberService.getBandIdByUserEmail(email);
        if (!postService.isPostByBand(postId, bandId)) {
            throw new PostNotOfBandException();
        }
        List<PrerequisiteDto> prerequisites = bandPrerequisiteService.getAllPrerequisiteOfPost(postId);
        return ResponseEntity.ok().body(new PrerequisitesDto(prerequisites));
    }

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 등록")
    @PostMapping("/api/band/post/{post_id}/prerequisites")
    public ResponseEntity<?> addBandPrerequisite(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId, @RequestBody PrerequisiteDto prerequisiteDto) {
        String email = userDetails.getUsername();
        Long bandId = bandMemberService.getBandIdByUserEmail(email);
        if (!postService.isPostByBand(postId, bandId)) {
            throw new PostNotOfBandException();
        }
        bandPrerequisiteService.addPrerequisite(postId, prerequisiteDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 수정")
    @PutMapping("/api/band/post/{post_id}/prerequisites/{prerequisite_id}")
    public ResponseEntity<?> editBandPrerequisite(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable("post_id") Long postId,
                                                  @PathVariable("prerequisite_id") Long prerequisiteId,
                                                  @RequestBody PrerequisiteDto prerequisiteDto) {
        String email = userDetails.getUsername();
        Long bandId = bandMemberService.getBandIdByUserEmail(email);
        if (!postService.isPostByBand(postId, bandId)) {
            throw new PostNotOfBandException();
        }
        bandPrerequisiteService.editPrerequisite(postId, prerequisiteId, prerequisiteDto);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 삭제")
    @DeleteMapping("/api/band/post/{post_id}/prerequisites/{prerequisite_id}")
    public ResponseEntity<?> deleteBandPrerequisite(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId, @PathVariable("prerequisite_id") Long prerequisiteId) {
        String email = userDetails.getUsername();
        Long bandId = bandMemberService.getBandIdByUserEmail(email);
        if (!postService.isPostByBand(postId, bandId)) {
            throw new PostNotOfBandException();
        }
        bandPrerequisiteService.deletePrerequisite(postId, prerequisiteId);
        return ResponseEntity.ok().body(null);
    }

    @Operation(description = "밴드 구인글 - 지원 조건 체크")
    @GetMapping("/api/band/post/{post_id}/prerequisites/check")
    public ResponseEntity<?> checkUserWithPrerequisites(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId) {
        String email = userDetails.getUsername();
        List<PrerequisiteCheckDto> prerequisiteCheckDtos = bandPrerequisiteService.checkUserAndReturnForm(email, postId);
        return ResponseEntity.ok(prerequisiteCheckDtos);
    }

    @Data
    static class PrerequisitesDto {
        private List<PrerequisiteDto> prerequisites;

        public PrerequisitesDto(List<PrerequisiteDto> prerequisites) {
            this.prerequisites = prerequisites;
        }
    }

}
