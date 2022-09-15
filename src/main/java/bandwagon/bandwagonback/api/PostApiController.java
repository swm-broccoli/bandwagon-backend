package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.dto.exception.InvalidTypeException;
import bandwagon.bandwagonback.dto.exception.notof.PostNotOfBandException;
import bandwagon.bandwagonback.dto.exception.notof.PostNotOfUserException;
import bandwagon.bandwagonback.repository.specification.BandPostSpecification;
import bandwagon.bandwagonback.repository.specification.UserPostSpecification;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "PostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostApiController {

    private final PostService postService;
    private final UserService userService;
    private final BandService bandService;
    private final BandMemberService bandMemberService;


    @Operation(description = "게시글 제목, 본문 조회")
    @GetMapping("/api/post/{post_id}")
    public ResponseEntity<?> getPost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId) {
        PostDto postDto;
        if (userDetails == null) {
            postDto = postService.viewPost(postId);
        } else {
            String email = userDetails.getUsername();
            User user = userService.findOneByEmail(email);
            postDto = postService.viewPost(postId, user);
        }
        return ResponseEntity.ok(postDto);
    }

    @Operation(description = "게시글 등록")
    @PostMapping("/api/post")
    public ResponseEntity<?> postPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostDto postDto) {
        String email = userDetails.getUsername();
        if (postDto.getDtype().equals("Band")) {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            Long bandPostId = postService.createBandPost(bandId, postDto);
            return ResponseEntity.ok(new SimpleIdResponse(bandPostId));
        } else if (postDto.getDtype().equals("User")) {
            Long userPostId = postService.createUserPost(email, postDto);
            return ResponseEntity.ok(new SimpleIdResponse(userPostId));
        } else {
            throw new InvalidTypeException();
        }
    }

    @Operation(description = "게시글 수정")
    @PutMapping("/api/post/{post_id}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId, @RequestBody PostDto postDto) {
        String email = userDetails.getUsername();
        if (postDto.getDtype().equals("Band")) {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            if (!postService.isPostByBand(postId, bandId)) {
                throw new PostNotOfBandException();
            }
            return ResponseEntity.ok(new SimpleIdResponse(postService.editPost(postId, postDto)));
        } else if (postDto.getDtype().equals("User")) {
            if (!postService.isPostByUser(postId, email)) {
                throw new PostNotOfUserException();
            }
            return ResponseEntity.ok(new SimpleIdResponse(postService.editPost(postId, postDto)));
        } else {
            throw new InvalidTypeException();
        }
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping("/api/post/{post_id}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId) {
        String email = userDetails.getUsername();
        String dtype = postService.getPostType(postId);
        if (dtype.equals("Band")) {
            Long bandId = bandMemberService.getBandIdByUserEmail(email);
            if (!postService.isPostByBand(postId, bandId)) {
                throw new PostNotOfBandException();
            }
            postService.deletePost(postId);
        } else {
            if (!postService.isPostByUser(postId, email)) {
                throw new PostNotOfUserException();
            }
            postService.deletePost(postId);
        }
        return ResponseEntity.ok(null);
    }

    @Operation(description = "밴드 게시글 검색")
    @GetMapping("/api/band/post")
    public ResponseEntity<?> searchBandPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer[] position,
            @RequestParam(required = false) Boolean anyPosition,
            @RequestParam(required = false) Integer[] genre,
            @RequestParam(required = false) Boolean anyGenre,
            @RequestParam(required = false) Integer[] area,
            @RequestParam(required = false) Boolean anyArea,
            @RequestParam(required = false) Integer[] day) {

        Specification<BandPost> specification = (root, query, criteriaBuilder) -> null;
        if (title != null) {
            specification = specification.and(BandPostSpecification.containsStringInTitle(title));
        }
        if (position != null) {
            specification = specification.and(BandPostSpecification.containsPosition(position));
        }
        if (anyPosition != null && anyPosition) {
            specification = specification.or(BandPostSpecification.anyPosition());
        }
        if (genre != null) {
            specification = specification.and(BandPostSpecification.containsGenre(genre));
        }
        if (anyGenre != null && anyGenre) {
            specification = specification.or(BandPostSpecification.anyGenre());
        }
        if (area != null) {
            specification = specification.and(BandPostSpecification.containsArea(area));
        }
        if (anyArea != null && anyArea) {
            specification = specification.or(BandPostSpecification.anyArea());
        }
        if (day != null) {
            specification = specification.and(BandPostSpecification.containsDay(day));
        }
        if (minAge != null) {
            specification = specification.and(BandPostSpecification.ageGreaterThan(minAge));
        }
        if (maxAge != null) {
            specification = specification.and(BandPostSpecification.ageLessThan(maxAge));
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BandPost> bandPosts = postService.searchBandPosts(specification, pageRequest);
        BandPostPageDto bandPostPageDto;
        if (userDetails == null) {
            bandPostPageDto = new BandPostPageDto(bandPosts.getContent().stream().map(BandPostDto::new).collect(Collectors.toList()), bandPosts.getNumber(), bandPosts.getTotalElements(), bandPosts.getTotalPages());
        } else {
            String email = userDetails.getUsername();
            User user = userService.findOneByEmail(email);
            bandPostPageDto = new BandPostPageDto(bandPosts.getContent().stream().map(post -> BandPostDto.makeBandPostDto(post, user)).collect(Collectors.toList()), bandPosts.getNumber(), bandPosts.getTotalElements(), bandPosts.getTotalPages());
        }
        return ResponseEntity.ok(bandPostPageDto);
    }

    @Operation(description = "유저 게시글 검색")
    @GetMapping("/api/user/post")
    public ResponseEntity<?> searchUserPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) Integer[] position,
            @RequestParam(required = false) Integer[] genre,
            @RequestParam(required = false) Integer[] area,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {

        Specification<UserPost> specification = (root, query, criteriaBuilder) -> null;
        if (title != null) {
            specification = specification.and(UserPostSpecification.containsStringInTitle(title));
        }
        if (gender != null) {
            specification = specification.and(UserPostSpecification.isGender(gender));
        }
        if (position != null) {
            specification = specification.and(UserPostSpecification.playsPosition(position));
        }
        if (genre != null) {
            specification = specification.and(UserPostSpecification.likesGenre(genre));
        }
        if (area != null) {
            specification = specification.and(UserPostSpecification.availableArea(area));
        }
        if (minAge != null) {
            specification = specification.and(UserPostSpecification.ageGreaterThan(minAge));
        }
        if (maxAge != null) {
            specification = specification.and(UserPostSpecification.ageLessThan(maxAge));
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<UserPost> userPosts = postService.searchUserPosts(specification, pageRequest);
        UserPostPageDto userPostPageDto;
        if (userDetails == null) {
            userPostPageDto = new UserPostPageDto(userPosts.getContent().stream().map(UserPostDto::new).collect(Collectors.toList()), userPosts.getNumber(), userPosts.getTotalElements(), userPosts.getTotalPages());
        } else {
            String email = userDetails.getUsername();
            User user = userService.findOneByEmail(email);
            userPostPageDto = new UserPostPageDto(userPosts.getContent().stream().map(post -> UserPostDto.makeUserPostDto(post, user)).collect(Collectors.toList()), userPosts.getNumber(), userPosts.getTotalElements(), userPosts.getTotalPages());
        }
        return ResponseEntity.ok(userPostPageDto);
    }

    @Operation(description = "유저 작성 구직글 조회")
    @GetMapping("/api/my/user/post")
    public ResponseEntity<?> getUsersPosts(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findOneByEmail(email);
        UserPost usersPost = postService.getUsersPost(user);
        return ResponseEntity.ok(UserPostDto.makeUserPostDto(usersPost, user));
    }

    @Operation(description = "유저의 밴드의 구인글 조회")
    @GetMapping("/api/my/band/post")
    public ResponseEntity<?> getUsersBandsPosts(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        String email = userDetails.getUsername();
        User user = userService.findOneByEmail(email);
        Band usersBand = bandService.getUsersBand(user);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BandPost> bandsPosts = postService.getBandsPosts(usersBand, pageRequest);
        BandPostPageDto bandPostPageDto = new BandPostPageDto(bandsPosts.getContent().stream().map(post -> BandPostDto.makeBandPostDto(post, user)).collect(Collectors.toList()), bandsPosts.getNumber(), bandsPosts.getTotalElements(), bandsPosts.getTotalPages());
        return ResponseEntity.ok(bandPostPageDto);
    }

    @Operation(description = "게시글 좋아요")
    @PostMapping("/api/post/{post_id}/like")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId) {
        String email = userDetails.getUsername();
        postService.likePost(email, postId);
        return ResponseEntity.ok(null);
    }

    @Operation(description = "게시글 좋아요 취소")
    @DeleteMapping("/api/post/{post_id}/like")
    public ResponseEntity<?> unlikePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("post_id") Long postId) {
        String email = userDetails.getUsername();
        postService.unlikePost(email, postId);
        return ResponseEntity.ok(null);
    }

}
