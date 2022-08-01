package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.dto.SimpleIdResponse;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "BandPostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BandPostApiController {

    private final UserService userService;
    private final BandService bandService;
    private final BandMemberService bandMemberService;
    private final PostService postService;
    private final BandPrerequisiteService bandPrerequisiteService;
    private final JwtUtil jwtTokenUtil;

    @PostMapping("/api/band/post")
    public ResponseEntity<?> postBandPost(@RequestBody PostDto postDto, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            Long bandPostId = postService.createBandPost(bandId, postDto);
            return ResponseEntity.ok(new SimpleIdResponse(bandPostId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/band/post/{post_id}")
    public ResponseEntity<?> deleteBandPost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            BandPost bandPost = postService.getBandPostByPostId(postId);
            if (!bandPost.getBand().getId().equals(bandId)) {
                throw new Exception("로그인 한 유저와 uri로 제공된 post의 band가 일치하지 않습니다!");
            }
            postService.deletePost(postId);
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
