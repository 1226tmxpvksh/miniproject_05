package miniproject.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyHandler {

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCoverCreated_thenHandle(@Payload CoverCreated coverCreated) {
        if (coverCreated == null || coverCreated.getBookId() == null) return;

        log.info("📘 CoverCreated 이벤트 수신: {}", coverCreated);

        // 출간 완료 처리 로직 호출
        Book.coverCandidatesReady(coverCreated);
    }
}
