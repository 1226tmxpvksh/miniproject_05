package miniproject.infra;

import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@Slf4j
public class UserController {

    @Autowired
    UserRepository userRepository;

    private PasswordEncoder getPasswordEncoder() {
        return miniproject.UserApplication.applicationContext.getBean(PasswordEncoder.class);
    }

    @PostMapping(value = "/users/register", produces = "application/json;charset=UTF-8")
    public User register(@RequestBody RegisterCommand registerCommand) {
        log.info("▶ /users/register called: {}", registerCommand);

        userRepository.findByEmail(registerCommand.getEmail())
            .ifPresent(u -> { throw new EmailAlreadyExistsException(registerCommand.getEmail()); });

        userRepository.findByNickname(registerCommand.getNickname())
            .ifPresent(u -> { throw new NicknameAlreadyExistsException(registerCommand.getNickname()); });

        User user = new User();
        user.register(registerCommand);
        return userRepository.save(user);
    }

    // 로그인: 세션에 userId 저장
    @PostMapping(value = "/users/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> login(@RequestBody LoginCommand loginCommand, HttpSession session) {
        log.info("▶ /users/login called: {}", loginCommand.getEmail());

        User user = userRepository.findByEmail(loginCommand.getEmail())
            .orElseThrow(() -> new UserNotFoundException());

        PasswordEncoder encoder = getPasswordEncoder();

        if (user.checkPassword(loginCommand.getPassword(), encoder)) {
            // 로그인 성공: 세션에 userId 저장
            session.setAttribute("userId", user.getUserId());
            return ResponseEntity.ok("로그인 성공 (세션 생성)");
        } else {
            return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
        }
    }

    // 로그아웃: 세션 만료
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 완료");
    }

    // 인증이 필요:(세션 체크 후)구독
    @PutMapping(value = "/users/{id}/subscribe", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> subscribe(@PathVariable Long id, @RequestBody SubscribeCommand command, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(id)) {
            return ResponseEntity.status(401).body("로그인이 필요하거나 잘못된 접근입니다.");
        }
        User user = findUserOrThrow(id);
        user.subscribe(command);
        return ResponseEntity.ok(userRepository.save(user));
    }

    // 인증 필요: 작가신청
    @PutMapping(value = "/users/{id}/writerquest", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> writerQuest(@PathVariable Long id, @RequestBody WriterQuestCommand command, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(id)) {
            return ResponseEntity.status(401).body("로그인이 필요하거나 잘못된 접근입니다.");
        }
        User user = findUserOrThrow(id);
        user.writerQuest(command);
        return ResponseEntity.ok(userRepository.save(user));
    }

    // 인증 필요: 구독 취소
    @PutMapping(value = "/users/{id}/cancelsubscription", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long id, @RequestBody CancelSubscriptionCommand command, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(id)) {
            return ResponseEntity.status(401).body("로그인이 필요하거나 잘못된 접근입니다.");
        }
        User user = findUserOrThrow(id);
        user.cancelSubscription(command);
        return ResponseEntity.ok(userRepository.save(user));
    }

    // 인증 필요: 포인트 충전
    @PutMapping(value = "/users/{id}/chargepoint", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> chargePoint(@PathVariable Long id, @RequestBody ChargePointCommand command, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null || !sessionUserId.equals(id)) {
            return ResponseEntity.status(401).body("로그인이 필요하거나 잘못된 접근입니다.");
        }
        User user = findUserOrThrow(id);
        user.chargePoint(command);
        return ResponseEntity.ok(userRepository.save(user));
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }
}



