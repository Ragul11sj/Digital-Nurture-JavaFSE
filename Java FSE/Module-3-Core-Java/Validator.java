package com.ragulsj.eventmanagement.util;

import com.ragulsj.eventmanagement.exception.ValidationException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+]?[0-9]{10,15}$");

    public static void validateNotBlank(String value, String fieldName) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName, "cannot be blank");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        validateNotBlank(email, "email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("email", "invalid format: " + email);
        }
    }

    public static void validatePhone(String phone) throws ValidationException {
        validateNotBlank(phone, "phone");
        if (!PHONE_PATTERN.matcher(phone.replaceAll("\\s", "")).matches()) {
            throw new ValidationException("phone", "invalid format: " + phone);
        }
    }

    public static void validateAge(int age) throws ValidationException {
        if (age < 18 || age > 120) {
            throw new ValidationException("age", "must be between 18 and 120, got: " + age);
        }
    }

    public static void validatePositive(double value, String fieldName) throws ValidationException {
        if (value < 0) {
            throw new ValidationException(fieldName, "must be non-negative, got: " + value);
        }
    }

    public static void validateFutureDate(LocalDate date) throws ValidationException {
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new ValidationException("date", "must be a future date, got: " + date);
        }
    }

    public static void validateSeats(int seats) throws ValidationException {
        if (seats <= 0 || seats > 10000) {
            throw new ValidationException("seats", "must be between 1 and 10000, got: " + seats);
        }
    }

    public static void validatePositiveId(int id, String fieldName) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException(fieldName, "must be positive, got: " + id);
        }
    }
}
