package miniproject.domain;

import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
public class PointChargeRequested extends AbstractEvent {

    private Long userId;
    private Integer amount; // 충전 요청 금액

    // 이벤트 생성시 userId와 amount만 전달
    public PointChargeRequested(Long userId, Integer amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
//>>> DDD / Domain Event
