package com.ragulsj.eventmanagement.model;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = 1L;

    private int userId;
    private String name;
    private String email;
    private String phone;
    private int age;
    private String city;
    private LocalDate joinedDate;

    public User() {}

    public User(int userId, String name, String email, String phone, int age, String city) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.city = city;
        this.joinedDate = LocalDate.now();
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; }

    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name=%s, email=%s, phone=%s, age=%d, city=%s]",
                userId, name, email, phone, age, city);
    }

    public String toDisplayString() {
        return String.format("%-5d %-25s %-30s %-15s %-5d %-15s %s",
                userId, name, email, phone, age, city, joinedDate);
    }
}
