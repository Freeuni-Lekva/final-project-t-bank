use
t_bank_test_db;

DROP TABLE IF EXISTS crowd_funding_events;
DROP TABLE IF EXISTS account_logs;
DROP TABLE IF EXISTS account_cards;
DROP TABLE IF EXISTS card_types;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS currency_exchange;
DROP TABLE IF EXISTS transaction_types;
DROP TABLE IF EXISTS testing_seeds;

CREATE TABLE accounts
(
    account_id    INT AUTO_INCREMENT,
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    personal_id   CHAR(11),
    user_name     VARCHAR(100),
    user_password VARCHAR(100),
    birth_date    DATE,
    PRIMARY KEY (account_id)
);

CREATE TABLE card_types
(
    card_type_id int auto_increment,
    card_type    varchar(20),
    card_prefix  char(4),
    card_desc    varchar(100),
    card_limit   int,
    primary key (card_type_id),
    unique (card_prefix)
);

INSERT INTO card_types (card_type_id, card_type, card_prefix,
                        card_desc, card_limit)
values (1, 'MasterCard', 'MTSC', 'standard card', 20000);
INSERT INTO card_types (card_type_id, card_type, card_prefix,
                        card_desc, card_limit)
values (2, 'VISA', 'VISA', '', 50000);
INSERT INTO card_types (card_type_id, card_type, card_prefix,
                        card_desc, card_limit)
values (3, 'AMEX', 'AMEX', 'Super Rich Card', 1000000);

CREATE TABLE account_cards
(
    account_card_id INT AUTO_INCREMENT,
    card_identifier CHAR(11),
    account_id      INT,
    card_type_id    INT,
    card_name       VARCHAR(30),
    gel_balance     DOUBLE,
    usd_balance     DOUBLE,
    euro_balance    DOUBLE,
    PRIMARY KEY (account_card_id),
    FOREIGN KEY (account_id)
        REFERENCES accounts (account_id),
    FOREIGN KEY (card_type_id)
        REFERENCES card_types (card_type_id),
    UNIQUE (card_identifier)
);

CREATE TABLE currency_exchange
(
    currency_id   int auto_increment,
    currency_name varchar(50),
    call_price    double,
    bid_price     double,
    primary key (currency_id),
    unique (currency_name)
);

INSERT INTO currency_exchange (currency_id, currency_name, call_price,
                               bid_price)
values (1, 'GEL', 1, 1);
INSERT INTO currency_exchange (currency_id, currency_name, call_price,
                               bid_price)
values (2, 'USD', 3.13, 3.01);
INSERT INTO currency_exchange (currency_id, currency_name, call_price,
                               bid_price)
values (3, 'EURO', 4.22, 4.08);

CREATE TABLE transaction_types
(
    transaction_type_id   int auto_increment,
    transaction_type_name varchar(100),
    transaction_type_desc varchar(100),
    primary key (transaction_type_id)
);

INSERT INTO transaction_types(transaction_type_id,
                              transaction_type_name,
                              transaction_type_desc)
values (1, 'transfer', 'between different accounts, or
								same ones');
INSERT INTO transaction_types(transaction_type_id,
                              transaction_type_name,
                              transaction_type_desc)
values (2, 'currency_exchange', 'between different accounts, or
								same ones');

CREATE TABLE account_logs
(
    account_log_id           int auto_increment,
    sender_account_id        int,
    receiver_account_id      int,
    sender_card_identifier   char(11),
    receiver_card_identifier char(11),
    transaction_type_id      int,
    log_date                 date,
    currency_id              int,
    amount                   double,
    primary key (account_log_id),
    foreign key (sender_card_identifier)
        references account_cards (card_identifier),
    foreign key (receiver_card_identifier)
        references account_cards (card_identifier),
    foreign key (sender_account_id)
        references accounts (account_id),
    foreign key (receiver_account_id)
        references accounts (account_id),
    foreign key (currency_id)
        references currency_exchange (currency_id)

);

CREATE TABLE testing_seeds
(
    test_seed int auto_increment,
    primary key (test_seed)
);

CREATE TABLE crowd_funding_events
(
    event_id        INT AUTO_INCREMENT,
    event_name      VARCHAR(100),
    account_id      INT,
    card_identifier CHAR(11),
    event_desc      VARCHAR(400),
    target          DOUBLE,
    done            DOUBLE,
    active_event    BOOLEAN,
    currency_id     INT,
    PRIMARY KEY (event_id),
    FOREIGN KEY (account_id)
        REFERENCES accounts (account_id),
    FOREIGN KEY (card_identifier)
        REFERENCES account_cards (card_identifier),
    FOREIGN KEY (currency_id)
        REFERENCES currency_exchange (currency_id)
);

-- insert into t_bank_test_db.accounts (first_name, last_name, personal_id, user_name, user_password, birth_date)
-- values ('testName1', 'testSurname1', 'testID1', 'testUsername1', 'testPass1', null);
-- insert into t_bank_test_db.accounts (first_name, last_name, personal_id, user_name, user_password, birth_date)
-- values ('testName2', 'testSurname2', 'testID2', 'testUsername2', 'testPass2', null);
--
-- insert into t_bank_test_db.account_cards (account_id, card_identifier,card_type_id, card_name,gel_balance, usd_balance, euro_balance) values (1, 'CardID1', 1, 'testCard1', 100000, 10000, 10000);
-- insert into t_bank_test_db.account_cards (account_id, card_identifier,card_type_id, card_name,gel_balance, usd_balance, euro_balance) values (2, 'CardID2', 1, 'testCard2', 100000, 10000, 10000);
