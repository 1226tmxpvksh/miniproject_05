package miniproject.infra;

import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class SubscriptionController {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @RequestMapping(
        value = "/subscriptions/{id}/checksubscription",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription checkSubscription(
        @PathVariable(value = "id") Long id,
        @RequestBody CheckSubscriptionCommand checkSubscriptionCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /subscription/checkSubscription called #####");

        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);

        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();

            // 유효 기간 비교
            Date now = new Date();
            if (
                "구독중".equals(subscription.getSubscriptionStatus()) &&
                subscription.getSubscriptionExpiryDate() != null &&
                subscription.getSubscriptionExpiryDate().after(now)
            ) {
                // 접근 허용
                BookAccessGranted granted = new BookAccessGranted(subscription);
                granted.publishAfterCommit();
            } else {
                // 접근 거부
                BookAccessDenied denied = new BookAccessDenied(subscription);
                denied.publishAfterCommit();
            }

            return subscription;
        } else {
            throw new Exception("No Entity Found");
        }
    }

    @RequestMapping(
        value = "/subscriptions/{id}/subscriptionregister",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription subscriptionRegister(
        @PathVariable(value = "id") Long id,
        @RequestBody SubscriptionRegisterCommand cmd,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /subscription/subscriptionRegister called #####");

        Subscription subscription = subscriptionRepository
            .findById(id)
            .orElseGet(() -> {
                Subscription s = new Subscription();
                s.setUserId(id);
                return s;
            });

        subscription.setSubscriptionStatus("구독중");

        // 30일 뒤로 만료일 설정
        Date expiry = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
        subscription.setSubscriptionExpiryDate(expiry);

        subscriptionRepository.save(subscription);

        SubscriptionRegistered event = new SubscriptionRegistered(subscription);
        event.publishAfterCommit();

        return subscription;
    }

    @RequestMapping(
        value = "/subscriptions/{id}/subscriptioncancel",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Subscription subscriptionCancel(
        @PathVariable(value = "id") Long id,
        @RequestBody SubscriptionCancelCommand cmd,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /subscription/subscriptionCancel called #####");

        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        optionalSubscription.orElseThrow(() -> new Exception("No Entity Found"));

        Subscription subscription = optionalSubscription.get();
        subscription.setSubscriptionStatus("해지됨");
        subscription.setSubscriptionExpiryDate(null);

        subscriptionRepository.save(subscription);

        SubscriptionCanceled event = new SubscriptionCanceled(subscription);
        event.publishAfterCommit();

        return subscription;
    }
}
