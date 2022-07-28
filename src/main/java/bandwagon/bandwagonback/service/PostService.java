package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BandPostRepository bandPostRepository;
    private final BandRepository bandRepository;

    @Transactional
    public void createBandPost(Long bandId, PostDto postDto) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        BandPost prevBandPost = bandPostRepository.findFirstByBand(band);
        if (prevBandPost != null) {
            throw new Exception("Band Already has Post");
        }
        BandPost bandPost = new BandPost(postDto);
        bandPost.setBand(band);
        postRepository.save(bandPost);
    }

    public PostDto viewBandPostByBandId(Long bandId) throws Exception {
        BandPost bandPost = bandPostRepository.findFirstByBand_id(bandId);
        if (bandPost == null) {
            throw new Exception("Band Post does not exist!");
        }
        return new PostDto(bandPost);
    }

}
