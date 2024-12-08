-- create custom type for wake up feeling
CREATE TYPE feeling AS ENUM ('BAD', 'OK', 'GOOD');
-- allow casting from String to custom type
CREATE CAST (character varying as feeling) WITH INOUT AS IMPLICIT;

-- user is a keyword so I named it sleeper. Not a great table name, but it's what I came up with :)
CREATE TABLE sleeper(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE sleep_session(
    id SERIAL PRIMARY KEY,
    sleeper_id INT NOT NULL,
    sleep_date DATE NOT NULL,
    sleep_start TIME NOT NULL,
    sleep_end TIME NOT NULL,
    wake_up_feeling feeling NOT NULL,
    CONSTRAINT sleeper_sleep_session_fkey FOREIGN KEY (sleeper_id)
        REFERENCES sleeper (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);
