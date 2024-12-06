INSERT INTO sleeper (id, name)
VALUES (1, 'Sam'),
       (2, 'Kassidy'),
       (3, 'Goose'),
       (4, 'Mr. Insomniac');

-- Sam
INSERT INTO sleep_session (id, sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (11, 1, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (12, 1, '2024-12-02', '23:30:00', '06:00:00', 'BAD'),
       (13, 1, '2024-12-03', '22:00:00', '08:30:00', 'GOOD');

-- Kassidy
INSERT INTO sleep_session (id, sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (21, 2, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (22, 2, '2024-12-02', '23:30:00', '06:00:00', 'BAD'),
       (23, 2, '2024-12-03', '22:00:00', '08:30:00', 'GOOD');
-- Goose
INSERT INTO sleep_session (id, sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (31, 3, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (32, 3, '2024-12-02', '23:30:00', '06:00:00', 'BAD'),
       (33, 3, '2024-12-03', '22:00:00', '08:30:00', 'GOOD');

-- Mr. Insomniac
INSERT INTO sleep_session (id, sleeper_id, sleep_date, sleep_start, sleep_end, wake_up_feeling)
VALUES (41, 4, '2024-12-01', '23:00:00', '08:00:00', 'OK'),
       (42, 4, '2024-12-03', '01:30:00', '06:00:00', 'BAD'),
       (43, 4, '2024-12-04', '00:30:00', '08:30:00', 'GOOD'),
       (44, 4, '2024-12-04', '22:30:00', '07:30:00', 'BAD');


