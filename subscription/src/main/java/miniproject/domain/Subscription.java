package miniproject.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import miniproject.SubscriptionApplication;
import commondto.event.BookViewedDto;
import commondto.event.SubscriptionCancelRequestedDto;
import commondto.event.SubscriptionRequestedDto;

@Entity
@Table(name = "Subscription_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus; // ACTIVE, INACTIVE

    private Date subscriptionExpiryDate;

    public static SubscriptionRepository repository() {
        return SubscriptionApplication.applicationContext.getBean(SubscriptionRepository.class);
    }

    //구독권한 체크: 허용이면 true, 아니면 false만 반환! (이벤트 pub은 PolicyHandler에서)
    public boolean checkSubscription(CheckSubscriptionCommand cmd, BookViewedDto bookViewedDto) {
        //저자 본인인 경우 무조건 허용
        if (cmd.getUserId().equals(bookViewedDto.getWriterId())) {
            return true;
        }
        //구독상태 분기 (ACTIVE + 만료일이 오늘 이후)
        if (this.subscriptionStatus == SubscriptionStatus.ACTIVE
            && this.subscriptionExpiryDate != null
            && this.subscriptionExpiryDate.after(new Date())) {
            return true;
        }
        //구독 미가입/만료 → 거절
        return false;
    }

    // 구독 등록 커맨드 처리
    public void subscriptionRegister(SubscriptionRegisterCommand cmd) {
        this.subscriptionStatus = SubscriptionStatus.ACTIVE;
        this.subscriptionExpiryDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);

        SubscriptionRegistered event = new SubscriptionRegistered(this);
        event.publishAfterCommit();
    }

    // 구독 취소 커맨드 처리
    public void subscriptionCancel(SubscriptionCancelCommand cmd) {
        this.subscriptionStatus = SubscriptionStatus.INACTIVE;
        this.subscriptionExpiryDate = null;

        SubscriptionCanceled event = new SubscriptionCanceled(this);
        event.publishAfterCommit();
    }

    // 구독요청 이벤트 수신 시 커맨드 실행
    public static void subscribe(SubscriptionRequestedDto event) {
        Subscription subscription = new Subscription();
        subscription.setUserId(event.getUserId());
        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        subscription.setSubscriptionExpiryDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30));

        repository().save(subscription);

        SubscriptionRegistered registered = new SubscriptionRegistered(subscription);
        registered.publishAfterCommit();
    }

    // 구독취소요청 이벤트 수신 시 커맨드 실행
    public static void subscriptionCancel(SubscriptionCancelRequestedDto event) {
        repository().findByUserId(event.getUserId()).ifPresent(subscription -> {
            subscription.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
            subscription.setSubscriptionExpiryDate(null);

            repository().save(subscription);

            SubscriptionCanceled canceled = new SubscriptionCanceled(subscription);
            canceled.publishAfterCommit();
        });
    }
}







