package net.local.poc.serviceorders.presenters;

import java.net.URI;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.serviceorders.application.ports.usecases.PlaceOrderUC;
import net.local.poc.serviceorders.application.ports.usecases.PlaceOrderUC.PlaceOrderInput;

@Slf4j
@RestController
@RequestMapping("orders")
public class PurchaseOrderController {
    
    private final PlaceOrderUC placeOrderPort;

    public PurchaseOrderController(PlaceOrderUC placeOrderPort) {
        this.placeOrderPort = placeOrderPort;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody PlaceOrderInput input) {
        log.info("[POST ::: /orders]: {}", input);
        var response = placeOrderPort.execute(input);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).location(URI.create("/orders/"+response)).body("ok");
    }
}
