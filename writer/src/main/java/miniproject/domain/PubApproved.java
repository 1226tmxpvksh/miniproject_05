package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PubApproved extends AbstractEvent {

    private Long writerId;
    private Long bookId;
    private String publishStatus;

    public PubApproved(Writer aggregate) {
        super(aggregate);
        this.writerId = aggregate.getWriterId();
        this.publishStatus = aggregate.getPublishStatus();
        // bookId는 외부에서 별도로 setBookId()로 주입해줘야 할 수 있음
    }

    public PubApproved() {
        super();
    }
}
//>>> DDD / Domain Event
