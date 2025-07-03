package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class SubscriptionCheckRequested extends AbstractEvent {

    private Long userId;
    private Long bookId;

    public SubscriptionCheckRequested(Book aggregate) {
        super(aggregate);
    }
}
