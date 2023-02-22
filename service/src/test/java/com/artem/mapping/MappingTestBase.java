package com.artem.mapping;

import com.artem.util.ConnectionManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class MappingTestBase {

    private static final String CLEAN_SQL = "\n" +
            "DELETE FROM fund_transfer;\n" +
            "DELETE FROM utility_payment;\n" +
            "DELETE FROM banking_transaction;\n" +
            "DELETE FROM utility_account;\n" +
            "DELETE FROM bank_card;\n" +
            "DELETE FROM bank_account;\n" +
            "DELETE FROM account;\n" +
            "DELETE FROM users;";
    private static final String CREATE_SQL_USERS = """
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
            """;

    private static final String CREATE_SQL_ACCOUNT = """
            CREATE TABLE IF NOT EXISTS account
            (
                id         BIGSERIAL PRIMARY KEY,
                user_id    BIGINT UNIQUE REFERENCES users (id),
                status     VARCHAR(32) NOT NULL,
                created_at TIMESTAMP   NOT NULL,
                created_by VARCHAR(64) NOT NULL,
                updated_at TIMESTAMP,
                updated_by VARCHAR(64)
            );
            """;

    private static final String CREATE_SQL_BANK_ACCOUNT = """
            CREATE TABLE IF NOT EXISTS bank_account
            (
                id                BIGSERIAL PRIMARY KEY,
                account_id        BIGINT REFERENCES account (id) NOT NULL,
                number            VARCHAR(34)                  NOT NULL UNIQUE,
                type              VARCHAR(32)                  NOT NULL,
                status            VARCHAR(32)                  NOT NULL,
                available_balance DECIMAL(19, 2) DEFAULT 0,
                actual_balance    DECIMAL(19, 2) DEFAULT 0
            );
            """;

    private static final String CREATE_SQL_BANK_CARD = """
            CREATE TABLE IF NOT EXISTS bank_card
            (
                id              BIGSERIAL PRIMARY KEY,
                user_id         BIGINT REFERENCES users (id),
                bank_account_id BIGINT REFERENCES bank_account (id),
                card_number     VARCHAR(25) NOT NULL UNIQUE,
                expiry_date     VARCHAR(25) NOT NULL,
                bank            VARCHAR(32) NOT NULL,
                cvv             CHAR(4)     NOT NULL,
                type            VARCHAR(32) NOT NULL
            );
            """;

    private static final String CREATE_SQL_UTILITY_ACCOUNT = """
            CREATE TABLE IF NOT EXISTS utility_account
            (
                id            BIGSERIAL PRIMARY KEY,
                number        VARCHAR(128) NOT NULL UNIQUE,
                provider_name VARCHAR(64)  NOT NULL
            );
            """;

    private static final String CREATE_SQL_BANKING_TRANSACTION = """
            CREATE TABLE IF NOT EXISTS banking_transaction
            (
                id               BIGSERIAL PRIMARY KEY,
                amount           DECIMAL(19, 2)                      NOT NULL,
                type             VARCHAR(64)                         NOT NULL,
                reference_number VARCHAR(34)                         NOT NULL,
                transaction_id   VARCHAR(64)                         NOT NULL UNIQUE,
                bank_account_id  BIGINT REFERENCES bank_account (id) NOT NULL
            );
            """;

    private static final String CREATE_SQL_UTILITY_PAYMENT = """
            CREATE TABLE IF NOT EXISTS utility_payment
            (
                id                     BIGSERIAL PRIMARY KEY,
                amount                 DECIMAL(19, 2) NOT NULL,
                reference_number       VARCHAR(34)    NOT NULL,
                status                 VARCHAR(64)    NOT NULL,
                utility_account_id     BIGINT REFERENCES utility_account (id),
                banking_transaction_id BIGINT UNIQUE REFERENCES banking_transaction (id)
            );
            """;
    private static final String CREATE_SQL_FUND_TRANSFER = """
            CREATE TABLE IF NOT EXISTS fund_transfer
            (
                id                     BIGSERIAL PRIMARY KEY,
                from_account           VARCHAR(34)    NOT NULL,
                to_account             VARCHAR(34)    NOT NULL,
                amount                 DECIMAL(19, 2) NOT NULL,
                status                 VARCHAR(64)    NOT NULL,
                banking_transaction_id BIGINT UNIQUE REFERENCES banking_transaction (id)
            );
            """;


    @BeforeAll
    static void prepareDatabase() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_SQL_USERS);
            statement.execute(CREATE_SQL_ACCOUNT);
            statement.execute(CREATE_SQL_BANK_ACCOUNT);
            statement.execute(CREATE_SQL_BANK_CARD);
            statement.execute(CREATE_SQL_UTILITY_ACCOUNT);
            statement.execute(CREATE_SQL_BANKING_TRANSACTION);
            statement.execute(CREATE_SQL_UTILITY_PAYMENT);
            statement.execute(CREATE_SQL_FUND_TRANSFER);
        }
    }

    @BeforeEach
    void cleanData() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_SQL);
        }
    }

}
