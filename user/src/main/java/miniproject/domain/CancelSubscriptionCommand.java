package miniproject.domain;

import java.util.*;
import lombok.Data;

@Data
public class CancelSubscriptionCommand {

    private Long userId;
    private Boolean isSubscribed;         // false로 설정됨
    private Date subscriptionEnd;         // 해지일자
}
