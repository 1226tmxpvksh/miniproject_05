package miniproject.domain;

import java.util.*;
import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class BookAccessGranted extends AbstractEvent {

    private Long userId;
    private Long bookId;

    public BookAccessGranted(BestSeller aggregate) {
        super(aggregate);
        this.bookId = aggregate.getBookId(); // BestSeller에서 복사
    }
}
