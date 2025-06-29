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
    PointRepository pointRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
        // 아무 이벤트나 받는 용도 (로깅, 디버깅 시 사용 가능)
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookAccessDenied'"
    )
    public void wheneverBookAccessDenied_CheckPoint(
        @Payload BookAccessDenied bookAccessDenied
    ) {
        System.out.println(
            "\n\n##### listener CheckPoint : " + bookAccessDenied + "\n\n"
        );
        Point.checkPoint(bookAccessDenied);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointChargeRequested'"
    )
    public void wheneverPointChargeRequested_ChargePoint(
        @Payload PointChargeRequested pointChargeRequested
    ) {
        System.out.println(
            "\n\n##### listener ChargePoint : " + pointChargeRequested + "\n\n"
        );
        Point.chargePoint(pointChargeRequested);
    }
}
