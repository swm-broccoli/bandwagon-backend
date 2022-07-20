package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserPerformance;
import bandwagon.bandwagonback.dto.UserPerformanceDto;
import bandwagon.bandwagonback.repository.UserPerformanceRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserPerformanceService {

    private final UserPerformanceRepository userPerformanceRepository;

    private final UserRepository userRepository;

    // 신규 연주기록 저장
    @Transactional
    public void saveUserPerformance(String email, UserPerformanceDto userPerformanceDto) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        UserPerformance userPerformance = new UserPerformance(userPerformanceDto);
        user.addUserPerformance(userPerformance);
        userPerformanceRepository.save(userPerformance);
    }

    // 기존 연주기록 삭제
    @Transactional
    public void deleteUserPerformance(Long userPerformanceId){
        userPerformanceRepository.deleteById(userPerformanceId);
    }

    // 기존 연주기록 업데이트
    @Transactional
    public void updateUserPerformance(Long userPerformanceId, UserPerformanceDto userPerformanceDto) throws Exception{
        UserPerformance userPerformance = userPerformanceRepository.findById(userPerformanceId).orElse(null);
        if(userPerformance == null) {
            throw new Exception("User Performance does not exist!");
        }
        userPerformance.update(userPerformanceDto);
    }

    // 유저의 모든 연주기록 조회
    public List<UserPerformance> findUserPerformancesByUser(User user) {
        return userPerformanceRepository.findByUser(user);
    }
}
