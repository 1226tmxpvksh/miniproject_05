package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionChecked extends AbstractEvent {

    private Long userId;
    private Long bookId;
    private boolean subscribed;

    public SubscriptionChecked(Object aggregate) {
        super(aggregate);
    }
}
