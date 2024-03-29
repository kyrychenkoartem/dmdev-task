INSERT INTO users (id, firstname, lastname, email, password, birth_date, role, status)
VALUES (1, 'Ivan', 'Ivanov', 'ivan@gmail.com', '{noop}123', '2000-01-01', 'ADMIN', 'ACTIVE'),
       (2, 'Artem', 'Artemov', 'artem@gmail.com', '{noop}123', '2000-01-01', 'USER', 'ACTIVE'),
       (3, 'Petr', 'Ivanov', 'petr@gmail.com', '{noop}123', '2000-01-01', 'USER', 'ACTIVE'),
       (4, 'John', 'Liskov', 'john@gmail.com', '{noop}123', '2000-01-01', 'USER', 'ACTIVE'),
       (5, 'Mike', 'Dunk', 'mike@gmail.com', '{noop}123', '2000-01-01', 'USER', 'ACTIVE'),
       (6, 'Test', 'Test', 'test1@gmail.com', '{noop}123', '2000-01-01', 'USER', 'ACTIVE');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO account (id, user_id, status, created_at, created_by, updated_at, updated_by)
VALUES (1, 1, 'ACTIVE', now(), 'ivan@gmail.com', null, null),
       (2, 2, 'ACTIVE', now(), 'artem@gmail.com', null, null),
       (3, 3, 'ACTIVE', now(), 'petr@gmail.com', null, null),
       (4, 4, 'ACTIVE', now(), 'john@gmail.com', null, null),
       (5, 5, 'ACTIVE', now(), 'mike@gmail.com', null, null);
SELECT SETVAL('account_id_seq', (SELECT MAX(id) FROM account));

