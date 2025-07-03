package commondto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiDto {
    private Long requestId;
    private Long bookId;
    private String prompt;
    private String coverUrl;
}

