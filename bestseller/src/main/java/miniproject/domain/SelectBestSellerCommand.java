package miniproject.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectBestSellerCommand {

    private Long bestsellerId;
    private Long bookId;

    public SelectBestSellerCommand(Long bookId) {
        this.bookId = bookId;
    }
}
