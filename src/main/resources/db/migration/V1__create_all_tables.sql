CREATE TABLE users(
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE roles(
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users_roles(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE accounts(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL,
    user_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE categories(
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TYPE transaction_type AS ENUM('INCOME','EXPENSE');

CREATE TABLE transactions(
    id BIGINT PRIMARY KEY,
    description VARCHAR(255),
    date_time TIMESTAMP NOT NULL,
    type transaction_type NOT NULL,

    account_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);