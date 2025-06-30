package miniproject.infra;

import java.io.IOException;
import java.util.Optional;
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

    @StreamListener(KafkaProcessor.INPUT)
    public void whenSubscriptionRegistered_then_CREATE_1(
        @Payload SubscriptionRegistered subscriptionRegistered
    ) {
        try {
            if (!subscriptionRegistered.validate()) return;

            // view 객체 생성
            SubscriptionStatusCheck subscriptionStatusCheck = new SubscriptionStatusCheck();
            
            // 데이터 매핑
            subscriptionStatusCheck.setUserId(subscriptionRegistered.getUserId());
            subscriptionStatusCheck.setSubscriptionStatus(SubscriptionStatus.ACTIVE.name());
            
            if (subscriptionRegistered.getSubscriptionExpiryDate() != null) {
                subscriptionStatusCheck.setSubscriptionExpireDate(
                    subscriptionRegistered.getSubscriptionExpiryDate().toString()
                );
            }
            
            // view 레파지토리를 통해 저장
            subscriptionStatusCheckRepository.save(subscriptionStatusCheck);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenSubscriptionCanceled_then_UPDATE_1(
        @Payload SubscriptionCanceled subscriptionCanceled
    ) {
        try {
            if (!subscriptionCanceled.validate()) return;

            // view 레파지토리를 이용해 수정할 대상 조회
            subscriptionStatusCheckRepository.findByUserId(subscriptionCanceled.getUserId())
                .ifPresent(subscriptionStatusCheck -> {
                    // 데이터 매핑
                    subscriptionStatusCheck.setSubscriptionStatus(SubscriptionStatus.CANCELLED.name());
                    subscriptionStatusCheck.setSubscriptionExpireDate(null); // 만료일 초기화
                    
                    // view 레파지토리를 통해 저장
                    subscriptionStatusCheckRepository.save(subscriptionStatusCheck);
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}