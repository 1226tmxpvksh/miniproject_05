package miniproject.domain;

import java.util.*;
import lombok.Data;

//<<< DDD / Command
@Data
public class CancelSubscriptionCommand {

    private Long userId; // 구독 해지를 요청한 유저 식별자
    private Date requestedAt = new Date();
}
