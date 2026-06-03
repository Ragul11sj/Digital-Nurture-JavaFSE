# Community Event Management System

## Project Description

Community Event Management System is a full-featured MySQL relational database project that models the complete lifecycle of local community events — from venue and category setup through user registration, payment tracking, and attendance reporting. The project covers every core ANSI SQL concept required by Module-2 and is implemented entirely in standard MySQL syntax.

---

## Database Name

`community_event_management`

---

## Database Schema

```
categories ──────────────────────────┐
  category_id (PK)                   │
  category_name (UNIQUE, NOT NULL)   │
  description                        │
  created_at                         │
                                     ▼
venues                          events ──────────────────────┐
  venue_id (PK)                   event_id (PK)              │
  venue_name (NOT NULL)           event_name (NOT NULL)      │
  address (NOT NULL)              category_id (FK)           │
  city (NOT NULL)                 venue_id (FK)              │
  state (NOT NULL)                organizer_id (FK → users)  │
  capacity (CHECK > 0)            event_date (NOT NULL)      │
  contact_number                  start_time / end_time      │
  created_at                      fee (CHECK >= 0)           │
      │                           total_seats / available    │
      └──────────────────────────► status                    │
                                  created_at                 │
                                                             ▼
users ◄──────────────────────── registrations ──────────────►
  user_id (PK)                    registration_id (PK)
  full_name (NOT NULL)            user_id (FK)
  email (UNIQUE, NOT NULL)        event_id (FK)
  phone                           registration_date
  age (CHECK >= 18)               payment_status
  gender (ENUM)                   amount_paid (CHECK >= 0)
  city                            attendance_status
  joined_at                       UNIQUE(user_id, event_id)
```

---

## Tables Used

| Table | Purpose | Key Constraints |
|---|---|---|
| `categories` | Stores event categories | PK, UNIQUE(category_name) |
| `venues` | Venue details and capacity | PK, CHECK(capacity > 0) |
| `users` | Community member accounts | PK, UNIQUE(email), CHECK(age >= 18) |
| `events` | Event listings | PK, FK → categories, venues, users |
| `registrations` | User event sign-ups | PK, FK → users & events, UNIQUE(user_id, event_id) |

---

## Sample Data Volume

| Table | Records |
|---|---|
| categories | 5 |
| venues | 5 |
| users | 21 |
| events | 15 |
| registrations | 35 |

---

## Features

- Full relational schema with five normalised tables
- Referential integrity enforced via foreign keys
- Business rules enforced via CHECK constraints (age, fee, capacity, amount_paid)
- Duplicate prevention via UNIQUE constraints (email, user+event pair)
- Realistic Chennai-based community event sample data
- Revenue tracking per event and per user
- Attendance and payment status lifecycle management
- Seat availability tracking on events
- Organiser linkage (users act as event organisers)
- Two pre-built analytical views for reporting
- Five performance indexes on high-query columns

---

## SQL Concepts Covered

### DDL — Data Definition Language
- `CREATE DATABASE`
- `CREATE TABLE`
- `ALTER TABLE` — `ADD COLUMN`, `MODIFY COLUMN`, `DROP COLUMN`
- `CREATE INDEX`
- `CREATE VIEW`

### DML — Data Manipulation Language
- `INSERT INTO`
- `UPDATE ... SET ... WHERE`
- `DELETE FROM ... WHERE`

### DQL — Data Query Language
- `SELECT *` and specific columns
- `WHERE` with single and compound conditions
- `ORDER BY ASC / DESC`
- `LIMIT`

### Filtering Operators
- `AND`, `OR`
- `LIKE` with wildcard patterns
- `BETWEEN`
- `IN`

### Aggregate Functions
- `COUNT`, `SUM`, `AVG`, `MAX`, `MIN`

### Grouping and Having
- `GROUP BY` single and multiple columns
- `HAVING` with aggregate conditions

### Joins
- `INNER JOIN`
- `LEFT JOIN`
- `RIGHT JOIN`
- Self Join (users matched by city)

### Subqueries
- Scalar subquery in `SELECT`
- Subquery in `WHERE` with `IN`
- Correlated subquery with `EXISTS`

### Constraints
- `PRIMARY KEY`
- `FOREIGN KEY`
- `UNIQUE`
- `CHECK`
- `NOT NULL`
- `AUTO_INCREMENT`
- `DEFAULT`

### Views
- `vw_event_summary` — full event detail with category, venue, and organiser
- `vw_registration_report` — participant-level registration report with event info

### Advanced Queries
- Top registered events by count and revenue
- Most active users by events attended and money spent
- Upcoming events filtered by current date
- Category-level event statistics (count, avg fee, capacity)
- Venue utilisation report
- Gender and payment status breakdowns
- Index inspection with `SHOW INDEX`

---

## Files

| File | Description |
|---|---|
| `community_event_management.sql` | Complete executable MySQL script |
| `README.md` | Project documentation |

---

## How to Run

1. Open MySQL Workbench, DBeaver, or any MySQL-compatible client.
2. Connect to your MySQL server (version 8.0 or later recommended).
3. Open `community_event_management.sql`.
4. Execute the entire script.
5. The database, tables, sample data, indexes, views, and all queries will be created and run in sequence.

---

## Author

**Name:** Ragul SJ
**Email:** sjragul555@gmail.com
