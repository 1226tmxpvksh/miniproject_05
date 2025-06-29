package miniproject.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "PublicationApprovedManagement_table")
@Data
public class PublicationApprovedManagement {

    @Id
    private Long bookId;

    private String title;
    private String content;
    private String coverUrl;
    private Long writerId;
    private String publishStatus;

    // Optional: 추가 정보 (정렬/표시용)
    private Date publishedAt;       // 출간 시간
    private Integer viewCount;      // 조회수
    private String selectedStatus;  // 일반도서 / 베스트셀러
}
