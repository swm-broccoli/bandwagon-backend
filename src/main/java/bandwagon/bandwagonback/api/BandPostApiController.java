package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.PrerequisiteDto;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "BandPostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandPostApiController {

    private final BandMemberService bandMemberService;
    private final PostService postService;
    private final BandPrerequisiteService bandPrerequisiteService;
    private final JwtUtil jwtTokenUtil;

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 조회")
    @GetMapping("/api/band/post/{post_id}/prerequisites")
    public ResponseEntity<?> getAllBandPrerequisites(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            if (!postService.isPostByBand(postId, bandId)) {
                throw new Exception("로그인 한 유저의 밴드와 request로 제공된 post의 band가 일치하지 않습니다!");
            }
            List<PrerequisiteDto> prerequisites = bandPrerequisiteService.getAllPrerequisiteOfPost(postId);
            return ResponseEntity.ok().body(new PrerequisitesDto(prerequisites));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 등록")
    @PostMapping("/api/band/post/{post_id}/prerequisites")
    public ResponseEntity<?> addBandPrerequisite(@PathVariable("post_id") Long postId, @RequestBody PrerequisiteDto prerequisiteDto,
                                                 HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            if (!postService.isPostByBand(postId, bandId)) {
                throw new Exception("로그인 한 유저의 밴드와 request로 제공된 post의 band가 일치하지 않습니다!");
            }
            bandPrerequisiteService.addPrerequisite(postId, prerequisiteDto);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 수정")
    @PutMapping("/api/band/post/{post_id}/prerequisites/{prerequisite_id}")
    public ResponseEntity<?> editBandPrerequisite(@PathVariable("post_id") Long postId,
                                                 @PathVariable("prerequisite_id") Long prerequisiteId,
                                                 @RequestBody PrerequisiteDto prerequisiteDto,
                                                 HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            if (!postService.isPostByBand(postId, bandId)) {
                throw new Exception("로그인 한 유저의 밴드와 request로 제공된 post의 band가 일치하지 않습니다!");
            }
            bandPrerequisiteService.editPrerequisite(postId, prerequisiteId, prerequisiteDto);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 구인글 - 지원 조건(prerequisite) 삭제")
    @DeleteMapping("/api/band/post/{post_id}/prerequisites/{prerequisite_id}")
    public ResponseEntity<?> deleteBandPrerequisite(@PathVariable("post_id") Long postId, @PathVariable("prerequisite_id") Long prerequisiteId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            if (!postService.isPostByBand(postId, bandId)) {
                throw new Exception("로그인 한 유저의 밴드와 request로 제공된 post의 band가 일치하지 않습니다!");
            }
            bandPrerequisiteService.deletePrerequisite(postId, prerequisiteId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }

    @Data
    static class PrerequisitesDto {
        private List<PrerequisiteDto> prerequisites;

        public PrerequisitesDto(List<PrerequisiteDto> prerequisites) {
            this.prerequisites = prerequisites;
        }
    }

}
