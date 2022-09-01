package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.service.BandService;
import bandwagon.bandwagonback.service.PostService;
import bandwagon.bandwagonback.service.UserService;
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
    private final UserService userService;
    private final BandService bandService;

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
    
    //TODO: My/Band Page DTO 로 주지 말고 메인 페이지에 맞는 DTO로 변경 필요
    @Operation(description = "오늘의(랜덤) 포트폴리오 조회")
    @GetMapping("/api/random")
    public ResponseEntity<?> getRandomPortfolio() {
        if (Math.random() <= 0.5) {
            User randomUser = userService.getRandomUser();
            return ResponseEntity.ok(new MyPageDto(randomUser));
        } else {
            Band randomBand = bandService.getRandomBand();
            return ResponseEntity.ok(new BandPageDto(randomBand, false));
        }
    }
}
