use t_bank_db;

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


