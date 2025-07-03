package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class PointDeductRequested extends AbstractEvent {

    private Long userId;
    private Integer amount;
    private Long bookId;

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public PointDeductRequested(Book aggregate) {
        super(aggregate);
    }
}
