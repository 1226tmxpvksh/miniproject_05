package miniproject.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(
        value = "/users/register",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public User register(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterCommand registerCommand
    ) throws Exception {
        System.out.println("##### /user/register  called #####");
        User user = new User();
        user.register(registerCommand);
        userRepository.save(user);
        return user;
    }

    @GetMapping("/users/{id}/raw")
    public User getUserRaw(@PathVariable("id") Long id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.orElseThrow(() -> new Exception("No Entity Found"));
        return optionalUser.get();
    }

    // --- 이 메서드를 수정했습니다 ---
    @RequestMapping(
        value = "/users/{id}/subscribe",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public User subscribe(
        @PathVariable(value = "id") Long id,
        // @RequestBody를 삭제했습니다.
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /user/subscribe  called #####");
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.orElseThrow(() -> new Exception("No Entity Found"));
        User user = optionalUser.get();

        // Command 객체를 여기서 직접 생성합니다.
        SubscribeCommand subscribeCommand = new SubscribeCommand();
        // subscribeCommand.setUserId(id); // 필요 시 값 설정
        
        user.subscribe(subscribeCommand);

        userRepository.save(user);
        return user;
    }
    // --- 여기까지 수정 ---

    @RequestMapping(
        value = "/users/{id}/writerquest",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public User writerQuest(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /user/writerQuest  called #####");
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.orElseThrow(() -> new Exception("No Entity Found"));
        User user = optionalUser.get();
        
        WriterQuestCommand writerQuestCommand = new WriterQuestCommand();
        user.writerQuest(writerQuestCommand);

        userRepository.save(user);
        return user;
    }

    // --- cancelSubscription도 수정하는 것을 권장합니다 ---
    @RequestMapping(
        value = "/users/{id}/cancelsubscription",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public User cancelSubscription(
        @PathVariable(value = "id") Long id,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /user/cancelSubscription  called #####");
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.orElseThrow(() -> new Exception("No Entity Found"));
        User user = optionalUser.get();
        
        CancelSubscriptionCommand cancelSubscriptionCommand = new CancelSubscriptionCommand();
        user.cancelSubscription(cancelSubscriptionCommand);

        userRepository.save(user);
        return user;
    }
    // --- 여기까지 ---

    @RequestMapping(
        value = "/users/{id}/chargepoint",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public User chargePoint(
        @PathVariable(value = "id") Long id,
        @RequestBody ChargePointCommand chargePointCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /user/chargePoint  called #####");
        System.out.println("chargePointCommand: " + chargePointCommand);
        System.out.println("chargePointCommand.amount: " + chargePointCommand.getAmount());
        System.out.println("chargePointCommand.userId: " + chargePointCommand.getUserId());
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.orElseThrow(() -> new Exception("No Entity Found"));
        User user = optionalUser.get();
        user.chargePoint(chargePointCommand);

        userRepository.save(user);
        return user;
    }
}
