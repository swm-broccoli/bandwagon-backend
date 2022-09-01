package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.repository.specification.BandPostSpecification;
import bandwagon.bandwagonback.repository.specification.UserPostSpecification;
import bandwagon.bandwagonback.service.BandMemberService;
import bandwagon.bandwagonback.service.PostService;
import bandwagon.bandwagonback.service.RequestService;
import bandwagon.bandwagonback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "PostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostApiController {

    private final PostService postService;
    private final UserService userService;
    private final RequestService requestService;
    private final BandMemberService bandMemberService;
    private final JwtUtil jwtTokenUtil;


    @Operation(description = "게시글 제목, 본문 조회")
    @GetMapping("/api/post/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        try {
            String jwt = getJwtFromHeader(request);
            PostDto postDto;
            if (jwt == null) {
                postDto = postService.viewPost(postId);
            } else {
                String email = jwtTokenUtil.extractUsername(jwt);
                User user = userService.findOneByEmail(email);
                postDto = postService.viewPost(postId, user);
            }
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "게시글 등록")
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

    @Operation(description = "게시글 수정")
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
                return ResponseEntity.ok(new SimpleIdResponse(postService.editPost(postId, postDto)));
            } else if (postDto.getDtype().equals("User")) {
                if (!postService.isPostByUser(postId, email)) {
                    throw new Exception("로그인 한 유저와 post의 유저가 일치하지 않습니다!");
                }
                return ResponseEntity.ok(new SimpleIdResponse(postService.editPost(postId, postDto)));
            } else {
                throw new Exception("Invalid dtype in request!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "게시글 삭제")
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

    @Operation(description = "밴드 게시글 검색")
    @GetMapping("/api/band/post")
    public ResponseEntity<?> searchBandPosts(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) Integer minAge,
                                             @RequestParam(required = false) Integer maxAge,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) Integer[] position,
                                             @RequestParam(required = false) Integer[] genre,
                                             @RequestParam(required = false) Integer[] area,
                                             @RequestParam(required = false) Integer[] day,
                                             HttpServletRequest request) {

        try {
            Specification<BandPost> specification = (root, query, criteriaBuilder) -> null;
            if (title != null) {
                specification = specification.and(BandPostSpecification.containsStringInTitle(title));
            }
            if (position != null) {
                specification = specification.and(BandPostSpecification.containsPosition(position));
            }
            if (genre != null) {
                specification = specification.and(BandPostSpecification.containsGenre(genre));
            }
            if (area != null) {
                specification = specification.and(BandPostSpecification.containsArea(area));
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

            String jwt = getJwtFromHeader(request);
            BandPostPageDto bandPostPageDto;
            if (jwt == null) {
                bandPostPageDto = new BandPostPageDto(bandPosts.getContent().stream().map(BandPostDto::new).collect(Collectors.toList()), bandPosts.getNumber(), bandPosts.getTotalElements(), bandPosts.getTotalPages());
            } else {
                String email = jwtTokenUtil.extractUsername(jwt);
                User user = userService.findOneByEmail(email);
                bandPostPageDto = new BandPostPageDto(bandPosts.getContent().stream().map(post -> BandPostDto.makeBandPostDto(post, user)).collect(Collectors.toList()), bandPosts.getNumber(), bandPosts.getTotalElements(), bandPosts.getTotalPages());
            }
            return ResponseEntity.ok(bandPostPageDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }

    @Operation(description = "유저 게시글 검색")
    @GetMapping("/api/user/post")
    public ResponseEntity<?> searchUserPosts(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) Boolean gender,
                                             @RequestParam(required = false) Integer[] position,
                                             @RequestParam(required = false) Integer[] genre,
                                             @RequestParam(required = false) Integer[] area,
                                             @RequestParam(required = false) Integer minAge,
                                             @RequestParam(required = false) Integer maxAge,
                                             HttpServletRequest request) {

        try {
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

            String jwt = getJwtFromHeader(request);
            UserPostPageDto userPostPageDto;
            if (jwt == null) {
                userPostPageDto = new UserPostPageDto(userPosts.getContent().stream().map(UserPostDto::new).collect(Collectors.toList()), userPosts.getNumber(), userPosts.getTotalElements(), userPosts.getTotalPages());
            } else {
                String email = jwtTokenUtil.extractUsername(jwt);
                User user = userService.findOneByEmail(email);
                userPostPageDto = new UserPostPageDto(userPosts.getContent().stream().map(post -> UserPostDto.makeUserPostDto(post, user)).collect(Collectors.toList()), userPosts.getNumber(), userPosts.getTotalElements(), userPosts.getTotalPages());
            }
            return ResponseEntity.ok(userPostPageDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }

    @Operation(description = "게시글 좋아요")
    @PostMapping("/api/post/{post_id}/like")
    public ResponseEntity<?> likePost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            postService.likePost(email, postId);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "게시글 좋아요 취소")
    @DeleteMapping("/api/post/{post_id}/like")
    public ResponseEntity<?> unlikePost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            postService.unlikePost(email, postId);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

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

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            // JWT Auth 필터 안거쳐도 되는 search api에서 로그인 한 유전지, 아닌지 구분을 하기 위해서 null 사용.. 다른 방법 있으면 추후 변경 바람
            return null;
        }
        return authorizationHeader.substring(7);
    }
}
