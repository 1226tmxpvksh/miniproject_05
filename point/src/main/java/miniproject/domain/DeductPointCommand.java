package miniproject.domain;

import lombok.Data;

@Data
public class DeductPointCommand {

    private Long userId;
    private Integer amount;
}
