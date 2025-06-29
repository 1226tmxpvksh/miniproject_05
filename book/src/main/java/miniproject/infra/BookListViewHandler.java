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

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPublishCompleted_then_CREATE(@Payload PublishCompleted event) {
        try {
            if (!event.validate()) return;

            BookList view = new BookList();
            view.setBookId(event.getBookId());
            view.setTitle(event.getTitle());
            view.setCoverUrl(event.getCoverUrl());
            view.setViewCount(0);
            view.setWriterId(event.getWriterId());
            view.setWriterNickname(event.getWriterNickname());

            bookListRepository.save(view);
        } catch (Exception e) {
            log.error("❌ Error in whenPublishCompleted_then_CREATE", e);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookViewed_then_UPDATE(@Payload BookViewed event) {
        try {
            if (!event.validate()) return;
            Optional<BookList> optional = bookListRepository.findByBookId(event.getBookId());
            if (optional.isPresent()) {
                BookList view = optional.get();
                view.setViewCount(view.getViewCount() + 1);
                bookListRepository.save(view);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenBookViewed_then_UPDATE", e);
        }
    }

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