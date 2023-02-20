CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    firstname  VARCHAR(64)  NOT NULL,
    lastname   VARCHAR(64)  NOT NULL,
    email      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(128) NOT NULL,
    birth_date DATE         NOT NULL,
    role       VARCHAR(32)  NOT NULL
);

CREATE TABLE IF NOT EXISTS bank_card
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users (id),
    card_number VARCHAR(25) NOT NULL,
    expiry_date VARCHAR(25) NOT NULL,
    bank        VARCHAR(32) NOT NULL,
    cvv         CHAR(4)     NOT NULL,
    card_type   VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS bank_account
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT REFERENCES users (id),
    bank_card_id      BIGINT REFERENCES bank_card (id),
    number            VARCHAR(34) NOT NULL UNIQUE,
    account           VARCHAR(32) NOT NULL,
    status            VARCHAR(32) NOT NULL,
    available_balance DECIMAL(19, 2) DEFAULT NULL,
    actual_balance    DECIMAL(19, 2) DEFAULT NULL
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
    bank_account_id  BIGINT UNIQUE REFERENCES bank_account (id)
);

CREATE TABLE IF NOT EXISTS utility_payment
(
    id                     BIGSERIAL PRIMARY KEY,
    amount                 DECIMAL(19, 2) NOT NULL,
    reference_number       VARCHAR(34)    NOT NULL,
    status                 VARCHAR(64)    NOT NULL,
    utility_account        BIGINT REFERENCES utility_account (id),
    banking_transaction_id BIGINT UNIQUE REFERENCES banking_transaction (id)
);

CREATE TABLE IF NOT EXISTS fund_transfer
(
    id                     BIGSERIAL PRIMARY KEY,
    from_account           VARCHAR(34)    NOT NULL UNIQUE,
    to_account             VARCHAR(34)    NOT NULL UNIQUE,
    amount                 DECIMAL(19, 2) NOT NULL,
    status                 VARCHAR(64)    NOT NULL,
    banking_transaction_id BIGINT UNIQUE REFERENCES banking_transaction (id)
);