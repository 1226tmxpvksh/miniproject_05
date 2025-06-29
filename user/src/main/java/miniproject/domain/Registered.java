package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
public class Registered extends AbstractEvent {

    private Long userId;
    private String email;
    private String nickname;
    private Integer amount;
    private Boolean isSubscribed;
    private Date subscriptionStart;
    private Date subscriptionEnd;

    public Registered(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.amount = aggregate.getAmount();
        this.isSubscribed = aggregate.getIsSubscribed();
        this.subscriptionStart = aggregate.getSubscriptionStart();
        this.subscriptionEnd = aggregate.getSubscriptionEnd();
    }
}
//>>> DDD / Domain Event
