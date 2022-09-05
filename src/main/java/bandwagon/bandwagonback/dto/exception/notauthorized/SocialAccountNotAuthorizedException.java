package bandwagon.bandwagonback.dto.exception.notauthorized;

public class SocialAccountNotAuthorizedException extends NotAuthorizedException {

    public SocialAccountNotAuthorizedException() {
        super("소셜 로그인 계정은 해당 동작을 수행할 수 없습니다.");
    }
}
