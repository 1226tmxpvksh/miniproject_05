package commondto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookViewedDto {
    private Long bookId;
    private Long userId;   // 열람 요청자
    private Long writerId; // 책의 저자
}

