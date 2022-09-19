package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.service.RecommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BatchApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BatchApiController {

    private final RecommendationService recommendationService;

    @PostMapping("/api/recommendations")
    public ResponseEntity<?> makeRecommendations() {
        recommendationService.makeRecommendations();
        return ResponseEntity.ok().body(null);
    }
}
