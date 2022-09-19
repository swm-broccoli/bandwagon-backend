package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.MultiplePostsDto;
import bandwagon.bandwagonback.dto.exception.notauthorized.UserNotAuthorizedException;
import bandwagon.bandwagonback.service.RecommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Tag(name = "BatchApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BatchApiController {

    private final RecommendationService recommendationService;
    // TODO: Change secret to be fetched from env var
    private final String SECRET_KEY = "changetolocalvar";

    @PostMapping("/api/recommendations")
    public ResponseEntity<?> makeRecommendations(HttpServletRequest request) {
        String requestKey = request.getHeader("Authorization");
        if (requestKey == null || !requestKey.equals(SECRET_KEY)) {
            throw new UserNotAuthorizedException();
        }
        recommendationService.makeRecommendations();
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/api/recommendations")
    public ResponseEntity<?> getMyRecommendations(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok(new MultiplePostsDto(new ArrayList<>()));
        }
        String email = userDetails.getUsername();
        MultiplePostsDto usersRecommendations = recommendationService.getUsersRecommendations(email);
        return ResponseEntity.ok(usersRecommendations);
    }
}
