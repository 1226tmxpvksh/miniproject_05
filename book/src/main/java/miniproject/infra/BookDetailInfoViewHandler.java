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

    // 도서 작성 시 상세 정보 생성
    @StreamListener(KafkaProcessor.INPUT)
    public void whenWritten_then_CREATE(@Payload Written event) {
        try {
            if (!event.validate()) return;

            BookDetailInfo view = new BookDetailInfo();
            view.setBookId(event.getBookId());
            view.setTitle(event.getTitle());
            view.setContent(event.getContent());
            view.setWriterId(event.getWriterId());
            view.setWriterNickname(event.getWriterNickname());
            view.setViewCount(0); // 기본값 설정

            bookDetailInfoRepository.save(view);
        } catch (Exception e) {
            log.error("❌ Error in whenWritten_then_CREATE", e);
        }
    }

    // 표지 선택 시 표지 URL 업데이트
    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookCoverSelected_then_UPDATE(@Payload BookCoverSelected event) {
        try {
            if (!event.validate()) return;
            bookDetailInfoRepository.findById(event.getBookId()).ifPresent(view -> {
                view.setCoverUrl(event.getCoverUrl());
                bookDetailInfoRepository.save(view);
            });
        } catch (Exception e) {
            log.error("❌ Error in whenBookCoverSelected_then_UPDATE", e);
        }
    }

    // 조회수 증가 이벤트 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookViewed_then_UPDATE(@Payload BookViewed event) {
        try {
            if (!event.validate()) return;
            bookDetailInfoRepository.findById(event.getBookId()).ifPresent(view -> {
                int current = view.getViewCount() != null ? view.getViewCount() : 0;
                view.setViewCount(current + 1);
                bookDetailInfoRepository.save(view);
            });
        } catch (Exception e) {
            log.error("❌ Error in whenBookViewed_then_UPDATE", e);
        }
    }

    // 회원 닉네임 변경 시 반영
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_UPDATE(@Payload Registered event) {
        try {
            if (!event.validate()) return;
            List<BookDetailInfo> books = bookDetailInfoRepository.findByWriterId(event.getUserId());
            for (BookDetailInfo view : books) {
                view.setWriterNickname(event.getNickname());
                bookDetailInfoRepository.save(view);
            }
        } catch (Exception e) {
            log.error("❌ Error in whenRegistered_then_UPDATE", e);
        }
    }

    // 도서 삭제 처리
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
