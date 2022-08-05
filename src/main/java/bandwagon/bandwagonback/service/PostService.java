package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BandPostRepository bandPostRepository;
    private final BandRepository bandRepository;

    @Transactional
    public Long createBandPost(Long bandId, PostDto postDto) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        // 밴드별 중복 포스트 허용?
//        BandPost prevBandPost = bandPostRepository.findFirstByBand(band);
//        if (prevBandPost != null) {
//            throw new Exception("Band Already has Post");
//        }
        BandPost bandPost = new BandPost(postDto);
        bandPost.setBand(band);
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

    public PostDto viewBandPostByBandId(Long bandId) throws Exception {
        BandPost bandPost = bandPostRepository.findFirstByBand_id(bandId);
        if (bandPost == null) {
            throw new Exception("Band Post does not exist!");
        }
        return new PostDto(bandPost);
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
    public void deletePost(Long postId) throws Exception {
        postRepository.deleteById(postId);
    }

    public BandPost getBandPostByPostId(Long postId) throws Exception {
        Optional<BandPost> bandPost = bandPostRepository.findById(postId);
        if (bandPost.isEmpty()) {
            throw new Exception("No Band Post by requested post ID!");
        }
        return bandPost.get();
    }

    public Boolean isPostByBand(Long bandPostId, Long bandId) throws Exception {
        BandPost bandPost = bandPostRepository.findById(bandPostId).orElse(null);
        if (bandPost == null) {
            throw new Exception("No Band post with that ID");
        }
        return bandPost.getBand().getId().equals(bandId);
    }

}
