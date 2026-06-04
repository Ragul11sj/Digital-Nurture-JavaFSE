CREATE DATABASE IF NOT EXISTS community_event_management;
USE community_event_management;

CREATE TABLE users (
    user_id     INT PRIMARY KEY,
    full_name   VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    phone       VARCHAR(20),
    age         INT NOT NULL CHECK (age >= 18 AND age <= 120),
    city        VARCHAR(100),
    joined_date DATE NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id        INT PRIMARY KEY,
    event_name      VARCHAR(200) NOT NULL,
    category        ENUM('CULTURAL','TECHNOLOGY','WELLNESS','FOOD','MUSIC','SPORTS','OTHER') NOT NULL,
    event_date      DATE NOT NULL,
    start_time      TIME NOT NULL,
    location        VARCHAR(255) NOT NULL,
    total_seats     INT NOT NULL CHECK (total_seats > 0),
    available_seats INT NOT NULL CHECK (available_seats >= 0),
    fee             DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (fee >= 0),
    organizer       VARCHAR(150) NOT NULL,
    status          ENUM('UPCOMING','ONGOING','COMPLETED','CANCELLED') NOT NULL DEFAULT 'UPCOMING',
    description     TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE registrations (
    registration_id   INT PRIMARY KEY,
    user_id           INT NOT NULL,
    event_id          INT NOT NULL,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status    ENUM('PENDING','PAID','REFUNDED') NOT NULL DEFAULT 'PENDING',
    amount_paid       DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (amount_paid >= 0),
    attendance_status ENUM('REGISTERED','ATTENDED','ABSENT','CANCELLED') NOT NULL DEFAULT 'REGISTERED',
    UNIQUE KEY unique_registration (user_id, event_id),
    FOREIGN KEY (user_id)  REFERENCES users(user_id)  ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE RESTRICT
);

CREATE INDEX idx_users_email    ON users(email);
CREATE INDEX idx_users_city     ON users(city);
CREATE INDEX idx_events_date    ON events(event_date);
CREATE INDEX idx_events_status  ON events(status);
CREATE INDEX idx_events_category ON events(category);
CREATE INDEX idx_reg_user       ON registrations(user_id);
CREATE INDEX idx_reg_event      ON registrations(event_id);

INSERT INTO users (user_id, full_name, email, phone, age, city, joined_date) VALUES
(1, 'Ragul SJ',       'sjragul555@gmail.com',   '9840000001', 24, 'Chennai',    CURDATE()),
(2, 'Priya Krishnan', 'priya.k@example.com',     '9840000002', 29, 'Chennai',    CURDATE()),
(3, 'Arjun Mehta',    'arjun.m@example.com',     '9840000003', 32, 'Coimbatore', CURDATE()),
(4, 'Sneha Ramesh',   'sneha.r@example.com',     '9840000004', 26, 'Chennai',    CURDATE()),
(5, 'Vikram Nair',    'vikram.n@example.com',     '9840000005', 35, 'Madurai',    CURDATE()),
(6, 'Divya Suresh',   'divya.s@example.com',     '9840000006', 22, 'Chennai',    CURDATE()),
(7, 'Karthik Babu',   'karthik.b@example.com',   '9840000007', 27, 'Trichy',     CURDATE()),
(8, 'Anitha Selvan',  'anitha.s@example.com',    '9840000008', 31, 'Chennai',    CURDATE());

INSERT INTO events (event_id, event_name, category, event_date, start_time, location, total_seats, available_seats, fee, organizer, status, description) VALUES
(1, 'Marina Heritage Walk',       'CULTURAL',    DATE_ADD(CURDATE(), INTERVAL 20 DAY), '06:30:00', 'Marina Beach, Chennai',   100, 100, 0.00,   'Chennai Heritage Trust',    'UPCOMING', 'Guided heritage walk along Marina Beach.'),
(2, 'AI for Everyone Workshop',   'TECHNOLOGY',  DATE_ADD(CURDATE(), INTERVAL 33 DAY), '10:00:00', 'TIDEL Park, OMR',          150, 150, 499.00, 'TechChennai Community',     'UPCOMING', 'Hands-on intro to AI concepts.'),
(3, 'Sunrise Yoga Session',       'WELLNESS',    DATE_ADD(CURDATE(), INTERVAL 26 DAY), '05:45:00', 'Nandanam Park, Adyar',     60,  60, 150.00, 'Wellness Chennai',          'UPCOMING', 'Outdoor yoga for all skill levels.'),
(4, 'South Indian Food Carnival', 'FOOD',        DATE_ADD(CURDATE(), INTERVAL 40 DAY), '11:00:00', 'Express Avenue Mall',      400, 400, 299.00, 'Flavours of South India',  'UPCOMING', '50+ stalls of authentic South Indian cuisine.'),
(5, 'Carnatic Fusion Night',      'MUSIC',       DATE_ADD(CURDATE(), INTERVAL 47 DAY), '19:00:00', 'Music Academy, TTK Road',  300, 300, 200.00, 'Raaga Collective',          'UPCOMING', 'Classical ragas with jazz and world music.');

INSERT INTO registrations (registration_id, user_id, event_id, payment_status, amount_paid, attendance_status) VALUES
(1, 1, 1, 'PAID',    0.00,   'REGISTERED'),
(2, 2, 1, 'PAID',    0.00,   'REGISTERED'),
(3, 3, 2, 'PAID',    499.00, 'REGISTERED'),
(4, 4, 3, 'PAID',    150.00, 'REGISTERED'),
(5, 5, 4, 'PENDING', 0.00,   'REGISTERED'),
(6, 6, 5, 'PAID',    200.00, 'REGISTERED');

SELECT u.full_name, u.email, u.city FROM users u ORDER BY u.full_name;

SELECT e.event_name, e.category, e.event_date, e.available_seats, e.fee
FROM events e
WHERE e.status = 'UPCOMING'
ORDER BY e.event_date;

SELECT u.full_name, e.event_name, r.payment_status, r.attendance_status
FROM registrations r
INNER JOIN users  u ON r.user_id  = u.user_id
INNER JOIN events e ON r.event_id = e.event_id
ORDER BY u.full_name;

SELECT e.category, COUNT(e.event_id) AS event_count, AVG(e.fee) AS avg_fee
FROM events e
GROUP BY e.category
ORDER BY event_count DESC;

SELECT u.full_name, COUNT(r.registration_id) AS total_events
FROM users u
LEFT JOIN registrations r ON u.user_id = r.user_id
GROUP BY u.user_id, u.full_name
ORDER BY total_events DESC;

SELECT e.event_name,
    (e.total_seats - e.available_seats) AS registered,
    e.total_seats,
    ROUND(((e.total_seats - e.available_seats) / e.total_seats) * 100, 1) AS fill_pct
FROM events e
ORDER BY fill_pct DESC;

CREATE OR REPLACE VIEW vw_event_summary AS
SELECT e.event_id, e.event_name, e.category, e.event_date, e.location,
       e.total_seats, e.available_seats, e.fee, e.organizer, e.status,
       COUNT(r.registration_id) AS registrations
FROM events e
LEFT JOIN registrations r ON e.event_id = r.event_id
GROUP BY e.event_id;

CREATE OR REPLACE VIEW vw_registration_report AS
SELECT r.registration_id, u.full_name, u.email, e.event_name,
       e.category, e.event_date, r.registration_time,
       r.payment_status, r.amount_paid, r.attendance_status
FROM registrations r
JOIN users  u ON r.user_id  = u.user_id
JOIN events e ON r.event_id = e.event_id;
