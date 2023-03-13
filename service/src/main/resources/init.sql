CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    firstname  VARCHAR(64)  NOT NULL,
    lastname   VARCHAR(64)  NOT NULL,
    email      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(128) NOT NULL,
    birth_date DATE         NOT NULL,
    role       VARCHAR(32)  NOT NULL,
    status     VARCHAR(32)  NOT NULL
);
CREATE TABLE IF NOT EXISTS account
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    status     VARCHAR(32) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    created_by VARCHAR(64) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(64)
);
CREATE TABLE IF NOT EXISTS bank_account
(
    id                BIGSERIAL PRIMARY KEY,
    account_id        BIGINT      NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    number            VARCHAR(34) NOT NULL UNIQUE,
    type              VARCHAR(32) NOT NULL,
    status            VARCHAR(32) NOT NULL,
    available_balance DECIMAL(19, 2) DEFAULT 0,
    actual_balance    DECIMAL(19, 2) DEFAULT 0
);
CREATE TABLE IF NOT EXISTS bank_card
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    bank_account_id BIGINT      NOT NULL REFERENCES bank_account (id) ON DELETE CASCADE,
    card_number     VARCHAR(25) NOT NULL UNIQUE,
    expiry_date     VARCHAR(25) NOT NULL,
    bank            VARCHAR(32) NOT NULL,
    cvv             VARCHAR(4)  NOT NULL,
    type            VARCHAR(32) NOT NULL
);
CREATE TABLE IF NOT EXISTS utility_account
(
    id            BIGSERIAL PRIMARY KEY,
    number        VARCHAR(128) NOT NULL UNIQUE,
    provider_name VARCHAR(64)  NOT NULL
);
CREATE TABLE IF NOT EXISTS banking_transaction
(
    id               BIGSERIAL PRIMARY KEY,
    amount           DECIMAL(19, 2) NOT NULL,
    type             VARCHAR(64)    NOT NULL,
    reference_number VARCHAR(34)    NOT NULL,
    transaction_id   VARCHAR(64)    NOT NULL UNIQUE,
    time             TIMESTAMP      NOT NULL,
    bank_account_id  BIGINT         NOT NULL REFERENCES bank_account (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS utility_payment
(
    id                 BIGSERIAL PRIMARY KEY,
    amount             DECIMAL(19, 2) NOT NULL,
    reference_number   VARCHAR(34)    NOT NULL,
    status             VARCHAR(64)    NOT NULL,
    utility_account_id BIGINT         NOT NULL REFERENCES utility_account (id),
    transaction_id     VARCHAR(64)    NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS fund_transfer
(
    id             BIGSERIAL PRIMARY KEY,
    from_account   VARCHAR(34)    NOT NULL,
    to_account     VARCHAR(34)    NOT NULL,
    amount         DECIMAL(19, 2) NOT NULL,
    status         VARCHAR(64)    NOT NULL,
    transaction_id VARCHAR(64)    NOT NULL UNIQUE
);