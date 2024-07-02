package net.local.poc.servicepayment.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Customer {
    
    private final UUID customerId;
    private final BigDecimal balance;
    
    public Customer(UUID customerId, BigDecimal balance) {
        this.customerId = customerId;
        this.balance = balance;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Customer credit(BigDecimal value) {
        return new Customer(customerId, balance.add(value));
    }

    public Customer debit(BigDecimal value) {
        return new Customer(customerId, balance.subtract(value));
    }
}
