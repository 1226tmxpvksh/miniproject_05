package miniproject.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import miniproject.UserApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String email;
    private String nickname;
    private String passwordHash;  //비밀번호 해시 저장용 필드

    public static UserRepository repository() {
        return UserApplication.applicationContext.getBean(UserRepository.class);
    }

    public void register(RegisterCommand registerCommand) {
        this.email = registerCommand.getEmail();
        this.nickname = registerCommand.getNickname();

        //비밀번호 해시화 후 저장
        PasswordEncoder passwordEncoder =
            UserApplication.applicationContext.getBean(PasswordEncoder.class);
        this.passwordHash = passwordEncoder.encode(registerCommand.getPassword());

        Registered registered = new Registered(this);
        registered.publishAfterCommit();
    }

    public void chargePoint(ChargePointCommand chargePointCommand) {
        int value = chargePointCommand.getAmount();
        if (value <= 0) throw new IllegalArgumentException("충전 포인트는 양수여야 합니다.");

        PointChargeRequested event = new PointChargeRequested(this.userId, value);
        event.publishAfterCommit();
    }

    public void writerQuest(WriterQuestCommand writerQuestCommand) {
        WriterRequest writerRequest = new WriterRequest(this, writerQuestCommand);
        writerRequest.publishAfterCommit();
    }

    public void subscribe(SubscribeCommand subscribeCommand) {
        SubscriptionRequested event =
            new SubscriptionRequested(this.userId, subscribeCommand.getRequestedAt());
        event.publishAfterCommit();
    }

    public void cancelSubscription(CancelSubscriptionCommand cancelSubscriptionCommand) {
        SubscriptionCancelRequested event =
            new SubscriptionCancelRequested(this.userId, cancelSubscriptionCommand.getRequestedAt());
        event.publishAfterCommit();
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.passwordHash);
    }
}
//>>> DDD / Aggregate Root

