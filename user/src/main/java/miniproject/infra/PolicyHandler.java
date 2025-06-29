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
    UserRepository userRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookViewed'"
    )
    public void wheneverBookViewed_HandlePointReward(@Payload BookViewed bookViewed) {
        // (선택적) 책을 본 사용자에게 포인트를 적립해주는 등의 정책이 있다면 구현
        System.out.println(">> Policy triggered by BookViewed: " + bookViewed);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BestsellerSelected'"
    )
    public void wheneverBestsellerSelected_NotifyUser(@Payload BestsellerSelected bestsellerSelected) {
        // (선택적) 베스트셀러 작가에게 알림을 보낼 수 있음
        System.out.println(">> Policy: Notify user about bestseller selection: " + bestsellerSelected);
    }
}
