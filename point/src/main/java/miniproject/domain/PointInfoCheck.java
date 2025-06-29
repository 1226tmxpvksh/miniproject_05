package miniproject.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PointInfoCheck_table")
@Data
public class PointInfoCheck {

    @Id
    private Long userId;
    private Integer amount;

    // ✅ Point 기반 생성자 추가
    public PointInfoCheck(Point point) {
        this.userId = point.getUserId();
        this.amount = point.getAmount();
    }

    // ✅ 이벤트처럼 사용하기 위한 임시 publish 메서드
    public void publishAfterCommit() {
        // 실제 이벤트 전송은 안하지만 메서드 존재시킴
    }

    public PointInfoCheck() {
        // 기본 생성자도 필요함
    }
}
