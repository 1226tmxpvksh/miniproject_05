package miniproject.domain;

import lombok.*;
import miniproject.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@NoArgsConstructor
public class Registered extends AbstractEvent {

    private Long userId;
    private String email;
    private String nickname;
    private String passwordHash; // 패스워드 해시(실제 비밀번호는 절대 저장X)

    // User 엔티티에서 이벤트 생성
    public Registered(User aggregate) {
        super(aggregate);
        this.userId = aggregate.getUserId();
        this.email = aggregate.getEmail();
        this.nickname = aggregate.getNickname();
        this.passwordHash = aggregate.getPasswordHash(); // User에 passwordHash 필드 필요
    }
}
