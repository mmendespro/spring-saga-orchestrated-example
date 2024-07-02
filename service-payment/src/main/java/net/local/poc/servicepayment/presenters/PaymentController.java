package net.local.poc.servicepayment.presenters;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.servicepayment.application.ports.usecases.ProcessPaymentUC;
import net.local.poc.servicepayment.application.ports.usecases.ProcessPaymentUC.PaymentInput;
import net.local.poc.servicepayment.application.ports.usecases.ProcessPaymentUC.PaymentOutput;

@Slf4j
@RestController
@RequestMapping("payments")
public class PaymentController {
    
    private final ProcessPaymentUC processPaymentPort;

    public PaymentController(ProcessPaymentUC processPaymentPort) {
        this.processPaymentPort = processPaymentPort;
    }

    @PostMapping
    public ResponseEntity<PaymentOutput> processPayment(@RequestBody PaymentInput input) {
        log.info("[POST ::: /payments]: {}", input);
        return ResponseEntity.ok(processPaymentPort.execute(input));
    }
}
