package miniproject.infra;

import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.CoverGenerationRequested;
import miniproject.domain.OpenAi;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class PolicyHandler {

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CoverGenerationRequested'"
    )
    public void wheneverCoverGenerationRequested(@Payload CoverGenerationRequested event) {
        if (event == null || event.getBookId() == null) return;
        log.info("📩 [PolicyHandler] CoverGenerationRequested 수신됨: {}", event);
        OpenAi.coverGenerationRequested(event);
    }
}