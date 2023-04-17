--liquibase formatted sql

--changeset artemkyrychenko:1
ALTER TABLE users
    ADD COLUMN image VARCHAR(64);
