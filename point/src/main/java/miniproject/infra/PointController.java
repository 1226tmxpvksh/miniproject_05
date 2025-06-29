package miniproject.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/points")
@Transactional
public class PointController {

    @Autowired
    PointRepository pointRepository;

    @PutMapping("/{id}/deductpoint")
    public Point deductPoint(
        @PathVariable("id") Long id,
        @RequestBody DeductPointCommand command,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /points/{id}/deductpoint called #####");

        Point point = pointRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));

        point.deductPoint(command);
        pointRepository.save(point);
        return point;
    }

    @PutMapping("/{id}/chargepoint")
    public Point chargePoint(
        @PathVariable("id") Long id,
        @RequestBody ChargePointCommand command,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /points/{id}/chargepoint called #####");

        Point point = pointRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));

        point.chargePoint(command);
        pointRepository.save(point);
        return point;
    }
}
