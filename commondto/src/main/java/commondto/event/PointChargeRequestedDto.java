package commondto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointChargeRequestedDto {
    private Long userId;
    private Integer amount;
}

