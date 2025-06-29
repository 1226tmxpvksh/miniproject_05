package miniproject.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import miniproject.PointApplication;

@Entity
@Table(name = "Point_table")
@Data
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private Integer amount;

    public static PointRepository repository() {
        return PointApplication.applicationContext.getBean(PointRepository.class);
    }

    // 포인트 차감 로직
    public void deductPoint(DeductPointCommand command) {
        if (this.amount != null && this.amount >= command.getAmount()) {
            this.amount -= command.getAmount();

            PointDeducted event = new PointDeducted(this);
            event.setAmount(command.getAmount());
            event.publishAfterCommit();
        } else {
            PointDeductFailed event = new PointDeductFailed(this);
            event.setReason("잔액 부족");
            event.publishAfterCommit();
        }
    }

    // 포인트 충전 로직
    public void chargePoint(ChargePointCommand command) {
        if (command.getAmount() > 0) {
            if (this.amount == null) this.amount = 0;
            this.amount += command.getAmount();

            PointCharged event = new PointCharged(this);
            event.setAmount(command.getAmount());
            event.publishAfterCommit();
        } else {
            PointChargeFailed event = new PointChargeFailed(this);
            event.setReason("충전 금액 오류");
            event.publishAfterCommit();
        }
    }

    // BookAccessDenied 이벤트 수신 후 포인트 확인
    public static void checkPoint(BookAccessDenied event) {
        repository().findById(event.getUserId()).ifPresent(point -> {
            PointInfoCheck check = new PointInfoCheck(point);
            check.publishAfterCommit();
        });
    }

    // 외부에서 충전 요청 이벤트 수신
    public static void chargePoint(PointChargeRequested event) {
        repository().findById(event.getUserId()).ifPresent(point -> {
            ChargePointCommand cmd = new ChargePointCommand();
            cmd.setAmount(event.getAmount());
            point.chargePoint(cmd);
            repository().save(point);
        });
    }
}
