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
    private String email;
    private String nickname;
    private Integer amount;

    public PointChargeRequested(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.amount = aggregate.getAmount(); // 혹은 command에서 받은 값
    }
}
//>>> DDD / Domain Event
