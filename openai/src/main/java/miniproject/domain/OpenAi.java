package miniproject.domain;

import lombok.Data;
import miniproject.OpenaiApplication;
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
    
    @PostPersist
    public void onPostPersist() {
        // 필요한 경우, 엔티티가 처음 저장될 때 이벤트를 발행할 수 있습니다.
        // 예: CoverCreated 이벤트 발행
        // CoverCreated coverCreated = new CoverCreated(this);
        // coverCreated.publishAfterCommit();
    }

    // ✅ CoverGenerationRequested 이벤트 수신 시 처리 로직
    public static void coverGenerationRequested(CoverGenerationRequested event) {
        OpenAi openAi = new OpenAi();
        openAi.setBookId(event.getBookId());

        String prompt = "Generate a book cover for: " + event.getTitle() + " - " + event.getContent();
        openAi.setPrompt(prompt);

        String coverUrl = "https://fake-cover.com/image/" + event.getBookId() + ".png";
        openAi.setCoverUrl(coverUrl);

        repository().save(openAi);

        CoverCreated coverCreated = new CoverCreated(openAi);
        coverCreated.publishAfterCommit();
    }
    
    // ✅ Controller에서 호출할 Command 처리 메서드 (추가된 부분)
    public void bookCoverCreate(BookCoverCreateCommand bookCoverCreateCommand) {
        this.setBookId(bookCoverCreateCommand.getBookId());
        this.setPrompt(bookCoverCreateCommand.getPrompt());

        String generatedCoverUrl = "https://generated-cover.com/image/" + this.getBookId() + ".jpg";
        this.setCoverUrl(generatedCoverUrl);
    }
}