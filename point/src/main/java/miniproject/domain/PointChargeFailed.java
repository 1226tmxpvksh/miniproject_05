package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class PointChargeFailed extends AbstractEvent {

    private Long userId;
    private Integer amount;

    public PointChargeFailed(Point aggregate) {
        super(aggregate);
    }

    public PointChargeFailed() {
        super();
    }
}
