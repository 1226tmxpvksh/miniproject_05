package miniproject.infra;

import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionStatusCheckViewHandler {

    @Autowired
    private SubscriptionStatusCheckRepository subscriptionStatusCheckRepository;

    //구독 등록 이벤트 수신 리드 모델 생성/업데이트
    @StreamListener(KafkaProcessor.INPUT)
    public void handleSubscriptionRegistered(@Payload SubscriptionRegistered event) {
        if (event == null || !event.validate()) return;

        SubscriptionStatusCheck view = new SubscriptionStatusCheck();
        view.setUserId(event.getUserId());
        view.setSubscriptionStatus(SubscriptionStatus.ACTIVE.name());
        if (event.getSubscriptionExpiryDate() != null) {
            view.setSubscriptionExpireDate(event.getSubscriptionExpiryDate().toString());
        }

        subscriptionStatusCheckRepository.save(view);
    }

    //구독 취소 이벤트 수신 리드 모델 상태 업데이트
    @StreamListener(KafkaProcessor.INPUT)
    public void handleSubscriptionCanceled(@Payload SubscriptionCanceled event) {
        if (event == null || !event.validate()) return;

        subscriptionStatusCheckRepository.findByUserId(event.getUserId())
            .ifPresent(view -> {
                //상태명 INACTIVE로 통일 (Enum 기준)
                view.setSubscriptionStatus(SubscriptionStatus.INACTIVE.name());
                view.setSubscriptionExpireDate(null);
                subscriptionStatusCheckRepository.save(view);
            });
    }
}
