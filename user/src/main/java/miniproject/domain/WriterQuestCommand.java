package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class WriterQuestCommand {

    private Long userId;
    private Boolean writerRequested; // 작가 신청 여부 (보통 true)
    private Date writerRequestedAt;  // 신청 시각 (선택 사항)
}
