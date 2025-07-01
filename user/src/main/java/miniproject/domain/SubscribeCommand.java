package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

//<<< DDD / Command
@Data
public class SubscribeCommand {

    private Long userId; // 구독을 요청한 유저 식별자
    private Date requestedAt = new Date();
}
