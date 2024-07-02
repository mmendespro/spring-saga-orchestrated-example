package net.local.poc.servicepayment.infrastructure.gateways;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import net.local.poc.servicepayment.application.ports.persistence.PaymentGateway;
import net.local.poc.servicepayment.domain.Customer;

@Repository
@Scope("singleton")
public class InMemPaymentGateway implements PaymentGateway {

    private Map<UUID, Customer> memDB;

    @Override
    public Optional<Customer> load(UUID customerId) {
       return Optional.ofNullable(memDB.get(customerId));
    }

    @Override
    public void save(Customer customer) {
        memDB.put(customer.getCustomerId(), customer);
    }
    
    @PostConstruct
    private void init(){
        this.memDB = new HashMap<>();
        this.memDB.put(UUID.fromString("90a571ab-91fa-4fda-bf81-f163b69c3e50"), new Customer(UUID.fromString("90a571ab-91fa-4fda-bf81-f163b69c3e50"), new BigDecimal(500d)));
    }
}
