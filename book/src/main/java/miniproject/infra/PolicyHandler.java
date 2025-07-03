package miniproject.infra;

import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.Book;
import miniproject.domain.CoverCreated;
import miniproject.domain.PubApproved;
import miniproject.domain.BookViewed;
import miniproject.domain.BestSellerSelected;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private RestTemplate restTemplate;

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PubApproved'"
    )
    public void wheneverPubApproved(@Payload PubApproved event) {
        if (event == null || event.getBookId() == null) return;
        System.out.println("📩 [PolicyHandler] PubApproved 수신됨: " + event);
        log.info("📩 [PolicyHandler] PubApproved 수신됨: {}", event);
        Book.publishComplete(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookViewed'"
    )
    public void wheneverBookViewed(@Payload BookViewed event) {
        if (event == null || event.getBookId() == null) return;

        Long userId = event.getWriterId(); // 또는 event에서 실제 조회자 ID를 받아야 함

        // 1. 구독 여부 확인 (동기 REST 호출)
        boolean isSubscribed = false;
        try {
            String url = "http://localhost:8085/subscriptions/check?userId=" + userId;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            isSubscribed = Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            isSubscribed = false;
        }

        if (isSubscribed) {
            // 구독자면 포인트 차감 없이 접근 허용
            if (event.getViewCount() != null && event.getViewCount() >= 5) {
                BestSellerSelected bestSellerSelected = new BestSellerSelected();
                bestSellerSelected.setBookId(event.getBookId());
                bestSellerSelected.setTitle(event.getTitle());
                bestSellerSelected.setCoverUrl(event.getCoverUrl());
                bestSellerSelected.setViewCount(event.getViewCount());
                bestSellerSelected.setWriterId(event.getWriterId());
                bestSellerSelected.publishAfterCommit();
            }
        } else {
            // 비구독자면 포인트 차감 REST 호출
            try {
                String url = "http://localhost:8087/points/deduct";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                Map<String, Object> body = new HashMap<>();
                body.put("userId", userId);
                body.put("amount", 100); // 예시: 100포인트 차감
                HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                // 포인트 차감 성공 시 베스트셀러 선정
                if (response.getStatusCode().is2xxSuccessful()) {
                    if (event.getViewCount() != null && event.getViewCount() >= 5) {
                        BestSellerSelected bestSellerSelected = new BestSellerSelected();
                        bestSellerSelected.setBookId(event.getBookId());
                        bestSellerSelected.setTitle(event.getTitle());
                        bestSellerSelected.setCoverUrl(event.getCoverUrl());
                        bestSellerSelected.setViewCount(event.getViewCount());
                        bestSellerSelected.setWriterId(event.getWriterId());
                        bestSellerSelected.publishAfterCommit();
                    }
                }
            } catch (Exception e) {
                // 포인트 차감 실패 시 아무것도 하지 않음(접근 거부)
            }
        }
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
