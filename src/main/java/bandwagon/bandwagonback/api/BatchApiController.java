package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.exception.notauthorized.UserNotAuthorizedException;
import bandwagon.bandwagonback.service.RecommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        if (!requestKey.equals(SECRET_KEY)) {
            throw new UserNotAuthorizedException();
        }
        recommendationService.makeRecommendations();
        return ResponseEntity.ok().body(null);
    }
}
