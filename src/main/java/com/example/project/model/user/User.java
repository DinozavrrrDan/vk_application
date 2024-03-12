package com.example.project.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User{
    public String id;
    public String name;
    public String username;
    public String email;
    public Address address;
    public String phone;
    public String website;
    public Company company;
}
