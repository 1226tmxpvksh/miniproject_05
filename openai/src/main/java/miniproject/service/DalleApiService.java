package miniproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import miniproject.domain.CoverCreated;
import miniproject.domain.OpenAi;
import miniproject.domain.OpenAiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Slf4j
@Service
public class DalleApiService {

    private final WebClient webClient;
    private final OpenAiRepository openAiRepository;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String dalleApiUrl;

    @Autowired
    public DalleApiService(
        WebClient.Builder webClientBuilder,
        OpenAiRepository openAiRepository,
        ObjectMapper objectMapper
    ) {
        this.webClient = webClientBuilder.build();
        this.openAiRepository = openAiRepository;
        this.objectMapper = objectMapper;
    }

    public void generateImageAndPublishEvent(Long bookId, String prompt) {
        log.info("DALL-E 3 API 요청 시작. bookId: {}, prompt: {}", bookId, prompt);

        Map<String, Object> body = Map.of(
            "model", "dall-e-3",
            "prompt", prompt,
            "n", 1,
            "size", "1024x1024",
            "quality", "standard"
        );

        webClient.post()
            .uri(dalleApiUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(
                responseJson -> handleSuccess(responseJson, bookId, prompt),
                this::handleError
            );
    }

    private void handleSuccess(String responseJson, Long bookId, String prompt) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseJson);
            String imageUrl = rootNode.path("data").get(0).path("url").asText();
            log.info("✅ DALL-E 3 API 성공. Image URL: {}", imageUrl);

            OpenAi openAiEntity = new OpenAi();
            openAiEntity.setBookId(bookId);
            openAiEntity.setPrompt(prompt);
            openAiEntity.setCoverUrl(imageUrl);
            openAiRepository.save(openAiEntity);

            CoverCreated coverCreatedEvent = new CoverCreated(openAiEntity);
            coverCreatedEvent.publish(); 
        } catch (Exception e) {
            log.error("❌ DALL-E 3 응답 파싱 실패", e);
        }
    }

    private void handleError(Throwable error) {
        log.error("❌ DALL-E 3 API 호출 실패: {}", error.getMessage());
    }
}