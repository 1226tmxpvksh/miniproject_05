package miniproject.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import miniproject.UserApplication;

@Entity
@Table(name = "User_table")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String email;

    private String nickname;

    private Integer amount;

    private Boolean isSubscribed = false;

    private Date subscriptionStart;

    private Date subscriptionEnd;

    // --- 이 부분을 추가하세요 ---
    private Boolean writerRequested;
    private Date writerRequestedAt;
    // --- 여기까지 추가 ---

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    public void register(RegisterCommand registerCommand) {
        this.email = registerCommand.getEmail();
        this.nickname = registerCommand.getNickname();
        this.amount = 0;
        this.isSubscribed = false;
        this.writerRequested = false; // 기본값 설정

        Registered registered = new Registered(this);
        registered.publishAfterCommit();
    }

    public void subscribe(SubscribeCommand subscribeCommand) {
        this.isSubscribed = true;
        this.subscriptionStart = new Date();

        Date end = new Date();
        end.setTime(this.subscriptionStart.getTime() + (1000L * 60 * 60 * 24 * 30));
        this.subscriptionEnd = end;

        SubscriptionRequested subscriptionRequested = new SubscriptionRequested(this);
        subscriptionRequested.publishAfterCommit();
    }

    // --- 이 메서드를 수정하세요 ---
    public void writerQuest(WriterQuestCommand writerQuestCommand) {
        // 작가 신청 상태를 변경하는 로직 추가
        this.setWriterRequested(true);
        this.setWriterRequestedAt(new Date());

        // 이벤트 발행
        WriterRequest writerRequest = new WriterRequest(this);
        writerRequest.publishAfterCommit();
    }
    // --- 여기까지 수정 ---

    public void cancelSubscription(CancelSubscriptionCommand cancelSubscriptionCommand) {
        this.isSubscribed = false;
        this.subscriptionStart = null;
        this.subscriptionEnd = null;

        SubscriptionCancelRequested subscriptionCancelRequested = new SubscriptionCancelRequested(this);
        subscriptionCancelRequested.publishAfterCommit();
    }

    public void chargePoint(ChargePointCommand chargePointCommand) {
        this.amount += chargePointCommand.getAmount();

        PointChargeRequested pointChargeRequested = new PointChargeRequested(this);
        pointChargeRequested.publishAfterCommit();
    }
}