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
    private Date requestedAt; // 구독 신청 요청 시각

    public SubscriptionRequested(Long userId, Date requestedAt) {
        this.userId = userId;
        this.requestedAt = requestedAt;
    }
}
//>>> DDD / Domain Event