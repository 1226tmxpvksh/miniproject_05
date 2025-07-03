package commondto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BestsellerDto {
    private Long bestsellerId;
    private Long bookId;
    private Integer viewCount;
    private String selectedStatus;
    private Date selectedAt;
}

