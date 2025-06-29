package miniproject.domain;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "BookDetailInfo_table")
@Data
public class BookDetailInfo {

    @Id
    private Long bookId; // 도서 ID (고유 키)

    private String title; // 도서 제목
    private String content; // 도서 본문
    private Integer viewCount = 0; // ✅ 조회수 (기본값 0)

    private Long writerId; // 작가 ID
    private String writerNickname; // 작가 닉네임
    private String coverUrl; // 표지 URL
}
