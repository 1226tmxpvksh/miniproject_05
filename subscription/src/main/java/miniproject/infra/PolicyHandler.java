package miniproject.infra;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import miniproject.domain.*;
import commondto.event.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PolicyHandler {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // miniproject 토픽에서 DTO 이벤트 수신
    @StreamListener(value = "event-in", condition = "headers['type']=='SubscriptionRequestedDto'")
    public void onSubscriptionRequested(@Payload SubscriptionRequestedDto dto) {
        log.info("[Policy] SubscriptionRequestedDto received: {}", dto);

        Optional<Subscription> opt = subscriptionRepository.findByUserId(dto.getUserId());
        Subscription subscription = opt.orElseGet(() -> {
            Subscription s = new Subscription();
            s.setUserId(dto.getUserId());
            return s;
        });

        subscription.subscriptionRegister(new SubscriptionRegisterCommand(dto.getUserId()));
        subscriptionRepository.save(subscription);
    }

    @StreamListener(value = "event-in", condition = "headers['type']=='SubscriptionCancelRequestedDto'")
    public void onSubscriptionCancelRequested(@Payload SubscriptionCancelRequestedDto dto) {
        log.info("[Policy] SubscriptionCancelRequestedDto received: {}", dto);

        subscriptionRepository.findByUserId(dto.getUserId())
            .ifPresent(subscription -> {
                subscription.subscriptionCancel(new SubscriptionCancelCommand(dto.getUserId()));
                subscriptionRepository.save(subscription);
            });
    }

    @StreamListener(value = "event-in", condition = "headers['type']=='BookViewedDto'")
    public void onBookViewed(@Payload BookViewedDto dto) {
        log.info("[Policy] BookViewedDto received: {}", dto);

        //작가 본인: 무조건 허용
        if(dto.getUserId() != null && dto.getWriterId() != null && dto.getUserId().equals(dto.getWriterId())) {
            BookAccessGranted granted = new BookAccessGranted(dto.getUserId(), dto.getBookId());
            granted.publishAfterCommit();

            kafkaTemplate.send("book-access-granted", new SubscriptionGrantedDto(dto.getUserId(), dto.getBookId()));
            log.info("Kafka pub to topic [book-access-granted]: userId={}, bookId={}", dto.getUserId(), dto.getBookId());
            return;
        }

        Optional<Subscription> opt = subscriptionRepository.findByUserId(dto.getUserId());

        if (opt.isPresent()) {
            Subscription subscription = opt.get();
            boolean granted = subscription.checkSubscription(new CheckSubscriptionCommand(dto.getUserId(), dto.getBookId()), dto);

            if (granted) {
                // 허용됨 이벤트 pub
                kafkaTemplate.send("book-access-granted", new SubscriptionGrantedDto(dto.getUserId(), dto.getBookId()));
                log.info("Kafka pub to topic [book-access-granted]: userId={}, bookId={}", dto.getUserId(), dto.getBookId());
            } else {
                // 거절됨 이벤트 pub
                kafkaTemplate.send("book-access-denied", new SubscriptionDeniedDto(dto.getUserId(), dto.getBookId()));
                log.info("Kafka pub to topic [book-access-denied]: userId={}, bookId={}", dto.getUserId(), dto.getBookId());
            }
        } else {
            // 구독 정보 자체가 없으면 무조건 거절, 거절됨 pub
            BookAccessDenied denied = new BookAccessDenied(dto.getUserId(), dto.getBookId());
            denied.publishAfterCommit();

            kafkaTemplate.send("book-access-denied", new SubscriptionDeniedDto(dto.getUserId(), dto.getBookId()));
            log.info("Kafka pub to topic [book-access-denied]: userId={}, bookId={}", dto.getUserId(), dto.getBookId());
        }
    }
}






