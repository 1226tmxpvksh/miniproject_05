package miniproject.infra;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("로그인이 필요합니다.");
    }
}