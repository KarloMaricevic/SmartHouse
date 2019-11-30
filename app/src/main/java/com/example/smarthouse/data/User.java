package com.example.smarthouse.data;


public class User {
    protected String username;
    protected String password;
    protected String e_mail;
    protected String firstName;
    protected String lastName;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String e_mail, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.e_mail = e_mail;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.e_mail = user.e_mail;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
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


}