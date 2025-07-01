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
public class WriterRequest extends AbstractEvent {

    private Long userId;
    private Date requestedAt; // WriterQuestCommand의 신청 시각

    // User 엔티티 WriterQuestCommand를 모두 받아서 생성
    public WriterRequest(User user, WriterQuestCommand command) {
        super(user);
        this.userId = user.getUserId();
        this.requestedAt = command.getRequestedAt();
    }
}
//>>> DDD / Domain Event
