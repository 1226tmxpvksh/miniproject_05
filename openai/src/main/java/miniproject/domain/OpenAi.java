package miniproject.domain;

import lombok.Data;
import miniproject.OpenaiApplication;
import javax.persistence.*; // 기존 javax 유지

@Entity
@Table(name = "OpenAi_table")
@Data
public class OpenAi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;

    private Long bookId;
    private String prompt;
    
    @Column(length = 1024)
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
    
    // Controller에서 호출할 Command 처리 메서드 (이것은 남겨둡니다)
    public void bookCoverCreate(BookCoverCreateCommand bookCoverCreateCommand) {
        this.setBookId(bookCoverCreateCommand.getBookId());
        this.setPrompt(bookCoverCreateCommand.getPrompt());

        // 이 부분의 로직은 PolicyHandler와 DalleApiService가 담당하므로,
        // 여기서는 Command의 값을 Entity에 설정하는 역할만 합니다.
        // 실제 URL 생성은 다른 곳에서 이루어집니다.
        String generatedCoverUrl = "https://placeholder-image.com/image/" + this.getBookId() + ".jpg";
        this.setCoverUrl(generatedCoverUrl);
    }
}