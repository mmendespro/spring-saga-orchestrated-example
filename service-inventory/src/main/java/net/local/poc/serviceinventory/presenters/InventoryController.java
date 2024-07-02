package net.local.poc.serviceinventory.presenters;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.serviceinventory.application.ports.usecases.DeductStockUC;
import net.local.poc.serviceinventory.application.ports.usecases.DeductStockUC.DeductStockInput;
import net.local.poc.serviceinventory.application.ports.usecases.DeductStockUC.DeductStockOutput;
import net.local.poc.serviceinventory.application.ports.usecases.RestoreStockUC;
import net.local.poc.serviceinventory.application.ports.usecases.RestoreStockUC.RestoreStockInput;

@Slf4j
@RestController
@RequestMapping("inventory")
public class InventoryController {

    private final DeductStockUC deductStockPort;
    private final RestoreStockUC restoreStockPort;
    
    public InventoryController(DeductStockUC deductStockPort, RestoreStockUC restoreStockPort) {
        this.deductStockPort = deductStockPort;
        this.restoreStockPort = restoreStockPort;
    }

    @PostMapping("/deduct")
    public ResponseEntity<DeductStockOutput> deductStock(@RequestBody final DeductStockInput input) {
        log.info("[POST ::: /deduct]: {}", input);
        return ResponseEntity.ok(deductStockPort.execute(input));
    }

    @PostMapping("/restore")
    public ResponseEntity<String> restoreStock(@RequestBody final RestoreStockInput input) {
        log.info("[POST ::: /restore]: {}", input);
        restoreStockPort.execute(input);
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).body("ok");
    }
}
