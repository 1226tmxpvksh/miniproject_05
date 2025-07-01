package miniproject.infra;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class PolicyHandler {

    //1. 포인트 충전 요청 이벤트 수신
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='PointChargeRequested'")
    public void onPointChargeRequested(@Payload PointChargeRequested event) {
        if (!event.validate()) return;
        log.info("[Policy] PointChargeRequested received: {}", event);

        // 포인트 바운더리에 커맨드 전달 (FeignClient or Kafka 발행 예정)
        // 예: pointService.chargePoint(event.getUserId(), event.getAmount());
    }

    //2. 구독 신청 요청 이벤트 수신
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionRequested'")
    public void onSubscriptionRequested(@Payload SubscriptionRequested event) {
        if (!event.validate()) return;
        log.info("[Policy] SubscriptionRequested received: {}", event);

        // 구독 바운더리에 구독 등록 요청
        // 예: subscriptionService.register(event.getUserId(), event.getRequestedAt());
    }

    //3. 구독 취소 요청 이벤트 수신
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionCancelRequested'")
    public void onSubscriptionCancelRequested(@Payload SubscriptionCancelRequested event) {
        if (!event.validate()) return;
        log.info("[Policy] SubscriptionCancelRequested received: {}", event);

        // 구독 바운더리에 구독 해지 요청
        // 예: subscriptionService.cancel(event.getUserId(), event.getRequestedAt());
    }

    //4. 작가 신청 요청 이벤트 수신
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='WriterRequest'")
    public void onWriterRequest(@Payload WriterRequest event) {
        if (!event.validate()) return;
        log.info("[Policy] WriterRequest received: {}", event);

        // 어드민 시스템에 전달, 이후 WriterApprove / WriterReject 커맨드 발생
        // 예: writerApprovalQueue.send(event);
    }
}
