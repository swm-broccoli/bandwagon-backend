package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.repository.BandPostRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

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
            HashMap<Long, Integer> scoreByPost = new HashMap<>();
            // scorebypost에 bandpost별 점수 추가

            while(user.getRecommendedPosts().size() < 3) {
                // scorebypost 정렬 후 top 3개 추가
            }
        }
    }
}
