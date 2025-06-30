package miniproject.infra;

import javax.transaction.Transactional;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.Date; // Date 클래스 import 추가
import java.util.Optional; // Optional 클래스 import 추가

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 이 메서드는 User 서비스의 SubscribeCommand에 의해 직접 처리되므로,
    // 중복 실행을 막기 위해 주석 처리하거나 삭제하는 것을 권장합니다.
    // 만약 Kafka를 통해 비동기적으로 처리해야 한다면 그대로 둡니다.
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

        // findById의 결과는 Optional 입니다.
        Optional<Subscription> optionalSubscription = subscriptionRepository
            .findById(subscriptionRequested.getUserId());
            
        // findById 결과가 있으면 업데이트, 없으면 새로 생성
        Subscription subscription = optionalSubscription.orElseGet(() -> {
            Subscription s = new Subscription();
            s.setUserId(subscriptionRequested.getUserId());
            return s;
        });

        subscription.setSubscriptionStatus("SUBSCRIBED"); // "구독중" -> "SUBSCRIBED" 등 영문 상태명 권장

        // 구독 만료일 설정 (30일 뒤)
        subscription.setSubscriptionExpiryDate(new Date(
            System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)
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

        // findByUserId 메서드는 Optional<Subscription>을 반환해야 합니다.
        // SubscriptionRepository에 이 메서드가 선언되어 있는지 확인이 필요합니다.
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByUserId(bookViewed.getWriterId());

        // ifPresent를 사용하여 코드를 간결하게 수정
        optionalSubscription.ifPresent(subscription -> {
            if (
                "SUBSCRIBED".equals(subscription.getSubscriptionStatus()) &&
                subscription.getSubscriptionExpiryDate() != null &&
                subscription.getSubscriptionExpiryDate().after(new Date())
            ) {
                BookAccessGranted granted = new BookAccessGranted(subscription);
                granted.setBookId(bookViewed.getBookId()); // BookId 설정
                granted.publishAfterCommit();
            } else {
                BookAccessDenied denied = new BookAccessDenied(subscription);
                denied.setBookId(bookViewed.getBookId()); // BookId 설정
                denied.publishAfterCommit();
            }
        });
        
        // 구독 정보가 없을 경우 거부 이벤트 발행
        if (!optionalSubscription.isPresent()) {
            BookAccessDenied denied = new BookAccessDenied();
            denied.setUserId(bookViewed.getWriterId());
            denied.setBookId(bookViewed.getBookId());
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

        // ifPresent 문법을 올바르게 수정
        subscriptionRepository.findById(subscriptionCancelRequested.getUserId())
            .ifPresent(subscription -> {
                subscription.setSubscriptionStatus("CANCELLED"); // "해지됨" -> "CANCELLED" 등 영문 상태명 권장
                subscription.setSubscriptionExpiryDate(null);
                subscriptionRepository.save(subscription);

                SubscriptionCanceled canceled = new SubscriptionCanceled(subscription);
                canceled.publishAfterCommit();
            });
    }
}