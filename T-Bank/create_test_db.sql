use
t_bank_test_db;

DROP TABLE IF EXISTS account_loans;
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
    account_id    int auto_increment,
    first_name    varchar(100),
    last_name     varchar(100),
    personal_id   char(11),
    user_name     varchar(100),
    user_password varchar(100),
    birth_date    date,
    primary key (account_id)
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
    account_card_id int auto_increment,
    card_identifier char(11),
    account_id      int,
    card_type_id    int,
    card_name       varchar(30),
    gel_balance     double,
    usd_balance     double,
    euro_balance    double,
    primary key (account_card_id),
    foreign key (account_id) references accounts (account_id),
    foreign key (card_type_id) references card_types (card_type_id),
    unique (card_identifier)
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
    event_id        int auto_increment,
    event_name      varchar(100),
    account_id      int,
    card_identifier char(11),
    event_desc      varchar(400),
    target          double,
    done            double,
    active_event    boolean,
    currency_id     int,
    primary key (event_id),
    foreign key (account_id) references accounts (account_id),
    foreign key (card_identifier)
        references account_cards (card_identifier),
    foreign key (currency_id)
        references currency_exchange (currency_id)
);

CREATE TABLE account_loans
(
    account_loan_id  int auto_increment,
    account_id       int,
    card_identifier  char(11),
    start_money      double,
    percent          double,
    periods          int,
    full_money       double,
    monthly_payment  double,
    start_date       timestamp,
    last_update_date timestamp,
    end_date         timestamp,
    active_loan      boolean,
    payed_amount     double,
    to_pay_amount    double,
    primary key (account_loan_id),
    foreign key (account_id)
        references accounts (account_id),
    foreign key (card_identifier)
        references account_cards (card_identifier)
);