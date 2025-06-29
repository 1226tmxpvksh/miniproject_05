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
public class SubscriptionRequested extends AbstractEvent {

    private Long userId;
    private String email;
    private String nickname;
    private Boolean isSubscribed;
    private Date subscriptionStart;
    private Date subscriptionEnd;
    private Boolean writerRequested;

    public SubscriptionRequested(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.isSubscribed = aggregate.getIsSubscribed();
        this.subscriptionStart = aggregate.getSubscriptionStart();
        this.subscriptionEnd = aggregate.getSubscriptionEnd();
        this.writerRequested = false; // 혹은 aggregate.getWriterRequested() 값이 있다면 반영
    }
}
//>>> DDD / Domain Event
