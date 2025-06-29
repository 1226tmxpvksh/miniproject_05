package miniproject.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import miniproject.SubscriptionApplication;

@Entity
@Table(name = "Subscription_table")
@Data
//<<< DDD / Aggregate Root
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String subscriptionStatus; // SUBSCRIBED / CANCELED
    private Date subscriptionExpiryDate;

    public static SubscriptionRepository repository() {
        return SubscriptionApplication.applicationContext.getBean(SubscriptionRepository.class);
    }

    //<<< Clean Arch / Port Method
    public void checkSubscription(CheckSubscriptionCommand cmd) {
        if ("SUBSCRIBED".equals(this.subscriptionStatus)) {
            BookAccessGranted granted = new BookAccessGranted(this);
            granted.publishAfterCommit();
        } else {
            BookAccessDenied denied = new BookAccessDenied(this);
            denied.publishAfterCommit();
        }
    }

    public void subscriptionRegister(SubscriptionRegisterCommand cmd) {
        this.subscriptionStatus = "SUBSCRIBED";
        this.subscriptionExpiryDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30); // 30일 구독

        SubscriptionRegistered event = new SubscriptionRegistered(this);
        event.publishAfterCommit();
    }

    public void subscriptionCancel(SubscriptionCancelCommand cmd) {
        this.subscriptionStatus = "CANCELED";
        this.subscriptionExpiryDate = null;

        SubscriptionCanceled event = new SubscriptionCanceled(this);
        event.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

    public static void subscribe(SubscriptionRequested event) {
        Subscription subscription = new Subscription();
        subscription.setUserId(event.getUserId());
        subscription.setSubscriptionStatus("SUBSCRIBED");
        subscription.setSubscriptionExpiryDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30));

        repository().save(subscription);

        SubscriptionRegistered registered = new SubscriptionRegistered(subscription);
        registered.publishAfterCommit();
    }

    public static void subscriptionCancel(SubscriptionCancelRequested event) {
        repository().findByUserId(event.getUserId()).ifPresent(subscription -> {
            subscription.setSubscriptionStatus("CANCELED");
            subscription.setSubscriptionExpiryDate(null);

            repository().save(subscription);

            SubscriptionCanceled canceled = new SubscriptionCanceled(subscription);
            canceled.publishAfterCommit();
        });
    }

    public static void checkSubscription(BookViewed event) {
        repository().findByUserId(event.getWriterId()).ifPresent(subscription -> {
            if ("SUBSCRIBED".equals(subscription.getSubscriptionStatus())) {
                BookAccessGranted granted = new BookAccessGranted(subscription);
                granted.setBookId(event.getBookId());
                granted.publishAfterCommit();
            } else {
                BookAccessDenied denied = new BookAccessDenied(subscription);
                denied.setBookId(event.getBookId());
                denied.publishAfterCommit();
            }
        });
    }
}