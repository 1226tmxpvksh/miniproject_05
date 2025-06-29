package miniproject.infra;

import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.Book;
import miniproject.domain.CoverCreated;
import miniproject.domain.PubApproved;
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
        condition = "headers['type']=='PubApproved'"
    )
    public void wheneverPubApproved(@Payload PubApproved event) {
        if (event == null || event.getBookId() == null) return;
        log.info("📩 [PolicyHandler] PubApproved 수신됨: {}", event);
        Book.publishComplete(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CoverCreated'"
    )
    public void wheneverCoverCreated(@Payload CoverCreated event) {
        if (event == null || event.getBookId() == null) return;
        log.info("📩 [PolicyHandler] CoverCreated 수신됨: {}", event);
        Book.coverCandidatesReady(event);
    }
} 