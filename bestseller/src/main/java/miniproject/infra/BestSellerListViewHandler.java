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

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookViewed'"
    )
    public void whenBookViewed_then_CreateOrUpdate(@Payload BookViewed bookViewed) {
        try {
            if (!bookViewed.validate()) return;
            
            // bookId로 기존 BestSellerList 데이터가 있는지 조회합니다.
            // BestSellerListRepository에 findByBookId 메서드가 있어야 합니다.
            Optional<BestSellerList> bestSellerListOptional = bestSellerListRepository.findByBookId(bookViewed.getBookId());
            
            BestSellerList bestSellerList;
            if(bestSellerListOptional.isPresent()) {
                // 데이터가 있으면 기존 객체를 가져옵니다.
                bestSellerList = bestSellerListOptional.get();
            } else {
                // 데이터가 없으면 (최초 조회), 새로 생성하고 ID를 설정합니다.
                bestSellerList = new BestSellerList();
                bestSellerList.setBookId(bookViewed.getBookId());
            }

            // 공통 정보 업데이트
            bestSellerList.setTitle(bookViewed.getTitle());
            
            // 조회수 업데이트
            int currentViewCount = bestSellerList.getViewCount() != null ? bestSellerList.getViewCount() : 0;
            bestSellerList.setViewCount(currentViewCount + 1);

            // 이벤트에 있는 다른 정보들도 업데이트
            bestSellerList.setCoverUrl(bookViewed.getCoverUrl());
            bestSellerList.setWriterId(bookViewed.getWriterId());

            bestSellerListRepository.save(bestSellerList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

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
