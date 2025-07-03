package miniproject.infra;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.CoverGenerationRequested;
import miniproject.service.DalleApiService; // service 패키지를 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private DalleApiService dalleApiService;

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CoverGenerationRequested'"
    )
    public void wheneverCoverGenerationRequested_CreateCover(
        @Payload CoverGenerationRequested event
    ) {
        if (event == null) return;
        log.info("📩 [PolicyHandler] CoverGenerationRequested 수신: {}", event.getBookId());

        // DALL-E 3에 전달할 프롬프트 생성
        String prompt = String.format(
            "A professional and artistic book cover for a novel titled '%s'. The story is about: '%s'. Style: minimalist, dramatic, symbolic.",
            event.getTitle(),
            // 내용은 최대 200자로 제한하여 전달
            event.getContent().substring(0, Math.min(event.getContent().length(), 200))
        );

        // 서비스 호출
        dalleApiService.generateImageAndPublishEvent(event.getBookId(), prompt);
    }
}