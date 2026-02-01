CREATE TABLE IF NOT EXISTS transfer (
  id BIGSERIAL PRIMARY KEY,
  source_account VARCHAR(255) NOT NULL,
  destination_account VARCHAR(255) NOT NULL,
  amount BIGINT NOT NULL,
  fee BIGINT NOT NULL,
  total_amount BIGINT NOT NULL,
  scheduled_date DATE NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_code ON transfer (id);