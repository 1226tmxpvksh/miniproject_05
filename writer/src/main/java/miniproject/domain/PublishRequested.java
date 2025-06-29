package miniproject.domain;

import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class PublishRequested extends AbstractEvent {

    private Long bookId;
    private String title;
    private String content;
    private Long writerId;
    private String coverUrl;

    private String publishStatus = "요청됨";
    private Date requestedAt = new Date(); // 이벤트 발생 시 시간
}
