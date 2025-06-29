package miniproject.domain;

import java.util.*;
import lombok.*;
import miniproject.domain.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class WriterRequest extends AbstractEvent {

    private Long userId;
    private String email;
    private String nickname;
    private String approvalStatus = "요청됨"; // 기본 상태
}
