package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostApiController {

    private final PostService postService;
    
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
}
