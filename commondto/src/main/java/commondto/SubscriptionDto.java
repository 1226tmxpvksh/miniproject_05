package commondto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {

    private Long userId;
    private String subscriptionStatus;
    private Date subscriptionExpiryDate;
}
