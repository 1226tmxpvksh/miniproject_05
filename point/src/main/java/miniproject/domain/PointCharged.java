package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class PointCharged extends AbstractEvent {

    private Long userId;
    private Integer amount;

    public PointCharged(Point aggregate) {
        super(aggregate);
    }

    public PointCharged() {
        super();
    }
}
