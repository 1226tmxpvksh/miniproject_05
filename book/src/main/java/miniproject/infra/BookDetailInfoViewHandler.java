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
public class BookDetailInfoViewHandler {

    @Autowired
    private BookDetailInfoRepository bookDetailInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenWritten_then_CREATE(@Payload Written written) {
        try {
            if (!written.validate()) return;

            BookDetailInfo bookDetailInfo = new BookDetailInfo();
            bookDetailInfo.setBookId(written.getBookId());
            bookDetailInfo.setTitle(written.getTitle());
            bookDetailInfo.setContent(written.getContent());
            bookDetailInfo.setWriterId(written.getWriterId());

            bookDetailInfoRepository.save(bookDetailInfo);
        } catch (Exception e) {
            log.error("❌ Error in whenWritten_then_CREATE", e);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookCoverSelected_then_UPDATE(@Payload BookCoverSelected event) {
        try {
            if (!event.validate()) return;
            Optional<BookDetailInfo> optional = bookDetailInfoRepository.findById(event.getBookId());
            if (optional.isPresent()) {
                BookDetailInfo view = optional.get();
                view.setCoverUrl(event.getCoverUrl());
                bookDetailInfoRepository.save(view);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenBookCoverSelected_then_UPDATE", e);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookViewIncreased_then_UPDATE(@Payload BookViewIncreased event) {
        try {
            if (!event.validate()) return;
            Optional<BookDetailInfo> optional = bookDetailInfoRepository.findById(event.getBookId());
            if (optional.isPresent()) {
                BookDetailInfo view = optional.get();
                view.setViewCount(event.getViewCount());
                bookDetailInfoRepository.save(view);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenBookViewIncreased_then_UPDATE", e);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_UPDATE(@Payload Registered event) {
        try {
            if (!event.validate()) return;
            List<BookDetailInfo> books = bookDetailInfoRepository.findAllByWriterId(event.getUserId());
            for (BookDetailInfo book : books) {
                book.setWriterNickname(event.getNickname());
                bookDetailInfoRepository.save(book);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenRegistered_then_UPDATE", e);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeleted_then_DELETE(@Payload Deleted event) {
        try {
            if (!event.validate()) return;
            bookDetailInfoRepository.deleteById(event.getBookId());
        } catch (Exception e) {
            log.error("❌ Error in whenDeleted_then_DELETE", e);
        }
    }
}