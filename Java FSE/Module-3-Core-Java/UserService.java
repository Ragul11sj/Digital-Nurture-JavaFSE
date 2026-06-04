package com.ragulsj.eventmanagement.service;

import com.ragulsj.eventmanagement.collection.CollectionManager;
import com.ragulsj.eventmanagement.exception.*;
import com.ragulsj.eventmanagement.model.User;
import com.ragulsj.eventmanagement.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class UserService {

    private final CollectionManager cm;

    public UserService(CollectionManager cm) {
        this.cm = cm;
    }

    public User addUser(String name, String email, String phone, int age, String city)
            throws ValidationException {
        Validator.validateNotBlank(name, "name");
        Validator.validateEmail(email);
        Validator.validatePhone(phone);
        Validator.validateAge(age);
        Validator.validateNotBlank(city, "city");

        if (cm.emailExists(email)) {
            throw new ValidationException("email", "already registered: " + email);
        }

        int id = IdGenerator.nextUserId();
        User user = new User(id, name, email, phone, age, city);
        cm.addUser(user);
        AppLogger.info("User added: " + user);
        return user;
    }

    public User addUser(String name, String email) throws ValidationException {
        return addUser(name, email, "0000000000", 18, "Chennai");
    }

    public User getUserById(int userId) throws UserNotFoundException {
        return cm.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(cm.getAllUsers());
    }

    public List<User> searchUsers(String keyword) {
        String kw = keyword.toLowerCase();
        return cm.getAllUsers().stream()
                .filter(u -> u.getName().toLowerCase().contains(kw)
                        || u.getEmail().toLowerCase().contains(kw)
                        || u.getCity().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public void updateUser(int userId, String name, String email, String phone, int age, String city)
            throws UserNotFoundException, ValidationException {
        User user = getUserById(userId);

        if (name != null && !name.isBlank()) {
            Validator.validateNotBlank(name, "name");
            user.setName(name);
        }
        if (email != null && !email.isBlank()) {
            Validator.validateEmail(email);
            if (!user.getEmail().equals(email) && cm.emailExists(email)) {
                throw new ValidationException("email", "already in use: " + email);
            }
            cm.updateUserEmail(userId, email);
        }
        if (phone != null && !phone.isBlank()) {
            Validator.validatePhone(phone);
            user.setPhone(phone);
        }
        if (age > 0) {
            Validator.validateAge(age);
            user.setAge(age);
        }
        if (city != null && !city.isBlank()) {
            user.setCity(city);
        }

        user.touch();
        AppLogger.info("User updated: " + userId);
    }

    public void deleteUser(int userId) throws UserNotFoundException {
        getUserById(userId);
        cm.removeUser(userId);
        AppLogger.info("User deleted: " + userId);
    }

    public void printAllUsers() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) {
            ConsoleUtil.printWarning("No users registered.");
            return;
        }
        System.out.printf("%n%-5s %-25s %-30s %-15s %-5s %-15s%n",
                "ID", "NAME", "EMAIL", "PHONE", "AGE", "CITY");
        ConsoleUtil.printDivider();
        users.stream()
                .sorted(Comparator.comparing(User::getName))
                .forEach(u -> System.out.println(u.toDisplayString()));
        ConsoleUtil.printDivider();
        System.out.println("Total: " + users.size() + " user(s)");
    }

    public void printUserDetails(int userId) throws UserNotFoundException {
        User u = getUserById(userId);
        System.out.println("\nUser Details:");
        ConsoleUtil.printDivider();
        System.out.println("ID       : " + u.getUserId());
        System.out.println("Name     : " + u.getName());
        System.out.println("Email    : " + u.getEmail());
        System.out.println("Phone    : " + u.getPhone());
        System.out.println("Age      : " + u.getAge());
        System.out.println("City     : " + u.getCity());
        System.out.println("Joined   : " + u.getJoinedDate());
        ConsoleUtil.printDivider();
    }

    public int getTotalUsers() {
        return cm.getAllUsers().size();
    }

    public static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    public static int fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
