package net.local.poc.serviceorders.infrastructure.gateways;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import net.local.poc.serviceorders.application.ports.gateways.PurchaseOrderGateway;
import net.local.poc.serviceorders.domain.entities.OrderStatus;
import net.local.poc.serviceorders.domain.entities.PurchaseOrder;
import net.local.poc.serviceorders.domain.entities.PurchaseOrderItem;

@Repository
public class JdbcPurchaseOrderGateway implements PurchaseOrderGateway {

    private final JdbcClient jdbcClient;

    public JdbcPurchaseOrderGateway(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<PurchaseOrder> load(UUID orderId) {
        var items = jdbcClient.sql("SELECT * FROM ORDER_ITEMS WHERE ORDER_ID = :orderId")
                              .param("orderId", orderId)
                              .query((rs, rowNum) -> new PurchaseOrderItem(UUID.fromString(rs.getString("PRODUCT_ID")),
                                                                           rs.getLong("QUANTITY"),
                                                                           rs.getBigDecimal("PRICE")))
                              .list();

        var order = jdbcClient.sql("SELECT * FROM ORDERS WHERE ORDER_ID = :orderId")
                              .param("orderId", orderId)
                              .query((rs, rowNum) -> new PurchaseOrder(UUID.fromString(rs.getString("ORDER_ID")),
                                                                       UUID.fromString(rs.getString("CUSTOMER_ID")),
                                                                       OrderStatus.valueOf(rs.getString("ORDER_STATUS")),
                                                                       items))
                              .optional();
        return order;
    }

    @Override
    public void save(PurchaseOrder order) {
        jdbcClient.sql("INSERT INTO ORDERS(ORDER_ID, CUSTOMER_ID, ORDER_STATUS) VALUES (:orderId, :customerId, :status)")
                  .param("orderId", order.getOrderId())
                  .param("customerId", order.getCustomerId())
                  .param("status", order.getStatus().name())
                  .update();

        order.getItems()
             .stream()
             .forEach(item -> {
                jdbcClient.sql("INSERT INTO ORDER_ITEMS(ORDER_ID, PRODUCT_ID, QUANTITY, PRICE) VALUES (:orderId, :productId, :quantity, :price)")
                          .param("orderId", order.getOrderId())
                          .param("productId", item.getProductId())
                          .param("quantity", item.getQuantity())
                          .param("price", item.getPrice())
                          .update();
             });
    }

    @Override
    public void update(PurchaseOrder order) {
        jdbcClient.sql("UPDATE ORDERS SET ORDER_STATUS = :status WHERE ORDER_ID = :orderId")
                  .param("orderId", order.getOrderId())
                  .param("STATUS", order.getStatus().name())
                  .update();

    }
    
}
