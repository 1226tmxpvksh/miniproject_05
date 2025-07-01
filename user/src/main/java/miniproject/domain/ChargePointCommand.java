package miniproject.domain;

import java.util.*;
import lombok.Data;
//<<< DDD / Command
@Data
public class ChargePointCommand {

    private Long userId;   // 충전 요청하는 유저 식별자
    private Integer amount; // 충전 요청 금액(양수)
}
