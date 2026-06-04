package com.ragulsj.eventmanagement.jdbc;

import com.ragulsj.eventmanagement.exception.EventManagementException;
import com.ragulsj.eventmanagement.model.Event;
import com.ragulsj.eventmanagement.model.Registration;
import com.ragulsj.eventmanagement.model.User;
import com.ragulsj.eventmanagement.util.AppLogger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcService {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(DatabaseConfig.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASSWORD);
    }

    public void insertUser(User user) throws EventManagementException {
        String sql = "INSERT INTO users (user_id, full_name, email, phone, age, city, joined_date) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getAge());
            ps.setString(6, user.getCity());
            ps.setDate(7, Date.valueOf(user.getJoinedDate()));
            ps.executeUpdate();
            AppLogger.info("JDBC: Inserted user " + user.getUserId());
        } catch (SQLException e) {
            AppLogger.error("JDBC insertUser failed", e);
            throw new EventManagementException("Database error inserting user: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() throws EventManagementException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY full_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setAge(rs.getInt("age"));
                u.setCity(rs.getString("city"));
                u.setJoinedDate(rs.getDate("joined_date").toLocalDate());
                users.add(u);
            }
        } catch (SQLException e) {
            AppLogger.error("JDBC getAllUsers failed", e);
            throw new EventManagementException("Database error fetching users: " + e.getMessage(), e);
        }
        return users;
    }

    public void updateUser(User user) throws EventManagementException {
        String sql = "UPDATE users SET full_name=?, email=?, phone=?, age=?, city=? WHERE user_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getAge());
            ps.setString(5, user.getCity());
            ps.setInt(6, user.getUserId());
            ps.executeUpdate();
            AppLogger.info("JDBC: Updated user " + user.getUserId());
        } catch (SQLException e) {
            AppLogger.error("JDBC updateUser failed", e);
            throw new EventManagementException("Database error updating user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(int userId) throws EventManagementException {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            AppLogger.info("JDBC: Deleted user " + userId);
        } catch (SQLException e) {
            AppLogger.error("JDBC deleteUser failed", e);
            throw new EventManagementException("Database error deleting user: " + e.getMessage(), e);
        }
    }

    public void insertEvent(Event event) throws EventManagementException {
        String sql = "INSERT INTO events (event_id, event_name, category, event_date, start_time, location, total_seats, available_seats, fee, organizer, status, description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, event.getEventId());
            ps.setString(2, event.getEventName());
            ps.setString(3, event.getCategory().name());
            ps.setDate(4, Date.valueOf(event.getEventDate()));
            ps.setTime(5, Time.valueOf(event.getStartTime()));
            ps.setString(6, event.getLocation());
            ps.setInt(7, event.getTotalSeats());
            ps.setInt(8, event.getAvailableSeats());
            ps.setDouble(9, event.getFee());
            ps.setString(10, event.getOrganizer());
            ps.setString(11, event.getStatus().name());
            ps.setString(12, event.getDescription());
            ps.executeUpdate();
            AppLogger.info("JDBC: Inserted event " + event.getEventId());
        } catch (SQLException e) {
            AppLogger.error("JDBC insertEvent failed", e);
            throw new EventManagementException("Database error inserting event: " + e.getMessage(), e);
        }
    }

    public List<Event> getAllEvents() throws EventManagementException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Event e = new Event();
                e.setEventId(rs.getInt("event_id"));
                e.setEventName(rs.getString("event_name"));
                e.setCategory(Event.Category.valueOf(rs.getString("category")));
                e.setEventDate(rs.getDate("event_date").toLocalDate());
                e.setStartTime(rs.getTime("start_time").toLocalTime());
                e.setLocation(rs.getString("location"));
                e.setTotalSeats(rs.getInt("total_seats"));
                e.setAvailableSeats(rs.getInt("available_seats"));
                e.setFee(rs.getDouble("fee"));
                e.setOrganizer(rs.getString("organizer"));
                e.setStatus(Event.Status.valueOf(rs.getString("status")));
                e.setDescription(rs.getString("description"));
                events.add(e);
            }
        } catch (SQLException e) {
            AppLogger.error("JDBC getAllEvents failed", e);
            throw new EventManagementException("Database error fetching events: " + e.getMessage(), e);
        }
        return events;
    }

    public void registerUserWithTransaction(Registration reg, int eventId) throws EventManagementException {
        String insertReg = "INSERT INTO registrations (registration_id, user_id, event_id, registration_time, payment_status, amount_paid, attendance_status) VALUES (?,?,?,?,?,?,?)";
        String updateSeats = "UPDATE events SET available_seats = available_seats - 1 WHERE event_id = ? AND available_seats > 0";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(insertReg)) {
                ps1.setInt(1, reg.getRegistrationId());
                ps1.setInt(2, reg.getUserId());
                ps1.setInt(3, reg.getEventId());
                ps1.setTimestamp(4, Timestamp.valueOf(reg.getRegistrationTime()));
                ps1.setString(5, reg.getPaymentStatus().name());
                ps1.setDouble(6, reg.getAmountPaid());
                ps1.setString(7, reg.getAttendanceStatus().name());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(updateSeats)) {
                ps2.setInt(1, eventId);
                int updated = ps2.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    throw new EventManagementException("No available seats — transaction rolled back.", "TXN-001");
                }
            }

            conn.commit();
            AppLogger.info("JDBC Transaction: Registration " + reg.getRegistrationId() + " committed.");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { AppLogger.error("Rollback failed", ex); }
            }
            AppLogger.error("JDBC transaction failed", e);
            throw new EventManagementException("Transaction failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            AppLogger.warn("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
