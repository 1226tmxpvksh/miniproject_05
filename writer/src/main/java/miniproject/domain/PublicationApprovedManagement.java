package miniproject.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class PublicationApprovedManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long bookId;           // ✅ 이미 쓰고 있던 필드
    private String title;          // ✅ PublicationApprovedManagementViewHandler에서 사용
    private String content;        // ✅ "
    private String coverUrl;       // ✅ "
    private Long writerId;         // ✅ "
    private String publishStatus;  // ✅ "

    // 추가 필드가 필요하면 여기에 계속 선언하면 됨
}
