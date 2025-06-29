package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class PointChargeFailed extends AbstractEvent {

    private Long userId;
    private Integer amount;
    private String reason;  // ✅ 추가

    public PointChargeFailed(Point aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.amount = aggregate.getAmount();
    }
}
