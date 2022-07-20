package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.repository.AreaRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;

    private final UserRepository userRepository;

    @Transactional
    public void addAreaToUser(String email, Long areaId) throws Exception{
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        Area area = areaRepository.findById(areaId).orElse(null);
        if (area == null) {
            throw new Exception("Area does not exist!");
        }
        if (user.getAreas().contains(area)) {
            log.info("User already has area: {} - {}", area.getCity(), area.getDistrict());
            return;
        }
        user.addArea(area);
    }

    @Transactional
    public void deleteAreaFromUser(String email, Long areaId) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        Area area = areaRepository.findById(areaId).orElse(null);
        if (area == null) {
            throw new Exception("Area does not exist!");
        }
        user.removeArea(area);
    }


}
