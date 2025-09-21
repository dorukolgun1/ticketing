CREATE TABLE IF NOT EXISTS event (
                                     id BIGSERIAL PRIMARY KEY,
                                     code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time   TIMESTAMPTZ NOT NULL
    );

CREATE TABLE IF NOT EXISTS inventory (
                                         id BIGSERIAL PRIMARY KEY,
                                         event_code VARCHAR(64) NOT NULL REFERENCES event(code),
    ticket_type VARCHAR(16) NOT NULL,
    total INT NOT NULL CHECK (total >= 0),
    sold  INT NOT NULL CHECK (sold >= 0),
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT inventory_uc UNIQUE(event_code, ticket_type)
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      order_code VARCHAR(64) NOT NULL UNIQUE,
    event_code VARCHAR(64) NOT NULL REFERENCES event(code),
    ticket_type VARCHAR(16) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );

CREATE INDEX IF NOT EXISTS idx_orders_event_code ON orders(event_code);
