-- Table: Account
CREATE TABLE IF NOT EXISTS account (
                                       id SERIAL PRIMARY KEY,
                                       login VARCHAR(255) NOT NULL UNIQUE,
                                       hashed_password VARCHAR(255) NOT NULL,
                                       info TEXT DEFAULT '',
                                       access_level INT DEFAULT 1
);