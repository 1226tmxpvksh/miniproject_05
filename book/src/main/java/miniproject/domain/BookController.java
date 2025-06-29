package miniproject.infra;

import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    // ✅ 도서 작성
    @PostMapping
    public Book write(@RequestBody WriteCommand command) {
        Book book = new Book();
        book.setTitle(command.getTitle());
        book.setContent(command.getContent());
        book.setWriterId(command.getWriterId());
        book.setWriterNickname(command.getWriterNickname());
        book.setStatus("작성됨");

        book.write(command);
        return bookRepository.save(book);
    }

    // ✅ 출간 요청
    @PutMapping("/{id}/publish")
    public Book publish(@PathVariable Long id, @RequestBody PublishRequestCommand command) throws Exception {
        Optional<Book> optional = bookRepository.findById(id);
        if (!optional.isPresent()) throw new Exception("Book not found");

        Book book = optional.get();
        book.publishRequest(command);
        return bookRepository.save(book);
    }

    // ✅ 도서 열람
    @PutMapping("/{id}/viewbook")
    public Book view(@PathVariable Long id, @RequestBody ViewBookCommand command) throws Exception {
        Optional<Book> optional = bookRepository.findById(id);
        if (!optional.isPresent()) throw new Exception("Book not found");

        Book book = optional.get();
        book.viewBook(command);
        return bookRepository.save(book);
    }

    // ✅ 도서 삭제
    @PutMapping("/{id}/delete")
    public Book delete(@PathVariable Long id, @RequestBody DeleteCommand command) throws Exception {
        Optional<Book> optional = bookRepository.findById(id);
        if (!optional.isPresent()) throw new Exception("Book not found");

        Book book = optional.get();
        book.delete(command);
        return bookRepository.save(book);
    }
}
