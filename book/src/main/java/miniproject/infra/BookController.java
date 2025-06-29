package miniproject.infra;

import java.util.Optional;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    // 도서 작성
    @PostMapping("/write")
    public Book write(@RequestBody WriteCommand command) {
        Book book = new Book();
        book.write(command);
        return bookRepository.save(book);
    }

    // 도서 삭제
    @DeleteMapping("/{id}/delete")
    public Book delete(@PathVariable Long id, @RequestBody DeleteCommand command) throws Exception {
        Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("No Entity Found"));
        book.delete(command);
        bookRepository.delete(book);
        return book;
    }

    // 출간 요청
    @PostMapping("/publishrequest")
    public Book publishRequest(@RequestBody PublishRequestCommand command) {
        Book book = new Book();
        book.publishRequest(command);
        return bookRepository.save(book);
    }

    // 도서 열람
    @PutMapping("/{id}/viewbook")
    public Book viewBook(@PathVariable Long id, @RequestBody ViewBookCommand command) throws Exception {
        Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("No Entity Found"));
        book.viewBook(command);
        return bookRepository.save(book);
    }

    // 표지 선택
    @PutMapping("/{id}/selectbookcover")
    public Book selectBookCover(@PathVariable Long id, @RequestBody SelectBookCoverCommand command) throws Exception {
        Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("No Entity Found"));
        book.selectBookCover(command);
        return bookRepository.save(book);
    }

    // 표지 생성 요청
    @PostMapping("/requestcovergeneration")
    public Book requestCoverGeneration(@RequestBody RequestCoverGenerationCommand command) {
        Book book = new Book();
        book.requestCoverGeneration(command);
        return bookRepository.save(book);
    }

    // 도서 수정
    @PutMapping("/{id}/update")
    public Book update(@PathVariable Long id, @RequestBody UpdateCommand command) throws Exception {
        Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("No Entity Found"));
        book.update(command);
        return bookRepository.save(book);
    }
} 