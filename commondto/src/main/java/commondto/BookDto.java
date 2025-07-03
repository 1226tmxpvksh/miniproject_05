package commondto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String writerNickname;
    private String coverUrl;
    private String status;    // 책 출간여부
}

