package miniproject.infra;

import miniproject.config.kafka.KafkaProcessor;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class UserInfoListViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private UserInfoListRepository userInfoListRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_CREATE_1(@Payload Registered registered) {
        try {
            if (!registered.validate()) return;

            // view 객체 생성
            UserInfoList userInfoList = new UserInfoList();
            // 이벤트의 Value를 모두 set
            userInfoList.setUserId(registered.getUserId());
            userInfoList.setEmail(registered.getEmail());
            userInfoList.setNickname(registered.getNickname());
            userInfoList.setPasswordHash(registered.getPasswordHash()); //패스워드 해시 추가

            userInfoListRepository.save(userInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}

