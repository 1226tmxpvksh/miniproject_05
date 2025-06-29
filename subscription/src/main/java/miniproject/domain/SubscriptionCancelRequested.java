package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionCancelRequested extends AbstractEvent {

    private Long userId;

    public SubscriptionCancelRequested(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
    }
}
