package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserInfo;
import bandwagon.bandwagonback.dto.PasswordEditRequest;
import bandwagon.bandwagonback.dto.SignUpRequest;
import bandwagon.bandwagonback.dto.UserEditDto;
import bandwagon.bandwagonback.dto.UserEditRequest;
import bandwagon.bandwagonback.repository.UserInfoRepository;
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

    private final UserInfoRepository userInfoRepository;

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
        log.info("User Saved");
        UserInfo userInfo = new UserInfo();
        log.info("UserInfo created");
        userInfo.setUser(user);
        log.info("UserInfo's user set");
        userInfoRepository.save(userInfo);
        log.info("UserInfo saved");
        return user.getId();
    }

    @Transactional
    public UserEditDto editUser(UserEditRequest request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("비밀번호가 올바르지 않습니다!");
        }
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setBirthday(request.getBirthday());
        user.setGender(request.getGender());
        return new UserEditDto(user);
    }

    @Transactional
    public void editPassword(String email, PasswordEditRequest request) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new Exception("비밀번호가 올바르지 않습니다!");
        }
        if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
            throw new Exception("신규 비밀번호를 동일하게 입력해주세요.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
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
