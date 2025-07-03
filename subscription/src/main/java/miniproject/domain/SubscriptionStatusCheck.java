package miniproject.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "SubscriptionStatusCheck_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionStatusCheck {

    @Id
    private Long userId; 

    private String subscriptionStatus;      // ACTIVE, INACTIVE
    private String subscriptionExpireDate; 
}

