package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PubRejected extends AbstractEvent {

    private Long writerId;
    private Long bookId;
    private String publishStatus;

    public PubRejected(Writer aggregate) {
        super(aggregate);
        this.writerId = aggregate.getWriterId();
        this.publishStatus = aggregate.getPublishStatus();
        // bookId는 Writer aggregate에 없다면, 별도로 setBookId()를 통해 주입 필요
    }

    public PubRejected() {
        super();
    }
}
//>>> DDD / Domain Event
