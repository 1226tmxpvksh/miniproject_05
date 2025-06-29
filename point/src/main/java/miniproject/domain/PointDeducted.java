package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class PointDeducted extends AbstractEvent {

    private Long userId;
    private Integer amount;

    public PointDeducted(Point aggregate) {
        super(aggregate);
    }

    public PointDeducted() {
        super();
    }
}
