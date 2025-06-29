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
    private String email;
    private String nickname;
    private Boolean writerRequested;
    private Date writerRequestedAt;

    public WriterRequest(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.writerRequested = aggregate.getWriterRequested();
        this.writerRequestedAt = aggregate.getWriterRequestedAt();
    }
}
//>>> DDD / Domain Event
