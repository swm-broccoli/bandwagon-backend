package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.dto.BandPostDto;
import bandwagon.bandwagonback.dto.MultiplePostsDto;
import bandwagon.bandwagonback.dto.PostDto;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
import bandwagon.bandwagonback.repository.BandPostRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final BandPostRepository bandPostRepository;
    private final BandPrerequisiteService bandPrerequisiteService;

    @Transactional
    public void makeRecommendations() {
        List<User> allUsers = userRepository.findAll();
        List<BandPost> allBandPosts = bandPostRepository.findAll();
        for (User user : allUsers) {
            user.resetRecommendation();
            HashMap<BandPost, Integer> scoreByPost = new HashMap<>();
            // scorebypost에 bandpost별 점수 추가
            for (BandPost bandPost : allBandPosts) {
                Integer score = bandPrerequisiteService.calculateScore(user, bandPost);
                scoreByPost.put(bandPost, score);
            }
            LinkedList<Map.Entry<BandPost, Integer>> scoreList = new LinkedList<>(scoreByPost.entrySet());
            scoreList.sort(((o1, o2) -> o2.getValue() - o1.getValue()));
            int i = 0;
            while(user.getRecommendedPosts().size() < 4 && i < scoreList.size()) {
                user.addRecommendation(scoreList.get(i).getKey());
                i++;
            }
        }
    }

    public MultiplePostsDto getUsersRecommendations(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        ArrayList<PostDto> posts = new ArrayList<>();
        for (BandPost bandPost : user.getRecommendedPosts()) {
            posts.add(new BandPostDto(bandPost));
        }
        posts.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        return new MultiplePostsDto(posts);
    }
}
