@Data
public class IncreaseBookViewCommand {
    private Long bookId;

    public IncreaseBookViewCommand(Long bookId) {
        this.bookId = bookId;
    }
}
