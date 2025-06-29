package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionCanceled extends AbstractEvent {

    private Long userId;
    private String subscriptionStatus;

    public SubscriptionCanceled(Subscription aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.subscriptionStatus = aggregate.getSubscriptionStatus();
    }
}
