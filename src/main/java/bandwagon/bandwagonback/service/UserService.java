package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.SignUpRequest;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(SignUpRequest request) throws Exception {
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            log.error("Password mismatch: Input1 = {}, Input2 = {}", request.getPassword(), request.getPasswordCheck());
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
        validateDuplicateUser(request.getEmail());
        //password Encrypt
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = new User(request);
        userRepository.save(user);
        return user.getId();
    }

    // 이메일로 회원 중복 검사
    public void validateDuplicateUser(String email) throws Exception {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            log.error("Existing user: User = {}", email);
            throw new Exception("이미 존재하는 회원입니다");
        }
    }

    //회원 전체 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    //회원 하나 조회 - table id로
    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    //회원 하나 조회 - email로
    public User findOneByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


}
