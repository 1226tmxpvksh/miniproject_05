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
//<<< DDD / Aggregate Root
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

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public void register(RegisterCommand registerCommand) {
        this.email = registerCommand.getEmail();
        this.nickname = registerCommand.getNickname();
        this.amount = 0;
        this.isSubscribed = false;

        Registered registered = new Registered(this);
        registered.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public void subscribe(SubscribeCommand subscribeCommand) {
        this.isSubscribed = true;
        this.subscriptionStart = new Date();

        // 기본 30일 구독 예시
        Date end = new Date();
        end.setTime(this.subscriptionStart.getTime() + (1000L * 60 * 60 * 24 * 30));
        this.subscriptionEnd = end;

        SubscriptionRequested subscriptionRequested = new SubscriptionRequested(this);
        subscriptionRequested.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public void writerQuest(WriterQuestCommand writerQuestCommand) {
        WriterRequest writerRequest = new WriterRequest(this);
        writerRequest.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public void cancelSubscription(CancelSubscriptionCommand cancelSubscriptionCommand) {
        this.isSubscribed = false;
        this.subscriptionStart = null;
        this.subscriptionEnd = null;

        SubscriptionCancelRequested subscriptionCancelRequested = new SubscriptionCancelRequested(this);
        subscriptionCancelRequested.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public void chargePoint(ChargePointCommand chargePointCommand) {
        this.amount += chargePointCommand.getAmount();

        PointChargeRequested pointChargeRequested = new PointChargeRequested(this);
        pointChargeRequested.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