INSERT INTO bank_account (id, account_id, number, type, status, available_balance, actual_balance)
VALUES (1, 1, '1234567890', 'CHECKING_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (2, 1, '2345678901', 'SAVINGS_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (3, 1, '3456789012', 'LOAN_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (4, 2, '4567890123', 'CHECKING_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (5, 2, '5678901234', 'SAVINGS_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (6, 2, '67890123456', 'LOAN_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (7, 3, '7890123456', 'CHECKING_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (8, 3, '8901234567', 'SAVINGS_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (9, 3, '9012345678', 'LOAN_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (10, 3, '0123456789', 'FIXED_DEPOSIT', 'ACTIVE', 200.00, 100.00),
       (11, 4, '1134567890', 'CHECKING_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (12, 4, '1224567890', 'SAVINGS_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (13, 5, '1233567890', 'CHECKING_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (14, 5, '1234467890', 'SAVINGS_ACCOUNT', 'ACTIVE', 200.00, 100.00),
       (15, 5, '1234557890', 'FIXED_DEPOSIT', 'ACTIVE', 200.00, 100.00);
SELECT SETVAL('bank_account_id_seq', (SELECT MAX(id) FROM bank_account));

INSERT INTO bank_card (id, user_id, bank_account_id, card_number, expiry_date, bank, cvv, type)
VALUES (1, 1, 1, '1234567890123456', '11/29', 'CIBC', '123', 'DEBIT');
SELECT SETVAL('bank_card_id_seq', (SELECT MAX(id) FROM bank_card));


INSERT INTO utility_account (id, account_id, number, provider_name)
VALUES (1, 1, '12345', 'Koodo'),
       (2, 2, '12346', 'Telus'),
       (3, 4, '12347', 'Hydro');
SELECT SETVAL('utility_account_id_seq', (SELECT MAX(id) FROM utility_account));

INSERT INTO banking_transaction (id, amount, type, status, reference_number, transaction_id, time, bank_account_id)
VALUES (1, 50.00, 'DEPOSIT', 'SUCCESS', '12345', '68dd91bf-3de2-4ddf-99c1-209e22b17618', '1982-01-08 00:00:00.000000', 1),
       (2, 50.00, 'DEPOSIT', 'SUCCESS', '2345678901', '9d3bf103-8558-45cc-8393-7d7fb8188ae2', '2021-09-19 00:00:00.000000', 1),
       (3, 50.00, 'DEPOSIT', 'SUCCESS', '3456789012', '9513172a-1389-4c81-aba2-496f3a139337', '2018-05-27 00:00:00.000000', 1),
       (4, 50.00, 'DEPOSIT', 'SUCCESS', '1234567890', 'fd68aeee-aaf0-49ec-912a-42876b25e247', '1993-01-19 00:00:00.000000', 1),
       (5, 50.00, 'DEPOSIT', 'SUCCESS', '2345678901', 'beb56e73-589f-430f-a824-dd1bc3e4652a', '2015-07-11 00:00:00.000000', 1),
       (6, 50.00, 'DEPOSIT', 'SUCCESS', '12345', '7aaf78cc-ebe3-470e-b5be-d180518023e4', '2019-08-02 00:00:00.000000', 1),
       (7, 50.00, 'DEPOSIT', 'SUCCESS', '3456789012', 'da9caabb-cd9a-409b-9b93-fe01cc4a4bca', '1990-07-01 00:00:00.000000', 2),
       (8, 50.00, 'DEPOSIT', 'SUCCESS', '1234567890', '0dfd996b-368d-49d1-bd47-833f606d6f65', '2003-04-05 00:00:00.000000', 2),
       (9, 50.00, 'DEPOSIT', 'SUCCESS', '3456789012', '7a3df576-bc21-4919-8ee1-924b16b8f1fb', '1970-07-23 00:00:00.000000', 2),
       (10, 50.00, 'DEPOSIT', 'SUCCESS', '1234567890', 'a79feacb-f400-458a-b6a6-129925ff696d', '2009-02-16 00:00:00.000000', 2),
       (11, 50.00, 'DEPOSIT', 'SUCCESS', '12345', '301bb7a4-3b7f-4091-b16c-9f50a8dbc1c1', '1976-12-19 00:00:00.000000', 2),
       (12, 50.00, 'DEPOSIT', 'SUCCESS', '2345678901', 'b518450e-b263-4636-80e9-e997c3a46aa1', '2002-07-28 00:00:00.000000', 3),
       (13, 50.00, 'DEPOSIT', 'SUCCESS', '2345678901', 'ed48eb60-c4ee-47b3-9a28-196f79a464f9', '2009-03-31 00:00:00.000000', 3),
       (14, 50.00, 'DEPOSIT', 'SUCCESS', '1234567890', '9b137f05-4d10-43ce-af60-8991c7873ec5', '2011-04-17 00:00:00.000000', 3),
       (15, 50.00, 'DEPOSIT', 'SUCCESS', '5678901234', '1d45fea8-4486-4b33-9eaf-f0b6e1b6c8de', '1989-02-07 00:00:00.000000', 4),
       (16, 50.00, 'DEPOSIT', 'SUCCESS', '12345', 'b9cfb440-7e05-44fc-9332-cbf930484ec7', '2006-07-12 00:00:00.000000', 4),
       (17, 50.00, 'DEPOSIT', 'SUCCESS', '67890123456', '73d8b48c-8cec-49f7-b7ed-ee250cd43647', '2022-01-22 00:00:00.000000', 4),
       (18, 50.00, 'DEPOSIT', 'SUCCESS', '4567890123', '0720ebf5-ecee-4842-bbae-7baf36a3ab48', '1998-08-08 00:00:00.000000', 5),
       (19, 50.00, 'DEPOSIT', 'SUCCESS', '4567890123', 'd7ca704c-ce37-4d3f-9f7d-67b413572d92', '1987-06-02 00:00:00.000000', 5),
       (20, 50.00, 'DEPOSIT', 'SUCCESS', '67890123456', 'be891dfa-824c-43a8-b19e-fe69f85fdcb3', '2006-08-03 00:00:00.000000', 5),
       (21, 50.00, 'DEPOSIT', 'SUCCESS', '12346', 'ccabd2d7-a26d-4aa5-a15e-1491889ff8d8', '1985-07-29 00:00:00.000000', 6),
       (22, 50.00, 'DEPOSIT', 'SUCCESS', '5678901234', '87f0e871-c6c9-43b9-a94d-59592eed05b5', '2002-02-26 00:00:00.000000', 6),
       (23, 50.00, 'DEPOSIT', 'SUCCESS', '67890123456', '2a04b807-4266-48b2-b6aa-2be1c626eedf', '2018-07-21 00:00:00.000000', 6),
       (24, 50.00, 'DEPOSIT', 'SUCCESS', '0123456789', 'b418bb5a-fe74-4b4d-b924-2bd7672610bb', '2006-05-05 00:00:00.000000', 7),
       (25, 50.00, 'DEPOSIT', 'SUCCESS', '9012345678', '966e6636-ae5e-4be9-bcbf-8d851a9ea4ea', '1993-01-10 00:00:00.000000', 7),
       (26, 50.00, 'DEPOSIT', 'SUCCESS', '12346', '75d527ac-9268-46cd-96a0-bcd60683b753', '2004-09-08 00:00:00.000000', 7),
       (27, 50.00, 'DEPOSIT', 'SUCCESS', '0123456789', '99be7082-f9ee-47d1-a5c3-4690c52a8d2f', '1976-10-12 00:00:00.000000', 8),
       (28, 50.00, 'DEPOSIT', 'SUCCESS', '7890123456', '2a57cfbd-04a7-4432-a854-fc07487d9673', '2018-12-19 00:00:00.000000', 8),
       (29, 50.00, 'DEPOSIT', 'SUCCESS', '9012345678', 'bf29d9b3-b08f-4914-8ae6-04fd7fe4846c', '1996-02-16 00:00:00.000000', 8),
       (30, 50.00, 'DEPOSIT', 'SUCCESS', '0123456789', '94234ae8-089f-4028-ab6c-54fccd3dd0eb', '2022-11-23 00:00:00.000000', 9),
       (31, 50.00, 'DEPOSIT', 'SUCCESS', '12346', 'efb6e54e-c9ec-44dd-87ee-a7add9aa2613', '1978-04-14 00:00:00.000000', 9),
       (32, 50.00, 'DEPOSIT', 'SUCCESS', '7890123456', 'bd7d0aa1-47f9-44a2-8b3d-3beca6b2ee40', '2011-01-23 00:00:00.000000', 9),
       (33, 50.00, 'DEPOSIT', 'SUCCESS', '9012345678', 'a6bb44a0-f996-459c-a012-730c453881a2', '2009-06-10 00:00:00.000000', 10),
       (34, 50.00, 'DEPOSIT', 'SUCCESS', '9012345678', 'b048c0d4-a052-4f89-a809-52af4044b985', '1976-04-04 00:00:00.000000', 10),
       (35, 50.00, 'DEPOSIT', 'SUCCESS', '7890123456', '3870bac8-0942-401e-b109-e30841d17d19', '1980-10-22 00:00:00.000000', 10),
       (36, 50.00, 'DEPOSIT', 'SUCCESS', '12346', 'bf99d65f-4861-432b-9abb-047d48887bdf', '1996-09-15 00:00:00.000000', 10),
       (37, 50.00, 'DEPOSIT', 'SUCCESS', '1224567890', '0667b457-1c86-4cf2-b43b-777669c02ef9', '1993-11-19 00:00:00.000000', 11),
       (38, 50.00, 'DEPOSIT', 'SUCCESS', '1134567890', 'c3408b75-ee22-4bff-828d-6c74fdd97a78', '1982-07-17 00:00:00.000000', 11),
       (39, 50.00, 'DEPOSIT', 'SUCCESS', '1224567890', 'bdd50ed6-6c5b-49c0-a13e-e7dc08589a1e', '1972-07-09 00:00:00.000000', 11),
       (40, 50.00, 'DEPOSIT', 'SUCCESS', '1134567890', 'dcf87fc2-7f72-4c26-a9e7-864c14e65deb', '2008-03-14 00:00:00.000000', 11),
       (41, 50.00, 'DEPOSIT', 'SUCCESS', '12347', '5ca8ded0-7e65-431c-8487-11446f39153a', '2013-12-11 00:00:00.000000', 11),
       (42, 50.00, 'DEPOSIT', 'SUCCESS', '1224567890', '33681ba1-a0bf-4547-a793-78549e5b27c0', '2018-07-11 00:00:00.000000', 12),
       (43, 50.00, 'DEPOSIT', 'SUCCESS', '1134567890', '8cda4a1f-f85a-418f-98a2-b4c6bba9eeef', '1975-08-22 00:00:00.000000', 12),
       (44, 50.00, 'DEPOSIT', 'SUCCESS', '1234557890', '8dfcdfd5-5f95-4eea-901d-5b7345f0fb31', '1987-12-20 00:00:00.000000', 13),
       (45, 50.00, 'DEPOSIT', 'SUCCESS', '1233567890', 'df67c73a-6e89-4c41-b96a-08e17a5811c5', '1972-08-31 00:00:00.000000', 13),
       (46, 50.00, 'DEPOSIT', 'SUCCESS', '12347', '0b32197e-ac4d-4a8e-a8f4-f979c096e1e7', '2018-06-08 00:00:00.000000', 13),
       (47, 50.00, 'DEPOSIT', 'SUCCESS', '1234467890', '25fe0d17-f58b-4a1d-a867-1155431b4915', '2014-05-12 00:00:00.000000', 13),
       (48, 50.00, 'DEPOSIT', 'SUCCESS', '1234467890', 'ddfd29ef-a31c-4514-a6aa-81cf75be95ca', '2018-02-20 00:00:00.000000', 14),
       (49, 50.00, 'DEPOSIT', 'SUCCESS', '1234557890', '6d0baf3e-498d-4f27-bfa9-45d610cb301f', '1986-12-06 00:00:00.000000', 14),
       (50, 50.00, 'DEPOSIT', 'SUCCESS', '1233567890', 'a916114f-9e6d-4b20-8f83-b1b8383bde1a', '2012-02-24 00:00:00.000000', 14),
       (51, 50.00, 'DEPOSIT', 'SUCCESS', '12347', '1d41ff30-b267-4f66-9608-b87820ca38f1', '1998-01-18 00:00:00.000000', 15),
       (52, 50.00, 'DEPOSIT', 'SUCCESS', '1234557890', '188431b8-0bf1-4ca7-82a6-b4ee75fedc60', '2001-07-03 00:00:00.000000', 15),
       (53, 50.00, 'DEPOSIT', 'SUCCESS', '12347', '1f73ca16-69df-456d-b0c5-c3c5b597539e', '1999-03-26 00:00:00.000000', 15);
SELECT SETVAL('banking_transaction_id_seq', (SELECT MAX(id) FROM banking_transaction));

INSERT INTO utility_payment (id, amount, reference_number, status, utility_account_id, transaction_id)
VALUES (1, 50.00, '12345', 'SUCCESS', 1, '68dd91bf-3de2-4ddf-99c1-209e22b17618'),
       (2, 50.00, '12345', 'SUCCESS', 1, '7aaf78cc-ebe3-470e-b5be-d180518023e4'),
       (3, 50.00, '12345', 'SUCCESS', 1, '301bb7a4-3b7f-4091-b16c-9f50a8dbc1c1'),
       (4, 50.00, '12345', 'SUCCESS', 1, 'b9cfb440-7e05-44fc-9332-cbf930484ec7'),
       (5, 50.00, '12346', 'SUCCESS', 2, 'ccabd2d7-a26d-4aa5-a15e-1491889ff8d8'),
       (6, 50.00, '12346', 'SUCCESS', 2, '75d527ac-9268-46cd-96a0-bcd60683b753'),
       (7, 50.00, '12346', 'SUCCESS', 2, 'efb6e54e-c9ec-44dd-87ee-a7add9aa2613'),
       (8, 50.00, '12346', 'SUCCESS', 2, 'bf99d65f-4861-432b-9abb-047d48887bdf'),
       (9, 50.00, '12347', 'SUCCESS', 3, '5ca8ded0-7e65-431c-8487-11446f39153a'),
       (10, 50.00, '12347', 'SUCCESS', 3, '0b32197e-ac4d-4a8e-a8f4-f979c096e1e7'),
       (11, 50.00, '12347', 'SUCCESS', 3, '1d41ff30-b267-4f66-9608-b87820ca38f1'),
       (12, 50.00, '12347', 'SUCCESS', 3, '1f73ca16-69df-456d-b0c5-c3c5b597539e');
SELECT SETVAL('utility_payment_id_seq', (SELECT MAX(id) FROM utility_payment));


INSERT INTO fund_transfer (id, from_account, to_account, amount, status, transaction_id)
VALUES (1, '1234567890', '2345678901', 50.00, 'SUCCESS', '9d3bf103-8558-45cc-8393-7d7fb8188ae2'),
       (2, '2345678901', '3456789012', 50.00, 'SUCCESS', '9513172a-1389-4c81-aba2-496f3a139337'),
       (3, '3456789012', '1234567890', 50.00, 'SUCCESS', 'fd68aeee-aaf0-49ec-912a-42876b25e247'),
       (4, '1234567890', '2345678901', 50.00, 'SUCCESS', 'beb56e73-589f-430f-a824-dd1bc3e4652a'),
       (5, '2345678901', '3456789012', 50.00, 'SUCCESS', 'da9caabb-cd9a-409b-9b93-fe01cc4a4bca'),
       (6, '3456789012', '1234567890', 50.00, 'SUCCESS', '0dfd996b-368d-49d1-bd47-833f606d6f65'),
       (7, '1234567890', '3456789012', 50.00, 'SUCCESS', '7a3df576-bc21-4919-8ee1-924b16b8f1fb'),
       (8, '2345678901', '1234567890', 50.00, 'SUCCESS', 'a79feacb-f400-458a-b6a6-129925ff696d'),
       (9, '3456789012', '2345678901', 50.00, 'SUCCESS', 'b518450e-b263-4636-80e9-e997c3a46aa1'),
       (10, '1234567890', '2345678901', 50.00, 'SUCCESS', 'ed48eb60-c4ee-47b3-9a28-196f79a464f9'),
       (11, '2345678901', '1234567890', 50.00, 'SUCCESS', '9b137f05-4d10-43ce-af60-8991c7873ec5'),
       (12, '4567890123', '5678901234', 50.00, 'SUCCESS', '1d45fea8-4486-4b33-9eaf-f0b6e1b6c8de'),
       (13, '5678901234', '67890123456', 50.00, 'SUCCESS', '73d8b48c-8cec-49f7-b7ed-ee250cd43647'),
       (14, '67890123456', '4567890123', 50.00, 'SUCCESS', '0720ebf5-ecee-4842-bbae-7baf36a3ab48'),
       (15, '5678901234', '4567890123', 50.00, 'SUCCESS', 'd7ca704c-ce37-4d3f-9f7d-67b413572d92'),
       (16, '4567890123', '67890123456', 50.00, 'SUCCESS', 'be891dfa-824c-43a8-b19e-fe69f85fdcb3'),
       (17, '67890123456', '5678901234', 50.00, 'SUCCESS', '87f0e871-c6c9-43b9-a94d-59592eed05b5'),
       (18, '5678901234', '67890123456', 50.00, 'SUCCESS', '2a04b807-4266-48b2-b6aa-2be1c626eedf'),
       (19, '7890123456', '0123456789', 50.00, 'SUCCESS', 'b418bb5a-fe74-4b4d-b924-2bd7672610bb'),
       (20, '8901234567', '9012345678', 50.00, 'SUCCESS', '966e6636-ae5e-4be9-bcbf-8d851a9ea4ea'),
       (21, '9012345678', '0123456789', 50.00, 'SUCCESS', '99be7082-f9ee-47d1-a5c3-4690c52a8d2f'),
       (22, '0123456789', '7890123456', 50.00, 'SUCCESS', '2a57cfbd-04a7-4432-a854-fc07487d9673'),
       (23, '7890123456', '9012345678', 50.00, 'SUCCESS', 'bf29d9b3-b08f-4914-8ae6-04fd7fe4846c'),
       (24, '8901234567', '0123456789', 50.00, 'SUCCESS', '94234ae8-089f-4028-ab6c-54fccd3dd0eb'),
       (25, '9012345678', '7890123456', 50.00, 'SUCCESS', 'bd7d0aa1-47f9-44a2-8b3d-3beca6b2ee40'),
       (26, '0123456789', '9012345678', 50.00, 'SUCCESS', 'a6bb44a0-f996-459c-a012-730c453881a2'),
       (27, '7890123456', '9012345678', 50.00, 'SUCCESS', 'b048c0d4-a052-4f89-a809-52af4044b985'),
       (28, '8901234567', '7890123456', 50.00, 'SUCCESS', '3870bac8-0942-401e-b109-e30841d17d19'),
       (29, '1134567890', '1224567890', 50.00, 'SUCCESS', '0667b457-1c86-4cf2-b43b-777669c02ef9'),
       (30, '1224567890', '1134567890', 50.00, 'SUCCESS', 'c3408b75-ee22-4bff-828d-6c74fdd97a78'),
       (31, '1134567890', '1224567890', 50.00, 'SUCCESS', 'bdd50ed6-6c5b-49c0-a13e-e7dc08589a1e'),
       (32, '1224567890', '1134567890', 50.00, 'SUCCESS', 'dcf87fc2-7f72-4c26-a9e7-864c14e65deb'),
       (33, '1134567890', '1224567890', 50.00, 'SUCCESS', '33681ba1-a0bf-4547-a793-78549e5b27c0'),
       (34, '1224567890', '1134567890', 50.00, 'SUCCESS', '8cda4a1f-f85a-418f-98a2-b4c6bba9eeef'),
       (35, '1233567890', '1234557890', 50.00, 'SUCCESS', '8dfcdfd5-5f95-4eea-901d-5b7345f0fb31'),
       (36, '1234467890', '1233567890', 50.00, 'SUCCESS', 'df67c73a-6e89-4c41-b96a-08e17a5811c5'),
       (37, '1234557890', '1234467890', 50.00, 'SUCCESS', '25fe0d17-f58b-4a1d-a867-1155431b4915'),
       (38, '1233567890', '1234467890', 50.00, 'SUCCESS', 'ddfd29ef-a31c-4514-a6aa-81cf75be95ca'),
       (39, '1234467890', '1234557890', 50.00, 'SUCCESS', '6d0baf3e-498d-4f27-bfa9-45d610cb301f'),
       (40, '1234557890', '1233567890', 50.00, 'SUCCESS', 'a916114f-9e6d-4b20-8f83-b1b8383bde1a'),
       (41, '1233567890', '1234557890', 50.00, 'SUCCESS', '188431b8-0bf1-4ca7-82a6-b4ee75fedc60');
SELECT SETVAL('fund_transfer_id_seq', (SELECT MAX(id) FROM fund_transfer));






