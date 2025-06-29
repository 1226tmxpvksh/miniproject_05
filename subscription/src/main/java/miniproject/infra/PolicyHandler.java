package miniproject.infra;

import javax.transaction.Transactional;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionRequested'"
    )
    public void wheneverSubscriptionRequested_Subscribe(
        @Payload SubscriptionRequested subscriptionRequested
    ) {
        System.out.println(
            "\n\n##### listener Subscribe : " + subscriptionRequested + "\n\n"
        );

        // 신규 구독 등록 처리
        Subscription subscription = subscriptionRepository
            .findById(subscriptionRequested.getUserId())
            .orElseGet(() -> {
                Subscription s = new Subscription();
                s.setUserId(subscriptionRequested.getUserId());
                return s;
            });

        subscription.setSubscriptionStatus("구독중");

        // 구독 만료일 설정 (30일 뒤)
        subscription.setSubscriptionExpiryDate(new java.util.Date(
            System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30
        ));

        subscriptionRepository.save(subscription);

        // 이벤트 발행
        SubscriptionRegistered registered = new SubscriptionRegistered(subscription);
        registered.publishAfterCommit();
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookViewed'"
    )
    public void wheneverBookViewed_CheckSubscription(
        @Payload BookViewed bookViewed
    ) {
        System.out.println(
            "\n\n##### listener CheckSubscription : " + bookViewed + "\n\n"
        );

        Subscription subscription = subscriptionRepository.findByUserId(bookViewed.getWriterId());

        if (
            subscription != null &&
            "구독중".equals(subscription.getSubscriptionStatus()) &&
            subscription.getSubscriptionExpiryDate() != null &&
            subscription.getSubscriptionExpiryDate().after(new java.util.Date())
        ) {
            BookAccessGranted granted = new BookAccessGranted(subscription);
            granted.publishAfterCommit();
        } else {
            BookAccessDenied denied = new BookAccessDenied(subscription != null ? subscription : new Subscription());
            denied.publishAfterCommit();
        }
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SubscriptionCancelRequested'"
    )
    public void wheneverSubscriptionCancelRequested_SubscriptionCancel(
        @Payload SubscriptionCancelRequested subscriptionCancelRequested
    ) {
        System.out.println(
            "\n\n##### listener SubscriptionCancel : " +
            subscriptionCancelRequested +
            "\n\n"
        );

        subscriptionRepository.findById(subscriptionCancelRequested.getUserId()).ifPresent(subscription -> {
            subscription.setSubscriptionStatus("해지됨");
            subscription.setSubscriptionExpiryDate(null);
            subscriptionRepository.save(subscription);

            SubscriptionCanceled canceled = new SubscriptionCanceled(subscription);
            canceled.publishAfterCommit();
        });
    }
}
