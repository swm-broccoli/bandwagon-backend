package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.BandPostDto;
import bandwagon.bandwagonback.dto.PopularPostsDto;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.dto.UserPostDto;
import bandwagon.bandwagonback.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "MainPageApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MainPageApiController {

    private final PostService postService;

    @Operation(description = "인기 게시글 조회")
    @GetMapping("/api/post/popular")
    public ResponseEntity<?> getPopularPosts() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Post> popularPosts = postService.getPopularPosts(pageRequest);
        List<PostDto> postDtoList = popularPosts.stream().map(post -> {
            if (post.getDtype().equals("Band")) {
                return new BandPostDto((BandPost) post);
            } else {
                return new UserPostDto((UserPost) post);
            }
        }).collect(Collectors.toList());
        PopularPostsDto popularPostsDto = new PopularPostsDto(postDtoList);
        return ResponseEntity.ok(popularPostsDto);
    }
}
