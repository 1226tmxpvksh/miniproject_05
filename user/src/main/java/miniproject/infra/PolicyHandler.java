package miniproject.infra;

import javax.transaction.Transactional;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

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
        condition = "headers['type']=='WriterQuested'"
    )
    public void wheneverWriterQuested_CreateWriter(@Payload WriterQuestCommand event) {
        if (event == null || event.getUserId() == null) return;

        // userId로 유저 닉네임 조회
        String nickname = null;
        User user = userRepository.findById(event.getUserId()).orElse(null);
        if (user != null) {
            nickname = user.getNickname();
        }

        try {
            String url = "http://localhost:8088/writers";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = new HashMap<>();
            body.put("userId", event.getUserId());
            body.put("nickname", nickname);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println(">> Writer created via REST: " + response.getStatusCode());
            System.out.println(">> Writer created response body: " + response.getBody());
        } catch (Exception e) {
            System.out.println(">> Writer creation failed: " + e.getMessage());
            e.printStackTrace();
        }
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
