package miniproject.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import miniproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Transactional
public class BestSellerController {

    @Autowired
    BestSellerRepository bestSellerRepository;

    @RequestMapping(
        value = "/bestSellers/{id}/increasebookview",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public BestSeller increaseBookView(
        @PathVariable(value = "id") Long id,
        @RequestBody IncreaseBookViewCommand increaseBookViewCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        log.info("📈 [PUT] /bestSellers/{}/increasebookview called", id);

        BestSeller bestSeller = bestSellerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("BestSeller not found: id = " + id));

        bestSeller.increaseBookView(increaseBookViewCommand);
        bestSellerRepository.save(bestSeller);

        return bestSeller;
    }

    @RequestMapping(
        value = "/bestSellers/{id}/selectbestseller",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public BestSeller selectBestSeller(
        @PathVariable(value = "id") Long id,
        @RequestBody SelectBestSellerCommand selectBestSellerCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        log.info("🏅 [PUT] /bestSellers/{}/selectbestseller called", id);

        BestSeller bestSeller = bestSellerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("BestSeller not found: id = " + id));

        bestSeller.selectBestSeller(selectBestSellerCommand);
        bestSellerRepository.save(bestSeller);

        return bestSeller;
    }
}
