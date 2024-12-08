-- Just adding some test data for postman requests and manual testing
INSERT INTO sleeper (name)
VALUES ('Sam'),
       ('Kassidy'),
       ('Goose'),
       ('Mr. Insomniac');

-- Sam
INSERT INTO sleep_session (sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (1, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (1, '2024-12-02', '23:30:00', '06:00:00', 'BAD'),
       (1, '2024-12-03', '22:00:00', '08:30:00', 'GOOD');

-- Kassidy
INSERT INTO sleep_session (sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (2, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (2, '2024-12-02', '23:30:00', '06:00:00', 'BAD'),
       (2, '2024-12-03', '22:00:00', '08:30:00', 'GOOD');
-- Goose
INSERT INTO sleep_session (sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (3, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (3, '2024-12-02', '23:30:00', '06:00:00', 'BAD'),
       (3, '2024-12-03', '22:00:00', '08:30:00', 'GOOD');

-- Mr. Insomniac
INSERT INTO sleep_session (sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (4, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (4, '2024-12-03', '01:30:00', '06:00:00', 'BAD'),
       (4, '2024-12-04', '00:30:00', '08:30:00', 'GOOD'),
       (4, '2024-12-04', '22:30:00', '07:30:00', 'BAD');


