package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.repository.AreaRepository;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
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

    private final BandRepository bandRepository;

    private final BandMemberRepository bandMemberRepository;

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
    //TODO: Area, genre, position 등 서비스에서 객체 속 리스트에 중복 있는지 검사하지 않고 객체 자체 메서드에서 검사하게 변경 (like I did in prerequisites)
    @Transactional
    public void addAreaToBand(String email, Long bandId, Long areaId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Area area = areaRepository.findById(areaId).orElse(null);
        if (area == null) {
            throw new Exception("Area does not exist!");
        }
        if (band.getAreas().contains(area)) {
            log.info("User already has area: {} - {}", area.getCity(), area.getDistrict());
            return;
        }
        band.addArea(area);
    }

    @Transactional
    public void deleteAreaFromBand(String email, Long bandId, Long areaId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Area area = areaRepository.findById(areaId).orElse(null);
        if (area == null) {
            throw new Exception("Area does not exist!");
        }
        band.removeArea(area);
    }
}
