package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class PointDeducted extends AbstractEvent {

    private Long userId;
    private Long bookId;
    private Integer amount;

    public PointDeducted(Object aggregate) {
        super(aggregate);
    }
}
