package miniproject.domain;

import java.util.*;
import lombok.Data;

@Data
public class ChargePointCommand {

    private Long userId;
    private Integer amount;
}
