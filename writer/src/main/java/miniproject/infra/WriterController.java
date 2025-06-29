package miniproject.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/writers")
@Transactional
public class WriterController {

    @Autowired
    WriterRepository writerRepository;

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
