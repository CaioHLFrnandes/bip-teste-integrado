CREATE TABLE beneficio (
                           id BIGSERIAL PRIMARY KEY,
                           titular VARCHAR(255) NOT NULL,
                           saldo NUMERIC(18,2) NOT NULL DEFAULT 0,
                           created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                           updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
                           version BIGINT DEFAULT 0
);

CREATE INDEX idx_beneficio_titular ON beneficio(titular);
