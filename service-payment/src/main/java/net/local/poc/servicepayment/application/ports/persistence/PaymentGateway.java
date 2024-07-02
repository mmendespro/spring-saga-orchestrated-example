package net.local.poc.servicepayment.application.ports.persistence;

import java.util.Optional;
import java.util.UUID;

import net.local.poc.servicepayment.domain.Customer;

public interface PaymentGateway {
    Optional<Customer> load(UUID customerId);
    void save(Customer customer);
}
