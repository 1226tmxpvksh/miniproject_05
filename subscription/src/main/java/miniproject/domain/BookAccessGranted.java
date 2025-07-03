package miniproject.domain;
import miniproject.infra.AbstractEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookAccessGranted extends AbstractEvent {
    private Long userId;
    private Long bookId;

    public BookAccessGranted(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
}



