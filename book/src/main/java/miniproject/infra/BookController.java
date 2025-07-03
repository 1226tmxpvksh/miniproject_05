package miniproject.infra;

import java.util.Optional; // Optional import 추가
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books") // 기본 경로를 /books로 설정
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/{id}")
    public Optional<Book> findById(@PathVariable Long id) {
        return bookRepository.findById(id);
    }

    // 도서 작성
    @PostMapping // "/write" 대신 상위 경로를 그대로 사용
    public Book write(@RequestBody WriteCommand command) {
        Book book = new Book();
        book.setTitle(command.getTitle());
        book.setContent(command.getContent());
        book.setWriterId(command.getWriterId());
        // writerNickname은 user 정보가 동기화된 후 View 모델에서 채워지는 것이 일반적입니다.
        // book.setWriterNickname(command.getWriterNickname()); 

        // book.write(command);
        return bookRepository.save(book);
    }

    // 도서 삭제
    @DeleteMapping("/{id}") // "/delete" 경로 단순화
    public void delete(@PathVariable Long id) throws Exception {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        
        // Command 객체를 직접 생성하거나, Aggregate에서 처리하도록 위임
        DeleteCommand command = new DeleteCommand(); 
        book.delete(command);
        
        bookRepository.delete(book);
    }

    // 출간 요청
    @PostMapping("/{id}/publishrequest")
    public Book publishRequest(@PathVariable Long id) throws Exception {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        book.publishRequest(new PublishRequestCommand()); // 빈 Command 객체 전달
        return bookRepository.save(book);
    }

    // 도서 열람 → 조회수 증가
    @PutMapping("/{id}/viewbook")
    public Book viewBook(@PathVariable Long id) throws Exception {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        book.viewBook(new ViewBookCommand()); // 빈 Command 객체 전달
        return bookRepository.save(book);
    }

    // 표지 선택
    @PutMapping("/{id}/selectbookcover")
    public Book selectBookCover(@PathVariable Long id, @RequestBody SelectBookCoverCommand command) throws Exception {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        book.selectBookCover(command);
        return bookRepository.save(book);
    }

    // --- 이 메서드를 수정했습니다 ---
    // 표지 생성 요청
    @PostMapping("/{id}/requestcovergeneration")
    public Book requestCoverGeneration(@PathVariable Long id) throws Exception {
        System.out.println("##### /book/requestCoverGeneration called for bookId: " + id);
        
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        
        // Command 객체를 여기서 직접 생성
        RequestCoverGenerationCommand command = new RequestCoverGenerationCommand();
        // command.setBookId(id); // 필요 시 Command에 값 설정
        
        book.requestCoverGeneration(command);
        
        // save는 requestCoverGeneration 내부 로직 후 상태 변경이 있다면 필요합니다.
        // 보통 이벤트 발행만 한다면 save는 필요 없을 수 있습니다.
        return bookRepository.save(book);
    }
    // --- 여기까지 수정 ---

    // 도서 수정
    @PutMapping("/{id}") // "/update" 경로 단순화
    public Book update(@PathVariable Long id, @RequestBody UpdateCommand command) throws Exception {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        book.update(command);
        return bookRepository.save(book);
    }
}