package miniproject.infra;

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
public class BestSellerListViewHandler {

    @Autowired
    private BestSellerListRepository bestSellerListRepository;

    // 베스트셀러 선정 시 View 생성
    @StreamListener(KafkaProcessor.INPUT)
    public void whenBestsellerSelected_then_CREATE_1(
        @Payload BestsellerSelected bestsellerSelected
    ) {
        try {
            if (!bestsellerSelected.validate()) return;

            BestSellerList bestSellerList = new BestSellerList();
            bestSellerList.setBookId(bestsellerSelected.getBookId());
            bestSellerList.setTitle(bestsellerSelected.getTitle());
            bestSellerList.setCoverUrl(bestsellerSelected.getCoverUrl());
            bestSellerList.setViewCount(bestsellerSelected.getViewCount());
            bestSellerList.setWriterId(bestsellerSelected.getWriterId());
            bestSellerList.setSelectedStatus(bestsellerSelected.getSelectedStatus());
            bestSellerList.setSelectedAt(bestsellerSelected.getSelectedAt());

            bestSellerListRepository.save(bestSellerList);
        } catch (Exception e) {
            log.error("Error creating BestSellerList view on BestsellerSelected event", e);
        }
    }

    // 조회수 증가 시 View 갱신
    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookViewIncreased_then_UPDATE_1(
        @Payload BookViewIncreased bookViewIncreased
    ) {
        try {
            if (!bookViewIncreased.validate()) return;

            Optional<BestSellerList> optional = bestSellerListRepository.findByBookId(
                bookViewIncreased.getBookId()
            );

            if (optional.isPresent()) {
                BestSellerList bestSellerList = optional.get();
                bestSellerList.setViewCount(bookViewIncreased.getViewCount());
                bestSellerListRepository.save(bestSellerList);
            }
        } catch (Exception e) {
            log.error("Error updating BestSellerList view on BookViewIncreased event", e);
        }
    }
}
