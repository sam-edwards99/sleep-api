CREATE TYPE feeling AS ENUM ('BAD', 'OK', 'GOOD');

CREATE TABLE sleeper(
    id INT PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE sleep_session(
    id INT PRIMARY KEY,
    sleeper_id INT NOT NULL,
    sleep_date DATE NOT NULL,
    sleep_start TIME NOT NULL,
    sleep_end TIME NOT NULL,
    wake_up_feeling feeling NOT NULL,
    CONSTRAINT sleeper_sleep_session_fkey FOREIGN KEY (sleeper_id)
        REFERENCES sleeper (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);
