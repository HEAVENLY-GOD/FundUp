package com.example.miniproject.model;

public class User {
    private String userId;  // User ID
    private String firstName;
    private String lastName;
    private String email;
    private String joinDate;  // Add join date

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    // Constructor with firstName, lastName, email, and joinDate
    public User(String firstName, String lastName, String email, String joinDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.joinDate = joinDate;
    }

    // Constructor with userId, firstName, lastName, email, and joinDate
    public User(String userId, String firstName, String lastName, String email, String joinDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.joinDate = joinDate;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}
