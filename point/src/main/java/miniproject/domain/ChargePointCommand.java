package miniproject.domain;

import lombok.Data;

@Data
public class ChargePointCommand {

    private Long userId;
    private Integer amount;
}
