package miniproject.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.PrePersist;

import miniproject.UserApplication;

@Entity
@Table(name = "User_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String email;

    private String nickname;

    @Column(nullable = false)
    private Integer amount = 0;
    private Boolean isSubscribed = false;
    private Boolean writerRequested = false;

    private Date subscriptionStart;
    private Date subscriptionEnd;
    private Date writerRequestedAt;

    public User() {}

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    @PrePersist
    public void prePersist() {
        if (this.amount == null) this.amount = 0;
    }

    public void register(RegisterCommand registerCommand) {
        this.email = registerCommand.getEmail();
        this.nickname = registerCommand.getNickname();
    }

    public void subscribe(SubscribeCommand subscribeCommand) {
        this.isSubscribed = true;
        this.subscriptionStart = new Date();

        Date end = new Date();
        end.setTime(this.subscriptionStart.getTime() + (1000L * 60 * 60 * 24 * 30));
        this.subscriptionEnd = end;
    }

    public void writerQuest(WriterQuestCommand writerQuestCommand) {
        this.writerRequested = true;
        this.writerRequestedAt = new Date();

        // WriterRequest(WriterQuested) 이벤트 발행
        WriterRequest writerRequest = new WriterRequest();
        writerRequest.setUserId(this.userId);
        writerRequest.publishAfterCommit();
    }

    public void cancelSubscription(CancelSubscriptionCommand cancelSubscriptionCommand) {
        this.isSubscribed = false;
        this.subscriptionStart = null;
        this.subscriptionEnd = null;
    }

    public void chargePoint(ChargePointCommand chargePointCommand) {
        this.amount += chargePointCommand.getAmount();
    }

    // Getter/Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public Boolean getIsSubscribed() { return isSubscribed; }
    public void setIsSubscribed(Boolean isSubscribed) { this.isSubscribed = isSubscribed; }

    public Boolean getWriterRequested() { return writerRequested; }
    public void setWriterRequested(Boolean writerRequested) { this.writerRequested = writerRequested; }

    public Date getSubscriptionStart() { return subscriptionStart; }
    public void setSubscriptionStart(Date subscriptionStart) { this.subscriptionStart = subscriptionStart; }

    public Date getSubscriptionEnd() { return subscriptionEnd; }
    public void setSubscriptionEnd(Date subscriptionEnd) { this.subscriptionEnd = subscriptionEnd; }

    public Date getWriterRequestedAt() { return writerRequestedAt; }
    public void setWriterRequestedAt(Date writerRequestedAt) { this.writerRequestedAt = writerRequestedAt; }
}
