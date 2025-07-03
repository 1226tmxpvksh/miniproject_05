package miniproject.domain;

import java.util.Date;
import lombok.*;
import miniproject.infra.AbstractEvent;

@Getter
@ToString
@NoArgsConstructor
public class SubscriptionRegistered extends AbstractEvent {

    private Long userId;
    private SubscriptionStatus subscriptionStatus; 
    private Date subscriptionExpiryDate;

    public SubscriptionRegistered(Subscription aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.subscriptionStatus = aggregate.getSubscriptionStatus();
        this.subscriptionExpiryDate = aggregate.getSubscriptionExpiryDate();
    }
}


