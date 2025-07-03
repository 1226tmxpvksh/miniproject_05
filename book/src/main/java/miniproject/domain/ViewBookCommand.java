package miniproject.domain;

import lombok.Data;

@Data
public class ViewBookCommand {
    private Long userId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    private Long bookId;
}
