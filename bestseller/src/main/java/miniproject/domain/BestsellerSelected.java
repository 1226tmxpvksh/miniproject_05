package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

import java.util.Date;

@Data
@ToString
public class BestsellerSelected extends AbstractEvent {

    private Long bestsellerId;
    private Long bookId;
    private Integer viewCount;
    private String selectedStatus;
    private Date selectedAt;

    // 필요 시 확장 가능
    private String title;
    private String coverUrl;
    private Long writerId;

    public BestsellerSelected(BestSeller aggregate) {
        super(aggregate);
        this.bestsellerId = aggregate.getBestsellerId();
        this.bookId = aggregate.getBookId();
        this.viewCount = aggregate.getViewCount();
        this.selectedStatus = aggregate.getSelectedStatus();
        this.selectedAt = aggregate.getSelectedAt();

        this.title = null;
        this.coverUrl = null;
        this.writerId = null;
    }

    public BestsellerSelected() {
        super();
    }
}
