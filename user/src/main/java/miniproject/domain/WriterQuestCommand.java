package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class WriterQuestCommand {
    private Long userId;
    private Date requestedAt = new Date(); // 기본값 현재시각

}

