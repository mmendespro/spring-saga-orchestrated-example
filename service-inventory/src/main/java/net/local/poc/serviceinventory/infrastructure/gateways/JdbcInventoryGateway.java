package net.local.poc.serviceinventory.infrastructure.gateways;

import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import net.local.poc.serviceinventory.application.ports.persistence.InventoryGateway;
import net.local.poc.serviceinventory.domain.entities.ProductInventory;

@Repository
public class JdbcInventoryGateway implements InventoryGateway {
    
    private final JdbcClient jdbcClient;

    public JdbcInventoryGateway(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<ProductInventory> load(UUID productId) {
        return jdbcClient.sql("SELECT PRODUCT_ID, STOCK FROM INVENTORIES WHERE PRODUCT_ID = :productId")
                         .param("productId", productId)
                         .query((rs, rowNum) -> new ProductInventory(UUID.fromString(rs.getString("PRODUCT_ID")), rs.getLong("STOCK")))
                         .optional();
    }

    @Override
    public void save(ProductInventory inventory) {
        jdbcClient.sql("UPDATE INVENTORIES SET STOCK = :newStock WHERE PRODUCT_ID = :productId")
                  .param("newStock", inventory.getQuantity())
                  .param("productId", inventory.getProductId())
                  .update();
    }
}
