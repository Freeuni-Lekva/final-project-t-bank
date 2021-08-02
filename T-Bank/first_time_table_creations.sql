use t_bank_db;

DROP TABLE IF EXISTS account_cards;
DROP TABLE IF EXISTS card_types;
DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts(
                         account_id int auto_increment,
                         first_name varchar(100),
                         last_name  varchar(100),
                         personal_id char(11),
                         user_name  varchar(100),
                         user_password varchar(100),
                         birth_date date,
                         primary key(account_id)
);

CREATE TABLE card_types(
                           card_type_id int auto_increment,
                           card_type varchar(20),
                           card_prefix char(4),
                           card_desc varchar(100),
                           card_limit int,
                           primary key(card_type_id),
                           unique(card_prefix)
);

INSERT INTO card_types	(card_type_id, card_type, card_prefix,
                           card_desc, card_limit)
values(1, 'MasterCard', 'MTSC','standard card', 20000);
INSERT INTO card_types	(card_type_id, card_type, card_prefix,
                           card_desc, card_limit)
values(2, 'VISA', 'VISA','', 50000);
INSERT INTO card_types	(card_type_id, card_type, card_prefix,
                           card_desc, card_limit)
values(3, 'AMEX', 'AMEX','Super Rich Card', 1000000);

CREATE TABLE account_cards(
                              account_card_id int auto_increment,
                              card_idenfier char(4),
                              account_id int,
                              card_type_id int,
                              card_name varchar(30),
                              gel_balance int,
                              usd_balance int,
                              euro_balance int,
                              primary key(account_card_id),
                              foreign key(account_id) references accounts(account_id),
                              foreign key(card_type_id) references card_types(card_type_id),
                              unique(card_identifier)
);

