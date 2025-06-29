package miniproject.domain;

import lombok.Data;
import miniproject.OpenaiApplication;
import miniproject.infra.AbstractEvent;

import javax.persistence.*;

@Entity
@Table(name = "OpenAi_table")
@Data
public class OpenAi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;

    private Long bookId;
    private String prompt;
    private String coverUrl;

    public static OpenAiRepository repository() {
        return OpenaiApplication.applicationContext.getBean(OpenAiRepository.class);
    }

    // ✅ CoverGenerationRequested 이벤트 수신 시 처리 로직
    public static void coverGenerationRequested(CoverGenerationRequested event) {
        OpenAi openAi = new OpenAi();
        openAi.setBookId(event.getBookId());

        // prompt 생성: 책 제목 + 내용으로 AI 요청 구성
        String prompt = "Generate a book cover for: " + event.getTitle() + " - " + event.getContent();
        openAi.setPrompt(prompt);

        // 표지 생성: 실제 AI API 연동이 없는 경우 임의 값 생성
        String coverUrl = "https://fake-cover.com/image/" + event.getBookId() + ".png";
        openAi.setCoverUrl(coverUrl);

        // 저장
        repository().save(openAi);

        // 이벤트 발행
        CoverCreated coverCreated = new CoverCreated(openAi);
        coverCreated.publishAfterCommit();
    }
}
