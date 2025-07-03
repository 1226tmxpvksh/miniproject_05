package miniproject.domain;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class SubscriptionCancelCommand {

    private Long userId;
}
