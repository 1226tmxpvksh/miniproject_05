package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class SubscribeCommand {

    private Long userId;
    private Boolean isSubscribed;         // true로 설정됨
    private Date subscriptionStart;       // 시작일
    private Date subscriptionEnd;         // 종료일
}
