package miniproject.domain;
import miniproject.infra.AbstractEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookAccessDenied extends AbstractEvent {
    private Long userId;
    private Long bookId;

    public BookAccessDenied(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
}
