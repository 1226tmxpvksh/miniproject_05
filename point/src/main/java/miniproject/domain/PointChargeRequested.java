package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

@Data
@ToString
public class PointChargeRequested extends AbstractEvent {

    private Long userId;
    private Integer amount; // ✅ String → Integer 로 변경
}
