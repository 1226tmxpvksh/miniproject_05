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
    WriterRepository writerRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
        // 기본 이벤트 수신 처리 (디버깅용)
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='WriterRequest'"
    )
    public void wheneverWriterRequest_WriterRequest(
        @Payload WriterRequest writerRequest
    ) {
        System.out.println(
            "\n\n##### listener WriterRequest : " + writerRequest + "\n\n"
        );
        System.out.println("📩 [PolicyHandler] WriterRequest 수신됨: " + writerRequest);
        Writer.writerRequest(writerRequest);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublishRequested'"
    )
    public void wheneverPublishRequested_PublishRequest(
        @Payload PublishRequested publishRequested
    ) {
        System.out.println(
            "\n\n##### listener PublishRequest : " + publishRequested + "\n\n"
        );
        Writer.publishRequest(publishRequested);
    }
}
