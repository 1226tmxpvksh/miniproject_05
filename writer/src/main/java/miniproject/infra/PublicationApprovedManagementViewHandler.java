package miniproject.infra;

import java.util.Optional;
import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PublicationApprovedManagementViewHandler {

    @Autowired
    private PublicationApprovedManagementRepository publicationApprovedManagementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPublishRequested_then_CREATE_1(@Payload PublishRequested publishRequested) {
        try {
            if (!publishRequested.validate()) return;

            PublicationApprovedManagement view = new PublicationApprovedManagement();
            view.setBookId(publishRequested.getBookId());
            view.setTitle(publishRequested.getTitle());
            view.setContent(publishRequested.getContent());
            view.setCoverUrl(publishRequested.getCoverUrl());
            view.setWriterId(publishRequested.getWriterId());
            view.setPublishStatus("PENDING"); // ✅ 문자열로 직접 입력

            publicationApprovedManagementRepository.save(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPubApproved_then_UPDATE_1(@Payload PubApproved pubApproved) {
        try {
            if (!pubApproved.validate()) return;

            Optional<PublicationApprovedManagement> optionalView =
                publicationApprovedManagementRepository.findByBookId(pubApproved.getBookId());

            if (optionalView.isPresent()) {
                PublicationApprovedManagement view = optionalView.get();
                view.setPublishStatus("APPROVED"); // ✅ 문자열로 직접 입력
                publicationApprovedManagementRepository.save(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPubRejected_then_UPDATE_2(@Payload PubRejected pubRejected) {
        try {
            if (!pubRejected.validate()) return;

            Optional<PublicationApprovedManagement> optionalView =
                publicationApprovedManagementRepository.findByBookId(pubRejected.getBookId());

            if (optionalView.isPresent()) {
                PublicationApprovedManagement view = optionalView.get();
                view.setPublishStatus("REJECTED"); // ✅ 문자열로 직접 입력
                publicationApprovedManagementRepository.save(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
