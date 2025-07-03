package commondto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionGrantedDto {
    
    private Long userId;
    private Long bookId;
}

