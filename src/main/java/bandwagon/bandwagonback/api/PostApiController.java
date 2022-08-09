package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.dto.SimpleIdResponse;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.BandMemberService;
import bandwagon.bandwagonback.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "PostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostApiController {

    private final PostService postService;
    private final BandMemberService bandMemberService;
    private final JwtUtil jwtTokenUtil;


    @Operation(description = "게시글 제목, 본문 조회")
    @GetMapping("/api/post/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable("post_id") Long postId) {
        try {
            PostDto postDto = postService.viewPost(postId);
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/post")
    public ResponseEntity<?> postPost(@RequestBody PostDto postDto, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            String email = jwtTokenUtil.extractUsername(jwt);
            if (postDto.getDtype().equals("Band")) {
                Long bandId = bandMemberService.getBandIdByUserEmail(email);
                Long bandPostId = postService.createBandPost(bandId, postDto);
                return ResponseEntity.ok(new SimpleIdResponse(bandPostId));
            } else if (postDto.getDtype().equals("User")) {
                Long userPostId = postService.createUserPost(email, postDto);
                return ResponseEntity.ok(new SimpleIdResponse(userPostId));
            } else {
                throw new Exception("Invalid dtype in request!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/api/post/{post_id}")
    public ResponseEntity<?> editPost(@PathVariable("post_id") Long postId, @RequestBody PostDto postDto, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            String email = jwtTokenUtil.extractUsername(jwt);
            if (postDto.getDtype().equals("Band")) {
                Long bandId = bandMemberService.getBandIdByUserEmail(email);
                if (!postService.isPostByBand(postId, bandId)) {
                    throw new Exception("로그인 한 유저의 밴드와 request로 제공된 post의 band가 일치하지 않습니다!");
                }
                postService.editPost(postId, postDto);
                return ResponseEntity.ok(null);
            } else if (postDto.getDtype().equals("User")) {
                if (!postService.isPostByUser(postId, email)) {
                    throw new Exception("로그인 한 유저와 post의 유저가 일치하지 않습니다!");
                }
                postService.editPost(postId, postDto);
                return ResponseEntity.ok(null);
            } else {
                throw new Exception("Invalid dtype in request!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/api/post/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            String email = jwtTokenUtil.extractUsername(jwt);
            String dtype = postService.getPostType(postId);
            if (dtype.equals("Band")) {
                Long bandId = bandMemberService.getBandIdByUserEmail(email);
                if (!postService.isPostByBand(postId, bandId)) {
                    throw new Exception("로그인 한 유저의 밴드와 request로 제공된 post의 band가 일치하지 않습니다!");
                }
                postService.deletePost(postId);
            } else {
                if (!postService.isPostByUser(postId, email)) {
                    throw new Exception("로그인 한 유저와 post의 유저가 일치하지 않습니다!");
                }
                postService.deletePost(postId);
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }
}
