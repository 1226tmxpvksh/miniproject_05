package miniproject.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import miniproject.domain.BestSellerSelected;
import miniproject.domain.BestSellerList;
import miniproject.infra.BestSellerListRepository;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    BestSellerRepository bestSellerRepository;

    @Autowired
    BestSellerListRepository bestSellerListRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookAccessGranted'"
    )
    public void wheneverBookAccessGranted_ViewCount(
        @Payload BookAccessGranted bookAccessGranted
    ) {
        BookAccessGranted event = bookAccessGranted;
        System.out.println(
            "\n\n##### listener ViewCount : " + bookAccessGranted + "\n\n"
        );

        // Sample Logic //
        BestSeller.viewCount(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BestSellerSelected'"
    )
    public void wheneverBestSellerSelected_AddToList(
        @Payload BestSellerSelected event
    ) {
        if (event == null || event.getBookId() == null) return;
        System.out.println("\n\n##### listener AddToList : " + event + "\n\n");

        // 이미 등재된 도서가 아니면 추가
        if (!bestSellerListRepository.findByBookId(event.getBookId()).isPresent()) {
            BestSellerList bestSellerList = new BestSellerList();
            bestSellerList.setBookId(event.getBookId());
            bestSellerList.setTitle(event.getTitle());
            bestSellerList.setCoverUrl(event.getCoverUrl());
            bestSellerList.setViewCount(event.getViewCount());
            bestSellerList.setWriterId(event.getWriterId());
            bestSellerList.setSelectedStatus("베스트셀러");
            bestSellerList.setSelectedAt(new java.util.Date());
            bestSellerListRepository.save(bestSellerList);
        }
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointDeducted'"
    )
    public void wheneverPointDeducted_ViewCount(
        @Payload PointDeducted pointDeducted
    ) {
        PointDeducted event = pointDeducted;
        System.out.println(
            "\n\n##### listener ViewCount : " + pointDeducted + "\n\n"
        );

        // Sample Logic //
        BestSeller.viewCount(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
