package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.LikedPostPageDto;
import bandwagon.bandwagonback.dto.PopularPostsDto;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.dto.UserHasPostException;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.PostNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    public Long createUserPost(String email, PostDto postDto) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if(userPostRepository.existsByUser(user)) {
            throw new UserHasPostException();
        }
        UserPost userPost = new UserPost(postDto);
        userPost.setUser(user);
        postRepository.save(userPost);
        return userPost.getId();
    }

    @Transactional
    public Long createBandPost(Long bandId, PostDto postDto) {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        BandPost bandPost = new BandPost(postDto);
        band.addPost(bandPost);
        postRepository.save(bandPost);
        return bandPost.getId();
    }

    public PostDto viewPost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return new PostDto(post);
    }

    public PostDto viewPost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return PostDto.makePostDto(post, user);
    }

    @Transactional
    public Long editPost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new PostNotFoundException();
        }
        post.update(postDto);
        return post.getId();
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Page<BandPost> searchBandPosts(Specification<BandPost> specification, PageRequest pageRequest) {
        return bandPostRepository.findAll(specification, pageRequest);
    }

    public Page<UserPost> searchUserPosts(Specification<UserPost> specification, PageRequest pageRequest) {
        return userPostRepository.findAll(specification, pageRequest);
    }

    public UserPost getUsersPost(User user) {
        UserPost userPost = userPostRepository.findOneByUser(user).orElse(null);
        if (userPost == null) {
            throw new PostNotFoundException();
        }
        return userPost;
    }

    public Page<BandPost> getBandsPosts(Band band, PageRequest pageRequest) {
        return bandPostRepository.findAllByBand(band, pageRequest);
    }

    @Transactional
    public void likePost(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new PostNotFoundException();
        }
        user.likePost(post);
    }

    @Transactional
    public void unlikePost(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new PostNotFoundException();
        }
        user.unlikePost(post);
    }

    public Page<Post> getLikedPosts(String email, PageRequest pageRequest) {
        return postRepository.findAllByLikingUsers_email(email, pageRequest);
    }


    public List<Post> getPopularPosts(PageRequest pageRequest) {
        return postRepository.findTop3OrderByLikingUsersSize(pageRequest);
    }

    public String getPostType(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return post.getDtype();
    }

    public Boolean isPostByUser(Long postId, String email) {
        UserPost userPost = userPostRepository.findById(postId).orElse(null);
        if (userPost == null) {
            throw new PostNotFoundException();
        }
        return userPost.getUser().getEmail().equals(email);
    }

    public Boolean isPostByBand(Long bandPostId, Long bandId) {
        BandPost bandPost = bandPostRepository.findById(bandPostId).orElse(null);
        if (bandPost == null) {
            throw new PostNotFoundException();
        }
        return bandPost.getBand().getId().equals(bandId);
    }

}
