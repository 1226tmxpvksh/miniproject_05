package miniproject.infra;

import java.util.Optional;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    //구독 상태(권한) 확인 (userId, bookId 입력)
    @PutMapping("/{userId}/check")
    public boolean checkSubscription(
            @PathVariable Long userId,
            @RequestBody CheckSubscriptionCommand cmd) throws Exception {

        Optional<Subscription> opt = subscriptionRepository.findByUserId(userId);
        if (!opt.isPresent()) throw new Exception("No Subscription Found");

        
        return opt.get().checkSubscription(cmd, null); 
    }

    //구독 등록 (구독 시작)
    @PostMapping("/{userId}/register")
    public Subscription registerSubscription(
            @PathVariable Long userId,
            @RequestBody SubscriptionRegisterCommand cmd) {

        Subscription subscription = subscriptionRepository.findByUserId(userId)
            .orElseGet(() -> {
                Subscription s = new Subscription();
                s.setUserId(userId);
                return s;
            });

        subscription.subscriptionRegister(cmd);
        return subscriptionRepository.save(subscription);
    }

    //구독 취소
    @PutMapping("/{userId}/cancel")
    public Subscription cancelSubscription(
            @PathVariable Long userId,
            @RequestBody SubscriptionCancelCommand cmd) throws Exception {

        Subscription subscription = subscriptionRepository.findByUserId(userId)
            .orElseThrow(() -> new Exception("No Subscription Found"));

        subscription.subscriptionCancel(cmd);
        return subscriptionRepository.save(subscription);
    }

    //구독 현황 조회 (READ)
    @GetMapping("/{userId}")
    public Subscription getSubscription(@PathVariable Long userId) throws Exception {
        return subscriptionRepository.findByUserId(userId)
            .orElseThrow(() -> new Exception("No Subscription Found"));
    }
}

