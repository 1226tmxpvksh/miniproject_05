package miniproject.infra;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import miniproject.domain.*;
import commondto.event.*;
import commondto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import miniproject.config.kafka.KafkaProcessor; // 중요!

@Service
@Transactional
@Slf4j
public class PolicyHandler {

    @Autowired
    private KafkaProcessor processor;

    // 구독 요청 이벤트 발행
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionRequested'")
    public void onSubscriptionRequested(@Payload SubscriptionRequested event) {
        if (!event.validate()) return;
        log.info("[Policy] SubscriptionRequested received: {}", event);

        SubscriptionRequestedDto dto = new SubscriptionRequestedDto(
            event.getUserId(),
            event.getRequestedAt()
        );
        Message<SubscriptionRequestedDto> message = MessageBuilder
            .withPayload(dto)
            .setHeader("type", "SubscriptionRequestedDto")
            .build();
        processor.outboundTopic().send(message); // event-out 바인딩 채널로 전송
        log.info("Kafka pub to topic [miniproject]: {}", dto);
    }

    // 구독 취소 요청 이벤트 발행
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='SubscriptionCancelRequested'")
    public void onSubscriptionCancelRequested(@Payload SubscriptionCancelRequested event) {
        if (!event.validate()) return;
        log.info("[Policy] SubscriptionCancelRequested received: {}", event);

        SubscriptionCancelRequestedDto dto = new SubscriptionCancelRequestedDto(
            event.getUserId(),
            event.getRequestedAt()
        );
        Message<SubscriptionCancelRequestedDto> message = MessageBuilder
            .withPayload(dto)
            .setHeader("type", "SubscriptionCancelRequestedDto")
            .build();
        processor.outboundTopic().send(message);
        log.info("Kafka pub to topic [miniproject]: {}", dto);
    }

    // 포인트 충전 요청 이벤트 발행
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='PointChargeRequested'")
    public void onPointChargeRequested(@Payload PointChargeRequested event) {
        if (!event.validate()) return;
        log.info("[Policy] PointChargeRequested received: {}", event);

        PointChargeRequestedDto dto = new PointChargeRequestedDto(
            event.getUserId(),
            event.getAmount()
        );
        Message<PointChargeRequestedDto> message = MessageBuilder
            .withPayload(dto)
            .setHeader("type", "PointChargeRequestedDto")
            .build();
        processor.outboundTopic().send(message);
        log.info("Kafka pub to topic [miniproject]: {}", dto);
    }

    // 작가 요청 이벤트 발행
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='WriterRequest'")
    public void onWriterRequest(@Payload WriterRequest event) {
        if (!event.validate()) return;
        log.info("[Policy] WriterRequest received: {}", event);

        WriterRequestDto dto = new WriterRequestDto(
            event.getUserId(),
            event.getRequestedAt()
        );
        Message<WriterRequestDto> message = MessageBuilder
            .withPayload(dto)
            .setHeader("type", "WriterRequestDto")
            .build();
        processor.outboundTopic().send(message);
        log.info("Kafka pub to topic [miniproject]: {}", dto);
    }

    // 계정 등록 이벤트 발행
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='Registered'")
    public void onUserRegistered(@Payload Registered event) {
        if (!event.validate()) return;
        log.info("[Policy] Registered received: {}", event);

        UserDto dto = new UserDto(
            event.getUserId(),
            event.getEmail(),
            event.getNickname()
        );
        Message<UserDto> message = MessageBuilder
            .withPayload(dto)
            .setHeader("type", "UserDto")
            .build();
        processor.outboundTopic().send(message);
        log.info("Kafka pub to topic [miniproject]: {}", dto);
    }
}







