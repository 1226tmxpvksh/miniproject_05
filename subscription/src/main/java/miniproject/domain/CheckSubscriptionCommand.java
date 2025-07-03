package miniproject.domain;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CheckSubscriptionCommand {

    private Long userId;
    private Long bookId;
}


