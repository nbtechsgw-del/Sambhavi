USE tourism_db;

ALTER TABLE payments
    MODIFY transaction_id VARCHAR(100) NULL,
    MODIFY status ENUM('PENDING', 'SUCCESS', 'REFUNDED', 'FAILED') NOT NULL DEFAULT 'PENDING';

ALTER TABLE payments ADD COLUMN gateway_provider VARCHAR(40);
ALTER TABLE payments ADD COLUMN gateway_order_id VARCHAR(100);
ALTER TABLE payments ADD COLUMN gateway_payment_id VARCHAR(100);
ALTER TABLE payments ADD COLUMN gateway_signature VARCHAR(255);
ALTER TABLE payments ADD COLUMN refund_id VARCHAR(100);
