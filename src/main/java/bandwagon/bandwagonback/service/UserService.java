package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserInfo;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.repository.UserInfoRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    private final S3Uploader s3Uploader;

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
    public void unregister(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        if (user.getBandMember() != null && user.getBandMember().getIsFrontman()) {
            throw new Exception("밴드의 프런트맨입니다! 프런트맨을 넘기시거나 밴드를 해체해 주세요.");
        }
        userRepository.delete(user);
    }

    @Transactional
    public UserEditDto editUser(String email, UserEditRequest request) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        if (request.getOldPassword() != null && !request.getOldPassword().equals("")) {
            editPassword(user, request);
        } else {
            user.updateUser(request);
        }
        return new UserEditDto(user);
    }

    @Transactional
    public void editPassword(User user, UserEditRequest request) throws Exception {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new Exception("비밀번호가 올바르지 않습니다!");
        }
        if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
            throw new Exception("신규 비밀번호를 동일하게 입력해주세요.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void editDescription(String email, String description) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        UserInfo userInfo = user.getUserInfo();
        userInfo.setDescription(description);
    }

    @Transactional
    public String  uploadAvatar(String email, MultipartFile multipartFile) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        UserInfo userInfo = user.getUserInfo();
        if (userInfo.getAvatarUrl() != null && userInfo.getAvatarUrl().length() != 0) {
            s3Uploader.deleteFromS3(userInfo.getAvatarUrl().replace(File.separatorChar, '/'));
        }
        String imgUrl = s3Uploader.upload(multipartFile, "user/avatar");
        userInfo.setAvatarUrl(imgUrl);
        return imgUrl;
    }

    // 이메일로 회원 중복 검사
    public void validateDuplicateUser(String email) throws Exception {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            log.error("Existing user: User = {}", email);
            throw new Exception("이미 존재하는 회원입니다");
        }
    }

    public FindUserEmailDto findUserEmail(FindUserEmailRequest request) throws Exception {
        List<User> users = userRepository.findAllByNameAndBirthday(request.getName(), request.getBirthday());
        if (users.isEmpty()) {
            throw new Exception("해당 이름과 생일 정보로 가입한 유저가 없습니다!");
        }
        List<String> userEmails = users.stream().map(user -> user.getEmail().replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*")).collect(Collectors.toList());
        return new FindUserEmailDto(userEmails);
    }

    public Band findBand(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        if (user.getBandMember() != null) {
            return user.getBandMember().getBand();
        }
        else {
            return null;
        }
    }

    //회원 전체 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    //회원 하나 조회 - table id로
    public User findOne(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    //회원 하나 조회 - email로
    public User findOneByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
