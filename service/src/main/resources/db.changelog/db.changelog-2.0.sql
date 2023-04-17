--liquibase formatted sql

--changeset artemkyrychenko:1
ALTER TABLE utility_account
    ADD COLUMN account_id BIGINT NOT NULL
        REFERENCES account (id) ON DELETE CASCADE DEFAULT 1;
