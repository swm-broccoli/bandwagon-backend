package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserInfo;
import bandwagon.bandwagonback.dto.*;
import bandwagon.bandwagonback.dto.exception.DuplicateUserException;
import bandwagon.bandwagonback.dto.exception.notauthorized.FrontmanCannotLeaveException;
import bandwagon.bandwagonback.dto.exception.notauthorized.SocialAccountNotAuthorizedException;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
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
import java.util.Random;
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

    private final EmailService emailService;

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
    public void unregister(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.getBandMember() != null && user.getBandMember().getIsFrontman()) {
            throw new FrontmanCannotLeaveException();
        }
        userRepository.delete(user);
    }

    @Transactional
    public UserEditDto editUser(String email, UserEditRequest request) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
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
    public void editDescription(String email, String description) {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        UserInfo userInfo = user.getUserInfo();
        userInfo.setDescription(description);
    }

    @Transactional
    public String  uploadAvatar(String email, MultipartFile multipartFile) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
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
    public void validateDuplicateUser(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            log.error("Existing user: User = {}", email);
            throw new DuplicateUserException();
        }
    }

    public FindUserEmailDto findUserEmail(FindUserEmailRequest request) {
        List<User> users = userRepository.findAllByNameAndBirthday(request.getName(), request.getBirthday());
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<String> userEmails = users.stream().map(user -> user.getEmail().replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*")).collect(Collectors.toList());
        return new FindUserEmailDto(userEmails);
    }

    @Transactional
    public void findUserPassword(FindUserPasswordRequest request) {
        User user = userRepository.findByNameAndEmail(request.getName(), request.getEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.getIsSocial()) {
            throw new SocialAccountNotAuthorizedException();
        }
        String newRandomPassword = randomPasswordGen();
        user.setPassword(passwordEncoder.encode(newRandomPassword));
        String emailText = "밴드웨건에서 " + user.getEmail() + "계정 비밀번호 찾기 요청에 따라 임시 비밀번호를 발급해드립니다. \n 임시 비밀번호: " + newRandomPassword;
        emailService.sendSimpleMessage(user.getEmail(), "[Band:Wagon] 임시 비밀번호 발급", emailText);
    }

    public Band findBand(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
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
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    //회원 하나 조회 - email로
    public User findOneByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private String randomPasswordGen() {
        int len = 10;
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
