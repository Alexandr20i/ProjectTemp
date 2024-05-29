package com.example.ProjectTemp.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UserRegistrationDto {
    private String name;
    private String fnameUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bday;

    private String gender;
    private String country;
    private String city;
    private Double height;
    private Double weight;
    private String email;
    private String username;
    private String password;
}