package net.local.poc.orderorch.infrastructure.gateways;

import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import net.local.poc.orderorch.application.ports.clients.PaymentClientPort;
import reactor.core.publisher.Mono;

@Repository
public class HttpPaymentClient implements PaymentClientPort {

    private final WebClient webClient;

    public HttpPaymentClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8020/payments").build();
    }

    @Override
    public Mono<PaymentOutput> processPayment(PaymentInput input) {
        return this.webClient
                    .post()
                    .body(BodyInserters.fromValue(input))
                    .retrieve()
                    .bodyToMono(PaymentOutput.class);
    }
    
}
