package miniproject.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/writers")
@Transactional
public class WriterController {

    @Autowired
    WriterRepository writerRepository;

    // Writer 단일 조회 API
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public Writer getWriter(@PathVariable("id") Long id) {
        return writerRepository.findById(id).orElse(null);
    }

    // Writer 생성 API (userId, nickname 등)
    @PostMapping(value = "", produces = "application/json;charset=UTF-8")
    public Writer createWriter(@RequestBody Map<String, Object> body) {
        Long userId = body.get("userId") != null ? Long.valueOf(body.get("userId").toString()) : null;
        String nickname = body.get("nickname") != null ? body.get("nickname").toString() : null;
        Writer writer = new Writer();
        writer.setWriterId(userId);
        // nickname 등 추가 필드가 있다면 writer.setNickname(nickname);
        writerRepository.save(writer);
        return writer;
    }

    // 작가 신청(WriterRequest) API 추가
    @PostMapping(value = "/writerrequest", produces = "application/json;charset=UTF-8")
    public Writer writerRequest(@RequestBody WriterRequest writerRequest) {
        Writer.writerRequest(writerRequest);
        return writerRepository.findById(writerRequest.getUserId()).orElse(null);
    }

    @PutMapping(value = "/{id}/writerapprove", produces = "application/json;charset=UTF-8")
    public Writer writerApprove(
        @PathVariable("id") Long id,
        @RequestBody WriterApproveCommand writerApproveCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /writer/writerApprove called #####");

        Writer writer = writerRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        writer.writerApprove(writerApproveCommand);
        writerRepository.save(writer);
        return writer;
    }

    @PutMapping(value = "/{id}/writerreject", produces = "application/json;charset=UTF-8")
    public Writer writerReject(
        @PathVariable("id") Long id,
        @RequestBody WriterRejectCommand writerRejectCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /writer/writerReject called #####");

        Writer writer = writerRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        writer.writerReject(writerRejectCommand);
        writerRepository.save(writer);
        return writer;
    }

    @PutMapping(value = "/{id}/pubapprove", produces = "application/json;charset=UTF-8")
    public Writer pubApprove(
        @PathVariable("id") Long id,
        @RequestBody PubApproveCommand pubApproveCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /writer/pubApprove called #####");

        Writer writer = writerRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        writer.pubApprove(pubApproveCommand);
        writerRepository.save(writer);
        return writer;
    }

    @PutMapping(value = "/{id}/pubreject", produces = "application/json;charset=UTF-8")
    public Writer pubReject(
        @PathVariable("id") Long id,
        @RequestBody PubRejectCommand pubRejectCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /writer/pubReject called #####");

        Writer writer = writerRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));
        writer.pubReject(pubRejectCommand);
        writerRepository.save(writer);
        return writer;
    }
}
