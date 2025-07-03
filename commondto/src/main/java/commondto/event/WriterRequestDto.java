package commondto.event;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WriterRequestDto {
    private Long userId;
    private Date requestedAt;
}
