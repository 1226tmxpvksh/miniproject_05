package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class PointDeductFailed extends AbstractEvent {

    private Long userId;
    private Integer amount;

    public PointDeductFailed(Point aggregate) {
        super(aggregate);
    }

    public PointDeductFailed() {
        super();
    }
}
