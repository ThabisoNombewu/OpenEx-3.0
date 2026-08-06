-- System user that owns the reserve accounts used to fund faucet deposits
INSERT INTO users (id, username, password, role, created_at, updated_at)
VALUES (
           '00000000-0000-0000-0000-000000000001',
           'SYSTEM_RESERVE',
           -- unusable bcrypt hash placeholder — this account never logs in
           '$2a$10$abcdefghijklmnopqrstuuvwxyzABCDEFGHIJKLMNOPQRSTUVWX',
           'SYSTEM',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       ) ON CONFLICT (id) DO NOTHING;

-- Reserve accounts with effectively unlimited simulated funds
INSERT INTO accounts (id, user_id, currency, balance, created_at, updated_at)
VALUES
    ('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000001', 'USD', 999999999999999999.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000001', 'BTC', 999999999999999999.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    ON CONFLICT (id) DO NOTHING;