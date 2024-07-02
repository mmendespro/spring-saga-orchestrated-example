package net.local.poc.orderorch.infrastructure.gateways;

import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import net.local.poc.orderorch.application.ports.clients.InventoryClientPort;
import reactor.core.publisher.Mono;

@Repository
public class HttpInventoryClient implements InventoryClientPort {
    
    private final WebClient webClient;

    public HttpInventoryClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8010/inventory").build();
    }

    @Override
    public Mono<StockOutput> deduct(StockInput input) {
        return this.webClient
                    .post()
                    .uri("/deduct")
                    .body(BodyInserters.fromValue(input))
                    .retrieve()
                    .bodyToMono(StockOutput.class);
    }

    @Override
    public Mono<Void> restore(StockInput input) {
        return this.webClient
                   .post()
                   .uri("/restore")
                   .body(BodyInserters.fromValue(input))
                   .retrieve()
                   .bodyToMono(Void.class);
    }
}
