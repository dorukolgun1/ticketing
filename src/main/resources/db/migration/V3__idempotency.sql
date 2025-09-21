ALTER TABLE orders ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(64);
CREATE UNIQUE INDEX IF NOT EXISTS ux_orders_idempotency ON orders(idempotency_key);
