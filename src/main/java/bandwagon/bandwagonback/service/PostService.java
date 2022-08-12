package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserPostRepository userPostRepository;
    private final BandPostRepository bandPostRepository;
    private final UserRepository userRepository;
    private final BandRepository bandRepository;


    @Transactional
    public Long createUserPost(String email, PostDto postDto) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        UserPost userPost = new UserPost(postDto);
        userPost.setUser(user);
        postRepository.save(userPost);
        return userPost.getId();
    }

    @Transactional
    public Long createBandPost(Long bandId, PostDto postDto) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        BandPost bandPost = new BandPost(postDto);
        band.addPost(bandPost);
        postRepository.save(bandPost);
        return bandPost.getId();
    }

    public PostDto viewPost(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new Exception("No Post by that ID");
        }
        return new PostDto(post);
    }

    @Transactional
    public void editPost(Long postId, PostDto postDto) throws Exception {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new Exception("Post does not exist!");
        }
        post.update(postDto);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Page<BandPost> searchBandPosts(Specification<BandPost> specification, PageRequest pageRequest) {
        return bandPostRepository.findAll(specification, pageRequest);
    }

    public String getPostType(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new Exception("No post by that ID!");
        }
        return post.getDtype();
    }

    public Boolean isPostByUser(Long postId, String email) throws Exception {
        UserPost userPost = userPostRepository.findById(postId).orElse(null);
        if (userPost == null) {
            throw new Exception("No User post with that ID");
        }
        return userPost.getUser().getEmail().equals(email);
    }

    public Boolean isPostByBand(Long bandPostId, Long bandId) throws Exception {
        BandPost bandPost = bandPostRepository.findById(bandPostId).orElse(null);
        if (bandPost == null) {
            throw new Exception("No Band post with that ID");
        }
        return bandPost.getBand().getId().equals(bandId);
    }

}
