-- Create the accounts table
CREATE TABLE IF NOT EXISTS accounts (
                                        id UUID PRIMARY KEY,
                                        user_id UUID NOT NULL,
                                        currency VARCHAR(10) NOT NULL,
    balance DECIMAL(38, 18) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create the ledger_entries table (DOUBLE-ENTRY LEDGER!)
CREATE TABLE IF NOT EXISTS ledger_entries (
                                              id UUID PRIMARY KEY,
                                              transaction_id UUID NOT NULL,
                                              account_id UUID NOT NULL REFERENCES accounts(id),
    amount DECIMAL(38, 18) NOT NULL,
    direction VARCHAR(10) NOT NULL CHECK (direction IN ('CREDIT', 'DEBIT')),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create the orders table
CREATE TABLE IF NOT EXISTS orders (
                                      id UUID PRIMARY KEY,
                                      user_id UUID NOT NULL,
                                      side VARCHAR(4) NOT NULL CHECK (side IN ('BUY', 'SELL')),
    order_type VARCHAR(10) NOT NULL CHECK (order_type IN ('LIMIT', 'MARKET')),
    price DECIMAL(38, 18),
    quantity DECIMAL(38, 18) NOT NULL,
    filled_quantity DECIMAL(38, 18) DEFAULT 0,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'FILLED', 'PARTIALLY_FILLED', 'CANCELLED')),
    idempotency_key UUID UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create the trades table
CREATE TABLE IF NOT EXISTS trades (
                                      id UUID PRIMARY KEY,
                                      buy_order_id UUID NOT NULL REFERENCES orders(id),
    sell_order_id UUID NOT NULL REFERENCES orders(id),
    price DECIMAL(38, 18) NOT NULL,
    quantity DECIMAL(38, 18) NOT NULL,
    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes for performance
CREATE INDEX idx_ledger_account_id ON ledger_entries(account_id);
CREATE INDEX idx_ledger_transaction_id ON ledger_entries(transaction_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_idempotency_key ON orders(idempotency_key);
CREATE INDEX idx_trades_buy_order ON trades(buy_order_id);
CREATE INDEX idx_trades_sell_order ON trades(sell_order_id);