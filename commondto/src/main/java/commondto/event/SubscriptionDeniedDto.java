package commondto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionDeniedDto {
    private Long userId;
    private Long bookId;
}

