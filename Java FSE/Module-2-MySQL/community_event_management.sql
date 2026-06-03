CREATE DATABASE IF NOT EXISTS community_event_management;
USE community_event_management;

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venues (
    venue_id INT AUTO_INCREMENT PRIMARY KEY,
    venue_name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0),
    contact_number VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(15),
    age INT CHECK (age >= 18),
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    city VARCHAR(100),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(200) NOT NULL,
    category_id INT NOT NULL,
    venue_id INT NOT NULL,
    organizer_id INT NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    fee DECIMAL(8,2) DEFAULT 0.00 CHECK (fee >= 0),
    total_seats INT NOT NULL CHECK (total_seats > 0),
    available_seats INT NOT NULL CHECK (available_seats >= 0),
    status ENUM('Upcoming', 'Ongoing', 'Completed', 'Cancelled') DEFAULT 'Upcoming',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (venue_id) REFERENCES venues(venue_id),
    FOREIGN KEY (organizer_id) REFERENCES users(user_id)
);

CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('Pending', 'Paid', 'Refunded') DEFAULT 'Pending',
    amount_paid DECIMAL(8,2) DEFAULT 0.00 CHECK (amount_paid >= 0),
    attendance_status ENUM('Registered', 'Attended', 'Absent') DEFAULT 'Registered',
    UNIQUE KEY unique_registration (user_id, event_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

INSERT INTO categories (category_name, description) VALUES
('Cultural', 'Events celebrating arts, heritage, and local traditions'),
('Technology', 'Workshops, hackathons, and tech conferences'),
('Wellness', 'Yoga, meditation, and health-focused sessions'),
('Food & Cuisine', 'Food festivals, cook-offs, and culinary workshops'),
('Music & Arts', 'Concerts, open mics, and art exhibitions');

INSERT INTO venues (venue_name, address, city, state, capacity, contact_number) VALUES
('Marina Convention Centre', '12 Beach Road, Marina', 'Chennai', 'Tamil Nadu', 500, '044-23456789'),
('TIDEL Park Auditorium', 'Old Mahabalipuram Road', 'Chennai', 'Tamil Nadu', 300, '044-22334455'),
('Nandanam Cultural Hall', '5 Nandanam Extension', 'Chennai', 'Tamil Nadu', 200, '044-24455667'),
('Express Avenue Arena', 'Whites Road, Royapettah', 'Chennai', 'Tamil Nadu', 800, '044-66778899'),
('Music Academy', '168 TTK Road, Alwarpet', 'Chennai', 'Tamil Nadu', 400, '044-28112231');

INSERT INTO users (full_name, email, phone, age, gender, city) VALUES
('Ragul SJ', 'sjragul555@gmail.com', '9840000001', 24, 'Male', 'Chennai'),
('Priya Krishnan', 'priya.krishnan@example.com', '9840000002', 29, 'Female', 'Chennai'),
('Arjun Mehta', 'arjun.mehta@example.com', '9840000003', 32, 'Male', 'Coimbatore'),
('Sneha Ramesh', 'sneha.ramesh@example.com', '9840000004', 26, 'Female', 'Chennai'),
('Vikram Nair', 'vikram.nair@example.com', '9840000005', 35, 'Male', 'Madurai'),
('Divya Suresh', 'divya.suresh@example.com', '9840000006', 22, 'Female', 'Chennai'),
('Karthik Babu', 'karthik.babu@example.com', '9840000007', 27, 'Male', 'Trichy'),
('Anitha Selvan', 'anitha.selvan@example.com', '9840000008', 31, 'Female', 'Chennai'),
('Rohit Sharma', 'rohit.sharma@example.com', '9840000009', 23, 'Male', 'Chennai'),
('Meena Pandian', 'meena.pandian@example.com', '9840000010', 28, 'Female', 'Salem'),
('Suresh Kumar', 'suresh.kumar@example.com', '9840000011', 40, 'Male', 'Chennai'),
('Lakshmi Rajan', 'lakshmi.rajan@example.com', '9840000012', 33, 'Female', 'Pondicherry'),
('Deepak Iyer', 'deepak.iyer@example.com', '9840000013', 25, 'Male', 'Chennai'),
('Nandhini Gopal', 'nandhini.gopal@example.com', '9840000014', 30, 'Female', 'Chennai'),
('Praveen Raj', 'praveen.raj@example.com', '9840000015', 21, 'Male', 'Erode'),
('Saranya Vel', 'saranya.vel@example.com', '9840000016', 27, 'Female', 'Chennai'),
('Murugan Das', 'murugan.das@example.com', '9840000017', 38, 'Male', 'Chennai'),
('Kavitha Nair', 'kavitha.nair@example.com', '9840000018', 29, 'Female', 'Vellore'),
('Arun Balaji', 'arun.balaji@example.com', '9840000019', 24, 'Male', 'Chennai'),
('Janani Mohan', 'janani.mohan@example.com', '9840000020', 26, 'Female', 'Chennai');

INSERT INTO events (event_name, category_id, venue_id, organizer_id, event_date, start_time, end_time, fee, total_seats, available_seats, status) VALUES
('Marina Heritage Walk', 1, 1, 1, '2026-06-22', '06:30:00', '09:00:00', 0.00, 100, 42, 'Upcoming'),
('AI for Everyone Workshop', 2, 2, 3, '2026-07-05', '10:00:00', '17:00:00', 499.00, 150, 7, 'Upcoming'),
('Sunrise Yoga Session', 3, 3, 2, '2026-06-28', '05:45:00', '07:30:00', 150.00, 60, 18, 'Upcoming'),
('South Indian Food Carnival', 4, 4, 5, '2026-07-12', '11:00:00', '20:00:00', 299.00, 400, 85, 'Upcoming'),
('Carnatic Fusion Night', 5, 5, 4, '2026-07-19', '19:00:00', '22:00:00', 200.00, 300, 11, 'Upcoming'),
('Street Art Mural Festival', 1, 1, 7, '2026-07-26', '09:00:00', '18:00:00', 0.00, 200, 60, 'Upcoming'),
('Python Bootcamp', 2, 2, 1, '2026-08-02', '09:00:00', '17:00:00', 599.00, 80, 20, 'Upcoming'),
('Zumba & Dance Fitness', 3, 3, 6, '2026-08-09', '07:00:00', '08:30:00', 100.00, 50, 30, 'Upcoming'),
('Baking Masterclass', 4, 3, 2, '2026-08-15', '10:00:00', '14:00:00', 350.00, 40, 12, 'Upcoming'),
('Open Mic Night', 5, 5, 4, '2026-08-22', '18:00:00', '21:00:00', 100.00, 250, 90, 'Upcoming'),
('Kalaignar Cultural Fest', 1, 4, 11, '2026-09-01', '10:00:00', '20:00:00', 0.00, 500, 200, 'Upcoming'),
('Data Science Conclave', 2, 2, 13, '2026-09-10', '09:00:00', '18:00:00', 799.00, 200, 50, 'Upcoming'),
('Mindfulness Retreat', 3, 3, 14, '2026-09-14', '08:00:00', '16:00:00', 250.00, 30, 5, 'Upcoming'),
('Street Food Throwdown', 4, 4, 17, '2026-09-20', '12:00:00', '21:00:00', 199.00, 350, 150, 'Upcoming'),
('Tamil Indie Music Fest', 5, 5, 19, '2026-09-27', '17:00:00', '22:00:00', 299.00, 400, 100, 'Upcoming');

INSERT INTO registrations (user_id, event_id, payment_status, amount_paid, attendance_status) VALUES
(1, 1, 'Paid', 0.00, 'Registered'),
(2, 1, 'Paid', 0.00, 'Registered'),
(3, 2, 'Paid', 499.00, 'Registered'),
(4, 2, 'Paid', 499.00, 'Registered'),
(5, 2, 'Paid', 499.00, 'Registered'),
(6, 3, 'Paid', 150.00, 'Registered'),
(7, 3, 'Paid', 150.00, 'Registered'),
(8, 4, 'Paid', 299.00, 'Registered'),
(9, 4, 'Paid', 299.00, 'Registered'),
(10, 4, 'Paid', 299.00, 'Registered'),
(11, 5, 'Paid', 200.00, 'Registered'),
(12, 5, 'Paid', 200.00, 'Registered'),
(13, 6, 'Paid', 0.00, 'Registered'),
(14, 6, 'Paid', 0.00, 'Registered'),
(15, 7, 'Paid', 599.00, 'Registered'),
(16, 7, 'Paid', 599.00, 'Registered'),
(17, 8, 'Paid', 100.00, 'Registered'),
(18, 8, 'Paid', 100.00, 'Registered'),
(19, 9, 'Paid', 350.00, 'Registered'),
(20, 9, 'Paid', 350.00, 'Registered'),
(1, 10, 'Paid', 100.00, 'Registered'),
(2, 11, 'Paid', 0.00, 'Registered'),
(3, 11, 'Paid', 0.00, 'Registered'),
(4, 12, 'Paid', 799.00, 'Registered'),
(5, 12, 'Paid', 799.00, 'Registered'),
(6, 13, 'Paid', 250.00, 'Registered'),
(7, 14, 'Paid', 199.00, 'Registered'),
(8, 14, 'Paid', 199.00, 'Registered'),
(9, 15, 'Paid', 299.00, 'Registered'),
(10, 15, 'Paid', 299.00, 'Registered'),
(11, 1, 'Pending', 0.00, 'Registered'),
(12, 3, 'Paid', 150.00, 'Registered'),
(13, 5, 'Refunded', 0.00, 'Absent'),
(14, 7, 'Paid', 599.00, 'Registered'),
(15, 10, 'Paid', 100.00, 'Registered');

SELECT * FROM users;

SELECT * FROM events;

SELECT * FROM registrations;

SELECT * FROM categories;

SELECT * FROM venues;

SELECT full_name, email, city FROM users;

SELECT event_name, event_date, fee, status FROM events;

SELECT full_name, email, age FROM users WHERE city = 'Chennai';

SELECT event_name, event_date, fee FROM events WHERE fee > 200.00;

SELECT * FROM events ORDER BY event_date ASC;

SELECT * FROM users ORDER BY full_name DESC;

SELECT * FROM users WHERE city = 'Chennai' AND gender = 'Female';

SELECT * FROM events WHERE status = 'Upcoming' OR fee = 0.00;

SELECT * FROM users WHERE full_name LIKE 'A%';

SELECT * FROM events WHERE event_name LIKE '%Fest%';

SELECT * FROM events WHERE fee BETWEEN 100.00 AND 500.00;

SELECT * FROM users WHERE age BETWEEN 22 AND 30;

SELECT * FROM events WHERE category_id IN (1, 3, 5);

SELECT * FROM users WHERE city IN ('Chennai', 'Coimbatore', 'Madurai');

SELECT * FROM events ORDER BY fee DESC;

SELECT * FROM registrations ORDER BY registration_date ASC;

SELECT COUNT(*) AS total_users FROM users;

SELECT COUNT(*) AS total_events FROM events;

SELECT SUM(amount_paid) AS total_revenue FROM registrations;

SELECT AVG(fee) AS average_event_fee FROM events;

SELECT MAX(fee) AS highest_fee FROM events;

SELECT MIN(fee) AS lowest_fee FROM events;

SELECT MAX(age) AS oldest_user, MIN(age) AS youngest_user FROM users;

SELECT AVG(age) AS average_age FROM users;

SELECT category_id, COUNT(*) AS total_events FROM events GROUP BY category_id;

SELECT event_id, COUNT(*) AS total_registrations, SUM(amount_paid) AS total_revenue FROM registrations GROUP BY event_id;

SELECT city, COUNT(*) AS total_users FROM users GROUP BY city ORDER BY total_users DESC;

SELECT payment_status, COUNT(*) AS count FROM registrations GROUP BY payment_status;

SELECT event_id, COUNT(*) AS total_registrations FROM registrations GROUP BY event_id HAVING COUNT(*) > 2;

SELECT category_id, AVG(fee) AS avg_fee FROM events GROUP BY category_id HAVING AVG(fee) > 100;

SELECT city, COUNT(*) AS user_count FROM users GROUP BY city HAVING user_count >= 2;

SELECT u.full_name, u.email, e.event_name, e.event_date, r.payment_status, r.amount_paid
FROM registrations r
INNER JOIN users u ON r.user_id = u.user_id
INNER JOIN events e ON r.event_id = e.event_id;

SELECT u.full_name, u.email, COUNT(r.registration_id) AS events_registered
FROM users u
LEFT JOIN registrations r ON u.user_id = r.user_id
GROUP BY u.user_id, u.full_name, u.email
ORDER BY events_registered DESC;

SELECT e.event_name, e.event_date, COUNT(r.registration_id) AS total_registrations
FROM events e
LEFT JOIN registrations r ON e.event_id = r.event_id
GROUP BY e.event_id, e.event_name, e.event_date
ORDER BY total_registrations DESC;

SELECT e.event_name, e.event_date, e.fee, c.category_name, v.venue_name, v.city
FROM events e
INNER JOIN categories c ON e.category_id = c.category_id
INNER JOIN venues v ON e.venue_id = v.venue_id;

SELECT e.event_name, u.full_name AS organizer_name, u.email AS organizer_email
FROM events e
RIGHT JOIN users u ON e.organizer_id = u.user_id
WHERE e.event_id IS NOT NULL;

SELECT u1.full_name AS user1, u2.full_name AS user2, u1.city
FROM users u1
INNER JOIN users u2 ON u1.city = u2.city AND u1.user_id < u2.user_id
ORDER BY u1.city;

SELECT full_name, email FROM users
WHERE user_id IN (SELECT DISTINCT user_id FROM registrations);

SELECT event_name, fee FROM events
WHERE fee > (SELECT AVG(fee) FROM events);

SELECT event_name, total_seats, available_seats
FROM events
WHERE available_seats = (SELECT MIN(available_seats) FROM events);

SELECT u.full_name, u.email,
    (SELECT COUNT(*) FROM registrations r WHERE r.user_id = u.user_id) AS total_registrations
FROM users u
ORDER BY total_registrations DESC;

SELECT e.event_name,
    (SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id) AS registration_count
FROM events e
ORDER BY registration_count DESC;

SELECT u.full_name FROM users u
WHERE EXISTS (
    SELECT 1 FROM registrations r WHERE r.user_id = u.user_id AND r.payment_status = 'Paid'
);

INSERT INTO users (full_name, email, phone, age, gender, city)
VALUES ('Tharun Balaji', 'tharun.balaji@example.com', '9840000021', 23, 'Male', 'Chennai');

INSERT INTO registrations (user_id, event_id, payment_status, amount_paid, attendance_status)
VALUES (21, 1, 'Paid', 0.00, 'Registered');

UPDATE events SET available_seats = available_seats - 1 WHERE event_id = 1;

UPDATE registrations SET attendance_status = 'Attended' WHERE user_id = 1 AND event_id = 1;

UPDATE users SET city = 'Chennai' WHERE city = 'Erode' AND user_id = 15;

DELETE FROM registrations WHERE payment_status = 'Refunded' AND attendance_status = 'Absent';

ALTER TABLE users ADD COLUMN profile_image_url VARCHAR(255);

ALTER TABLE events ADD COLUMN description TEXT;

ALTER TABLE venues MODIFY COLUMN contact_number VARCHAR(20);

ALTER TABLE users ADD COLUMN is_active TINYINT(1) DEFAULT 1;

ALTER TABLE users DROP COLUMN profile_image_url;

CREATE INDEX idx_users_city ON users(city);

CREATE INDEX idx_events_date ON events(event_date);

CREATE INDEX idx_events_status ON events(status);

CREATE INDEX idx_registrations_user ON registrations(user_id);

CREATE INDEX idx_registrations_event ON registrations(event_id);

SHOW INDEX FROM users;

SHOW INDEX FROM events;

SHOW INDEX FROM registrations;

CREATE VIEW vw_event_summary AS
SELECT
    e.event_id,
    e.event_name,
    c.category_name,
    v.venue_name,
    v.city,
    u.full_name AS organizer,
    e.event_date,
    e.start_time,
    e.end_time,
    e.fee,
    e.total_seats,
    e.available_seats,
    (e.total_seats - e.available_seats) AS seats_filled,
    e.status
FROM events e
INNER JOIN categories c ON e.category_id = c.category_id
INNER JOIN venues v ON e.venue_id = v.venue_id
INNER JOIN users u ON e.organizer_id = u.user_id;

CREATE VIEW vw_registration_report AS
SELECT
    r.registration_id,
    u.full_name AS participant_name,
    u.email AS participant_email,
    u.city AS participant_city,
    e.event_name,
    c.category_name,
    e.event_date,
    r.registration_date,
    r.payment_status,
    r.amount_paid,
    r.attendance_status
FROM registrations r
INNER JOIN users u ON r.user_id = u.user_id
INNER JOIN events e ON r.event_id = e.event_id
INNER JOIN categories c ON e.category_id = c.category_id;

SELECT * FROM vw_event_summary;

SELECT * FROM vw_registration_report;

SELECT e.event_name, c.category_name, COUNT(r.registration_id) AS total_registrations, SUM(r.amount_paid) AS revenue
FROM events e
INNER JOIN categories c ON e.category_id = c.category_id
LEFT JOIN registrations r ON e.event_id = r.event_id
GROUP BY e.event_id, e.event_name, c.category_name
ORDER BY total_registrations DESC
LIMIT 5;

SELECT u.full_name, u.email, u.city, COUNT(r.registration_id) AS total_events, SUM(r.amount_paid) AS total_spent
FROM users u
INNER JOIN registrations r ON u.user_id = r.user_id
GROUP BY u.user_id, u.full_name, u.email, u.city
ORDER BY total_events DESC
LIMIT 10;

SELECT e.event_id, e.event_name, c.category_name, v.venue_name, e.event_date, e.fee, e.available_seats
FROM events e
INNER JOIN categories c ON e.category_id = c.category_id
INNER JOIN venues v ON e.venue_id = v.venue_id
WHERE e.event_date >= CURDATE() AND e.status = 'Upcoming'
ORDER BY e.event_date ASC;

SELECT c.category_name,
    COUNT(e.event_id) AS total_events,
    AVG(e.fee) AS avg_fee,
    MAX(e.fee) AS max_fee,
    MIN(e.fee) AS min_fee,
    SUM(e.total_seats) AS total_capacity
FROM categories c
LEFT JOIN events e ON c.category_id = e.category_id
GROUP BY c.category_id, c.category_name
ORDER BY total_events DESC;

SELECT v.venue_name, v.city, v.capacity,
    COUNT(e.event_id) AS events_hosted,
    SUM(e.total_seats - e.available_seats) AS total_attendees
FROM venues v
LEFT JOIN events e ON v.venue_id = e.venue_id
GROUP BY v.venue_id, v.venue_name, v.city, v.capacity
ORDER BY events_hosted DESC;

SELECT gender, COUNT(*) AS count, AVG(age) AS avg_age
FROM users
GROUP BY gender;

SELECT payment_status, COUNT(*) AS registrations, SUM(amount_paid) AS total_amount
FROM registrations
GROUP BY payment_status;
