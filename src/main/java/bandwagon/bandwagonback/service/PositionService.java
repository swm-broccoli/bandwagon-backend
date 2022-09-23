package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.exception.notfound.PositionNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
import bandwagon.bandwagonback.repository.PositionRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    private final UserRepository userRepository;

    @Transactional
    public void addPositionToUser(String email, Long positionId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position == null) {
            throw new PositionNotFoundException();
        }
        if (user.getPositions().contains(position)) {
            log.info("User already has position: {}", position.getPosition());
            return;
        }
        user.addPosition(position);
    }

    @Transactional
    public void deletePositionFromUser(String email, Long positionId) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position == null) {
            throw new PositionNotFoundException();
        }
        user.removePosition(position);
    }
}
