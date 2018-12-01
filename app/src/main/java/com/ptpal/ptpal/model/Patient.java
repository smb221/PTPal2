package com.ptpal.ptpal.model;

public class Patient
{
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int height;
    private String gender;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstname)
    {
        this.firstName = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname)
    {
        this.lastName = lastname;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
