INSERT INTO users (id, username, password, role, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'testuser',
           '$2b$10$dsmbixQSkew7jpl0VYudve3zGytiG62kSSWwi.pCLo9jbeYNw/YjC',
           'USER',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       ) ON CONFLICT (username) DO NOTHING;CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (id, username, password, role, created_at, updated_at)
VALUES (
           gen_random_uuid(),
           'testuser',
           '$2b$10$dsmbixQSkew7jpl0VYudve3zGytiG62kSSWwi.pCLo9jbeYNw/YjC',
           'USER',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       ) ON CONFLICT (username) DO NOTHING;

INSERT INTO accounts (id, user_id, currency, balance, created_at, updated_at)
SELECT gen_random_uuid(), id, 'USD', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users WHERE username = 'testuser'
    ON CONFLICT DO NOTHING;