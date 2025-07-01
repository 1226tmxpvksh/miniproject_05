package miniproject.infra;

public class NicknameAlreadyExistsException extends RuntimeException {
    public NicknameAlreadyExistsException(String nickname) {
        super("이미 사용 중인 닉네임입니다: " + nickname);
    }
}

