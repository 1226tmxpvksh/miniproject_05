package miniproject.domain;

import java.util.*;
import lombok.*;

import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
public class SubscriptionCancelRequested extends AbstractEvent {

    private Long userId;
    private Date requestedAt; // 구독취소 신청 요청 시각

    public SubscriptionCancelRequested(Long userId, Date requestedAt) {
        this.userId = userId;
        this.requestedAt = requestedAt; 
    }
}