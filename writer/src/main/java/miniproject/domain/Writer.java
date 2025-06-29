package miniproject.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import miniproject.WriterApplication;

@Entity
@Table(name = "Writer_table")
@Data
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long writerId;

    private String approvalStatus;
    private String publishStatus;
    private Date requestedAt;
    private Date approvedAt;
    private Date rejectedAt;

    public static WriterRepository repository() {
        return WriterApplication.applicationContext.getBean(WriterRepository.class);
    }

    // 작가 신청 처리
    public static void writerRequest(WriterRequest writerRequest) {
        Writer writer = new Writer();
        writer.setWriterId(writerRequest.getUserId());
        writer.setApprovalStatus("요청됨");
        writer.setRequestedAt(new Date());
        repository().save(writer);
    }

    // 작가 승인
    public void writerApprove(WriterApproveCommand cmd) {
        this.approvalStatus = "승인됨";
        this.approvedAt = new Date();
        WriterApproved event = new WriterApproved(this);
        event.publishAfterCommit();
    }

    // 작가 거절
    public void writerReject(WriterRejectCommand cmd) {
        this.approvalStatus = "거절됨";
        this.rejectedAt = new Date();
        WriterRejected event = new WriterRejected(this);
        event.publishAfterCommit();
    }

    // 출간 승인
    public void pubApprove(PubApproveCommand cmd) {
        this.publishStatus = "출간승인됨";
        PubApproved event = new PubApproved(this);
        event.publishAfterCommit();
    }

    // 출간 거절
    public void pubReject(PubRejectCommand cmd) {
        this.publishStatus = "출간거절됨";
        PubRejected event = new PubRejected(this);
        event.publishAfterCommit();
    }

    // 출간 요청 → writerId 기반으로 기존 작가 찾아서 상태 업데이트
    public static void publishRequest(PublishRequested event) {
        repository().findById(event.getWriterId()).ifPresent(writer -> {
            writer.setPublishStatus("출간요청됨");
            repository().save(writer);
        });
    }
}
