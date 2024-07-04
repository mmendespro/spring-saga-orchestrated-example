DROP TABLE IF EXISTS ORDER_ITEMS;
DROP TABLE IF EXISTS ORDERS;

CREATE TABLE ORDERS (
    ORDER_ID UUID PRIMARY KEY, 
    CUSTOMER_ID UUID NOT NULL,
    ORDER_STATUS VARCHAR(20) NOT NULL
);

CREATE TABLE ORDER_ITEMS (
    ORDER_ITEM_ID INT AUTO_INCREMENT PRIMARY KEY,
    ORDER_ID UUID NOT NULL, 
    PRODUCT_ID UUID NOT NULL, 
    QUANTITY BIGINT NOT NULL, 
    PRICE NUMERIC(10,2) NOT NULL
);