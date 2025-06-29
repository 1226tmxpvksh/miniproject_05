package miniproject;

import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.BookAccessGranted;
import miniproject.domain.BestSeller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

@SpringBootApplication
public class BestsellerApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(BestsellerApplication.class, args);
    }

    // ✅ BookAccessGranted 이벤트를 수신하여 viewCount 증가 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBookAccessGranted_IncreaseViewCount(@Payload BookAccessGranted event) {
        if (event.validate()) {
            System.out.println("📚 Bestseller Policy Triggered: " + event.toJson());
            BestSeller.viewCount(event);
        }
    }

    // (선택) PointDeducted 이벤트 대응 가능
    /*
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPointDeducted_MaybeUpdate(@Payload PointDeducted event) {
        if (event.validate()) {
            System.out.println("💸 PointDeducted Received: " + event.toJson());
            BestSeller.viewCount(event);
        }
    }
    */
}
