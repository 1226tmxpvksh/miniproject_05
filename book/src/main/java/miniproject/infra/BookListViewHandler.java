package miniproject.infra;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookListViewHandler {

    @Autowired
    private BookListRepository bookListRepository;

    // 출간 완료 → 뷰 모델 생성
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPublishCompleted_then_CREATE(@Payload PublishCompleted event) {
        try {
            if (!event.validate()) return;

            BookList view = new BookList();
            view.setBookId(event.getBookId());
            view.setTitle(event.getTitle());
            view.setCoverUrl(event.getCoverUrl());
            view.setViewCount(0); // 초기 조회수
            view.setWriterId(event.getWriterId());
            view.setWriterNickname(event.getWriterNickname());

            bookListRepository.save(view);
        } catch (Exception e) {
            log.error("❌ Error in whenPublishCompleted_then_CREATE", e);
        }
    }

    // 도서 열람 → 조회수 증가
    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookViewed_then_UPDATE(@Payload BookViewed event) {
        try {
            if (!event.validate()) return;

            Optional<BookList> optional = bookListRepository.findByBookId(event.getBookId());
            if (optional.isPresent()) {
                BookList view = optional.get();
                view.setViewCount(view.getViewCount() + 1); // ✅ 조회수 증가
                bookListRepository.save(view);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenBookViewed_then_UPDATE", e);
        }
    }

    // 사용자 닉네임 변경 → writerNickname 업데이트
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_UPDATE(@Payload Registered event) {
        try {
            if (!event.validate()) return;

            List<BookList> views = bookListRepository.findByWriterId(event.getUserId());
            for (BookList view : views) {
                view.setWriterNickname(event.getNickname());
                bookListRepository.save(view);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenRegistered_then_UPDATE", e);
        }
    }
}
