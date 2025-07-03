package commondto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {
    private Long userId;
    private Integer amount; //보유 중인 전체 포인트 잔액
}

