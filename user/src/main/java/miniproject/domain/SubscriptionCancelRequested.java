package miniproject.domain;

import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
public class SubscriptionCancelRequested extends AbstractEvent {

    private Long userId;
    private String email;
    private String nickname;
    private Boolean isSubscribed;
    private Date subscriptionEnd;

    public SubscriptionCancelRequested(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.isSubscribed = aggregate.getIsSubscribed();
        this.subscriptionEnd = aggregate.getSubscriptionEnd();
    }
}
//>>> DDD / Domain Event
